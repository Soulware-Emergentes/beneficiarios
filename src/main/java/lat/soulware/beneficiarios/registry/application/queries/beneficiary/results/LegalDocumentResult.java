package lat.soulware.beneficiarios.registry.application.queries.beneficiary.results;

import lat.soulware.beneficiarios.shared.application.queries.results.QueryResult;

/**
 * A legal document.
 *
 * @param type   the kind of document, by name
 * @param number the document number
 */
public record LegalDocumentResult(String type, String number) implements QueryResult {
}
