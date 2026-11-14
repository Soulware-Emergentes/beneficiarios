package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.json.JsonMapper;

import lat.soulware.beneficiarios.support.PostgresIntegrationTest;
import lat.soulware.beneficiarios.support.TestTokens;

/**
 * Drives the registry's endpoints over HTTP against the seed loaded at startup, with tokens signed the
 * way staff signs them: only a service's token carrying {@code beneficiaries.read} is admitted.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BeneficiaryApiIT extends PostgresIntegrationTest {

    private static final String READER = "Bearer " + TestTokens.service("beneficiaries.read");

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper json = new JsonMapper();

    @Test
    void theApiDocsAreServedWithoutAToken() throws Exception {
        this.mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paths['/api/v1/beneficiaries'].get.tags[0]").value("beneficiary"))
            .andExpect(jsonPath("$.components.securitySchemes['bearer-token'].scheme").value("bearer"));
    }

    @Test
    void theSeedIsLoadedOnceAndPagedByName() throws Exception {
        this.mockMvc.perform(get("/api/v1/beneficiaries").header("Authorization", READER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(150))
            .andExpect(jsonPath("$.totalPages").value(15))
            .andExpect(jsonPath("$.actualPage").value(0))
            .andExpect(jsonPath("$.pageSize").value(10))
            .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void filtersNarrowThePage() throws Exception {
        this.mockMvc.perform(get("/api/v1/beneficiaries")
                .header("Authorization", READER)
                .queryParam("legalDocumentType", "FOREIGNER_ID_CARD")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(14));
        this.mockMvc.perform(get("/api/v1/beneficiaries")
                .header("Authorization", READER)
                .queryParam("legalDocumentNumber", "966450")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].legalDocument.number").value("99664504"));
    }

    @Test
    void aPageSortsByAColumnEitherWay() throws Exception {
        List<String> ascending = this.documentsSorted("asc");
        List<String> descending = this.documentsSorted("desc");

        assertTrue(ascending.size() > 1);
        assertEquals(ascending.reversed(), descending);
    }

    @Test
    void whatNoPageCanBeIsRefused() throws Exception {
        for (String[] parameter : List.of(
            new String[] {"sort", "id"},
            new String[] {"direction", "up"},
            new String[] {"size", "0"},
            new String[] {"size", "101"},
            new String[] {"page", "-1"},
            new String[] {"legalDocumentType", "LICENSE"}
        )) {
            this.mockMvc.perform(get("/api/v1/beneficiaries")
                    .header("Authorization", READER)
                    .queryParam(parameter[0], parameter[1])
                )
                .andExpect(status().isUnprocessableContent());
        }
    }

    @Test
    void aLookupResolvesWhatItCanAndReportsWhatItMisses() throws Exception {
        this.lookup("""
            {"legalDocuments": [
                {"type": "DNI", "number": "99664504"},
                {"type": "FOREIGNER_ID_CARD", "number": "8DBMJ7XACK"},
                {"type": "DNI", "number": "00000000"},
                {"type": "DNI", "number": "99664504"}
            ]}""")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.found.length()").value(2))
            .andExpect(jsonPath("$.found[?(@.legalDocument.number == '99664504')].names").value("Guillermo"))
            .andExpect(jsonPath("$.notFound.length()").value(1))
            .andExpect(jsonPath("$.notFound[0].number").value("00000000"));
    }

    @Test
    void aLookupOfNoLegalDocumentOrTooManyIsRefused() throws Exception {
        this.lookup("""
            {"legalDocuments": [{"type": "DNI", "number": "1234"}]}""")
            .andExpect(status().isUnprocessableContent());
        String tooMany = IntStream.range(0, 101)
            .mapToObj(number -> "{\"type\": \"DNI\", \"number\": \"%08d\"}".formatted(number))
            .collect(Collectors.joining(", ", "{\"legalDocuments\": [", "]}"));
        this.lookup(tooMany).andExpect(status().isUnprocessableContent());
    }

    @Test
    void onlyAServiceTokenWithTheReadScopeIsAdmitted() throws Exception {
        this.mockMvc.perform(get("/api/v1/beneficiaries"))
            .andExpect(status().isUnauthorized());
        String person = TestTokens.issue(
            TestTokens.ISSUER, TestTokens.AUDIENCE, "user", "someone", List.of("beneficiaries.read")
        );
        this.mockMvc.perform(get("/api/v1/beneficiaries").header("Authorization", "Bearer " + person))
            .andExpect(status().isUnauthorized());
        String elsewhere = TestTokens.issue(
            TestTokens.ISSUER, "poi-api-dev", "service", "sgt-service-dev", List.of("beneficiaries.read")
        );
        this.mockMvc.perform(get("/api/v1/beneficiaries").header("Authorization", "Bearer " + elsewhere))
            .andExpect(status().isUnauthorized());
        this.mockMvc.perform(get("/api/v1/beneficiaries")
                .header("Authorization", "Bearer " + TestTokens.service("tasks.read"))
            )
            .andExpect(status().isForbidden());
    }

    private ResultActions lookup(String body) throws Exception {
        return this.mockMvc.perform(post("/api/v1/beneficiaries/lookup")
            .header("Authorization", READER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        );
    }

    /** Document numbers are unique within a type, and the seed holds fewer foreigner ID cards than a page. */
    private List<String> documentsSorted(String direction) throws Exception {
        String page = this.mockMvc.perform(get("/api/v1/beneficiaries")
                .header("Authorization", READER)
                .queryParam("legalDocumentType", "FOREIGNER_ID_CARD")
                .queryParam("sort", "legalDocumentNumber")
                .queryParam("direction", direction)
                .queryParam("size", "100")
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<String> numbers = new ArrayList<>();
        this.json.readTree(page).get("content")
            .forEach(beneficiary -> numbers.add(beneficiary.get("legalDocument").get("number").asString()));
        return numbers;
    }
}
