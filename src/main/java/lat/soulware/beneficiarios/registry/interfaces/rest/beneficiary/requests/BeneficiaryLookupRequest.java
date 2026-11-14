package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.requests;

import java.util.List;

/**
 * The legal documents to resolve.
 *
 * @param legalDocuments the documents, at most 100
 */
public record BeneficiaryLookupRequest(List<LegalDocumentRequest> legalDocuments) {
}
