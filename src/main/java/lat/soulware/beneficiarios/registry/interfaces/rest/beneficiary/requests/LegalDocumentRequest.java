package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.requests;

/**
 * A legal document to resolve.
 *
 * @param type   the kind of document, by name
 * @param number the document number
 */
public record LegalDocumentRequest(String type, String number) {
}
