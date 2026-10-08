package lat.soulware.beneficiarios.registry.application.queries.beneficiary.results;

import java.util.List;

import lat.soulware.beneficiarios.shared.application.queries.results.QueryResult;

/**
 * The outcome of resolving several legal documents.
 *
 * @param found    the beneficiaries holding a requested document
 * @param notFound the requested documents nobody in the registry holds
 */
public record BeneficiaryLookupResult(List<BeneficiaryResult> found, List<LegalDocumentResult> notFound)
    implements QueryResult {

    public BeneficiaryLookupResult {
        found = List.copyOf(found);
        notFound = List.copyOf(notFound);
    }
}
