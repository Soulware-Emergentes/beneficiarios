package lat.soulware.beneficiarios.registry.domain.repositories;

import lat.soulware.beneficiarios.registry.domain.model.aggregates.Beneficiary;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.BeneficiaryId;
import lat.soulware.beneficiarios.shared.domain.repositories.DomainRepository;

/** Stores {@link Beneficiary} aggregates. */
public interface BeneficiaryRepository extends DomainRepository<Beneficiary, BeneficiaryId> {
}
