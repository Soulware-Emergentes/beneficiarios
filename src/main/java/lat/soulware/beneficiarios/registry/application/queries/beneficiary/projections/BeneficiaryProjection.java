package lat.soulware.beneficiarios.registry.application.queries.beneficiary.projections;

import java.util.List;

import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesPage;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiariesPageResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryResult;
import lat.soulware.beneficiarios.shared.application.queries.projections.Projection;

/** The beneficiaries the read store holds. */
public interface BeneficiaryProjection extends Projection {

    /**
     * @param criteria the page to read, its filters, sort and bounds already checked
     * @return the beneficiaries on it, with the size of the whole selection
     */
    BeneficiariesPageResult findPage(BeneficiariesPage criteria);

    /**
     * @param criteria the documents to resolve, each already checked to be a legal document
     * @return the beneficiaries holding any of them, in no particular order
     */
    List<BeneficiaryResult> findByDocuments(BeneficiariesByDocument criteria);
}
