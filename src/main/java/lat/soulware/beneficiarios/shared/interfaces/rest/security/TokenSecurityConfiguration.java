package lat.soulware.beneficiarios.shared.interfaces.rest.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Admits a request only with a bearer token staff issued: signed with one of the keys staff
 * publishes, naming staff as its issuer, and addressed to beneficiarios. The issuer and the audience
 * come from {@code spring.security.oauth2.resourceserver.jwt.*}, which Boot turns into the decoder
 * checking them.
 *
 * <p>Two kinds of token are told apart by {@code token_use}, and each is read for what it carries:
 *
 * <ul>
 *   <li>A service's token, such as sgt's, holds scopes. With {@code beneficiaries.read} it reads the
 *       whole registry.</li>
 *   <li>A person's token holds the roles they act as here. A field agent may only look up the
 *       beneficiaries whose documents they already have, to name the one a plot is registered to;
 *       listing the registry stays with services.</li>
 * </ul>
 *
 * <p>A scope is only read from a service's token and a role only from a person's, so neither kind
 * can borrow what the other is granted.
 *
 * <p>A request without such a token is answered by {@link UnauthenticatedEntryPoint}. Stateless,
 * so there is no session to forge a request against and no CSRF token either.
 *
 * <p>The OpenAPI document and the Swagger UI are the one exception, answered without a token, and
 * only served at all where {@code API_DOCS_ENABLED} is on.
 */
@Configuration
public class TokenSecurityConfiguration {

    private static final String TOKEN_USE_CLAIM = "token_use";
    private static final String SERVICE_TOKEN_USE = "service";
    private static final String PERSON_TOKEN_USE = "user";
    private static final String READ_SCOPE = "SCOPE_beneficiaries.read";
    private static final String FIELD_AGENT_ROLE = "ROLE_field-agent";

    @Bean
    SecurityFilterChain tokenSecurity(HttpSecurity http, UnauthenticatedEntryPoint entryPoint) throws Exception {
        JwtAuthenticationConverter tokenReader = new JwtAuthenticationConverter();
        tokenReader.setJwtGrantedAuthoritiesConverter(TokenSecurityConfiguration::authoritiesOf);

        return http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/beneficiaries/lookup").hasAnyAuthority(READ_SCOPE, FIELD_AGENT_ROLE)
                .anyRequest().hasAuthority(READ_SCOPE)
            )
            .oauth2ResourceServer(resourceServer -> resourceServer
                .jwt(jwt -> jwt.jwtAuthenticationConverter(tokenReader))
                .authenticationEntryPoint(entryPoint)
            )
            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(entryPoint))
            .build();
    }

    /** Refuses an access token that is neither a service's nor a person's, which Boot adds to the decoder it builds. */
    @Bean
    OAuth2TokenValidator<Jwt> serviceAndPersonTokensOnly() {
        return new JwtClaimValidator<String>(
            TOKEN_USE_CLAIM,
            tokenUse -> SERVICE_TOKEN_USE.equals(tokenUse) || PERSON_TOKEN_USE.equals(tokenUse)
        );
    }

    private static Collection<GrantedAuthority> authoritiesOf(Jwt token) {
        if (SERVICE_TOKEN_USE.equals(token.getClaimAsString(TOKEN_USE_CLAIM))) {
            String scopes = token.getClaimAsString("scope");
            return scopes == null
                ? List.of()
                : Arrays.stream(scopes.split(" ")).<GrantedAuthority>map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope)).toList();
        }
        List<String> roles = token.getClaimAsStringList("roles");
        return roles == null
            ? List.of()
            : roles.stream().<GrantedAuthority>map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
    }
}
