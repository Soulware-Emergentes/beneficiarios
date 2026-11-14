package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses;

import java.util.List;

/**
 * The outcome of resolving several legal documents.
 *
 * @param found    the beneficiaries holding a requested document
 * @param notFound the requested documents nobody in the registry holds
 */
public record BeneficiaryLookupResponse(List<BeneficiaryResponse> found, List<LegalDocumentResponse> notFound) {
}
