package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses;

import java.time.LocalDate;

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
public record BeneficiaryResponse(
    LegalDocumentResponse legalDocument,
    String names,
    String paternalSurname,
    String maternalSurname,
    LocalDate dateOfBirth,
    String ubigeo
) {
}
