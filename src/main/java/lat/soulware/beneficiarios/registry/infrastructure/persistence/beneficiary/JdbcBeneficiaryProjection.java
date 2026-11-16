package lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesPage;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.projections.BeneficiaryProjection;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiariesPageResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.LegalDocumentResult;

/** Reads beneficiaries straight from the {@code beneficiaries} table. */
@Component
public class JdbcBeneficiaryProjection implements BeneficiaryProjection {

    private static final String COLUMNS = """
        SELECT legal_document_type, legal_document, names, paternal_surname, maternal_surname, date_of_birth, ubigeo
        """;

    private static final String MATCHING = """
        FROM beneficiaries
        WHERE (CAST(:names AS VARCHAR) IS NULL OR lower(names) LIKE '%' || lower(:names) || '%')
            AND (CAST(:paternalSurname AS VARCHAR) IS NULL OR lower(paternal_surname) LIKE '%' || lower(:paternalSurname) || '%')
            AND (CAST(:maternalSurname AS VARCHAR) IS NULL OR lower(maternal_surname) LIKE '%' || lower(:maternalSurname) || '%')
            AND (CAST(:legalDocumentType AS VARCHAR) IS NULL OR legal_document_type = :legalDocumentType)
            AND (CAST(:legalDocumentNumber AS VARCHAR) IS NULL OR legal_document LIKE '%' || :legalDocumentNumber || '%')""";

    private static final String COUNT_PAGE = "SELECT count(*) " + MATCHING;

    private static final String FIND_PAGE = COLUMNS + """
        %s
        ORDER BY %s, id
        LIMIT :size OFFSET :offset""";

    private static final String FIND_BY_DOCUMENTS = COLUMNS + """
        FROM beneficiaries
        WHERE (legal_document_type, legal_document) IN (:documents)""";

    /**
     * What each sortable column orders by, with {@code %1$s} standing for the direction. Sorting by
     * the paternal surname orders by the whole name, the way a register of people reads.
     */
    private static final Map<String, String> ORDER_BY = Map.of(
        "paternalSurname", "paternal_surname %1$s, maternal_surname %1$s, names %1$s",
        "maternalSurname", "maternal_surname %1$s",
        "names", "names %1$s",
        "legalDocumentNumber", "legal_document %1$s",
        "dateOfBirth", "date_of_birth %1$s",
        "ubigeo", "ubigeo %1$s"
    );

    private final NamedParameterJdbcTemplate jdbc;

    public JdbcBeneficiaryProjection(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public BeneficiariesPageResult findPage(BeneficiariesPage criteria) {
        boolean isDescending = "desc".equalsIgnoreCase(criteria.direction());
        String direction = isDescending ? "DESC" : "ASC";
        String orderBy = ORDER_BY.get(criteria.sort()).formatted(direction);
        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("names", criteria.names())
            .addValue("paternalSurname", criteria.paternalSurname())
            .addValue("maternalSurname", criteria.maternalSurname())
            .addValue("legalDocumentType", criteria.legalDocumentType())
            .addValue("legalDocumentNumber", criteria.legalDocumentNumber())
            .addValue("size", criteria.size())
            .addValue("offset", (long) criteria.page() * criteria.size());

        long totalElements = this.jdbc.queryForObject(COUNT_PAGE, parameters, Long.class);
        List<BeneficiaryResult> content = this.jdbc.query(
            FIND_PAGE.formatted(MATCHING, orderBy),
            parameters,
            (row, rowNumber) -> JdbcBeneficiaryProjection.toResult(row)
        );
        int totalPages = (int) Math.ceilDiv(totalElements, criteria.size());

        return new BeneficiariesPageResult(content, totalPages, totalElements, criteria.page(), criteria.size());
    }

    @Override
    public List<BeneficiaryResult> findByDocuments(BeneficiariesByDocument criteria) {
        if (criteria.documents().isEmpty()) {
            return List.of();
        }
        List<Object[]> documents = criteria.documents().stream()
            .map(document -> new Object[] {document.type(), document.number()})
            .toList();
        MapSqlParameterSource parameters = new MapSqlParameterSource("documents", documents);

        return this.jdbc.query(
            FIND_BY_DOCUMENTS,
            parameters,
            (row, rowNumber) -> JdbcBeneficiaryProjection.toResult(row)
        );
    }

    private static BeneficiaryResult toResult(ResultSet row) throws SQLException {
        return new BeneficiaryResult(
            new LegalDocumentResult(row.getString("legal_document_type"), row.getString("legal_document")),
            row.getString("names"),
            row.getString("paternal_surname"),
            row.getString("maternal_surname"),
            row.getObject("date_of_birth", LocalDate.class),
            row.getString("ubigeo")
        );
    }
}
