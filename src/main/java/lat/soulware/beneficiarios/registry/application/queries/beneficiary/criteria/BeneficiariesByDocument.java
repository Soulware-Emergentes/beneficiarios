package lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria;

import java.util.List;

import lat.soulware.beneficiarios.shared.application.queries.criteria.QueryCriteria;

/**
 * The beneficiaries holding any of several legal documents.
 *
 * @param documents the documents to resolve
 */
public record BeneficiariesByDocument(List<BeneficiaryByDocument> documents) implements QueryCriteria {

    public BeneficiariesByDocument {
        documents = List.copyOf(documents);
    }
}
