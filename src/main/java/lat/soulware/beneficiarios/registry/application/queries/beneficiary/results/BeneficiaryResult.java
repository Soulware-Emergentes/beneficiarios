package lat.soulware.beneficiarios.registry.application.queries.beneficiary.results;

import java.time.LocalDate;

import lat.soulware.beneficiarios.shared.application.queries.results.QueryResult;

/**
 * A beneficiary.
 *
 * @param legalDocument   the legal document identifying them
 * @param names           their given names
 * @param paternalSurname their paternal surname
 * @param maternalSurname their maternal surname
 * @param dateOfBirth     when they were born
 * @param ubigeo          the district they live in
 */
public record BeneficiaryResult(
    LegalDocumentResult legalDocument,
    String names,
    String paternalSurname,
    String maternalSurname,
    LocalDate dateOfBirth,
    String ubigeo
) implements QueryResult {
}
