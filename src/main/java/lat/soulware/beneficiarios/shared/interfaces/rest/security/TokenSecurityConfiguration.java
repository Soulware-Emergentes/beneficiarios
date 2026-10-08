package lat.soulware.beneficiarios.shared.interfaces.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Admits a request only with a bearer token staff issued to a service: signed with one of the keys
 * staff publishes, naming staff as its issuer, addressed to beneficiarios, and carrying the
 * {@code beneficiaries.read} scope. The issuer and the audience come from
 * {@code spring.security.oauth2.resourceserver.jwt.*}, which Boot turns into the decoder checking them.
 *
 * <p>Only the other systems of the program call beneficiarios, so a token staff issued to a person
 * is refused, as {@code token_use: user} marks it, even when addressed here.
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

    @Bean
    SecurityFilterChain tokenSecurity(HttpSecurity http, UnauthenticatedEntryPoint entryPoint) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().hasAuthority("SCOPE_beneficiaries.read")
            )
            .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults())
                .authenticationEntryPoint(entryPoint)
            )
            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(entryPoint))
            .build();
    }

    /** Refuses an access token not issued to a service, which Boot adds to the decoder it builds. */
    @Bean
    OAuth2TokenValidator<Jwt> serviceTokensOnly() {
        return new JwtClaimValidator<String>(TOKEN_USE_CLAIM, SERVICE_TOKEN_USE::equals);
    }
}
