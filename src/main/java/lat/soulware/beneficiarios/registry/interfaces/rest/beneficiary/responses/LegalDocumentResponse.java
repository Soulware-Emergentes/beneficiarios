package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses;

/**
 * A legal document.
 *
 * @param type   the kind of document
 * @param number the document number
 */
public record LegalDocumentResponse(String type, String number) {
}
