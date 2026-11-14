package lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/** The Spring Data delegate behind {@code JpaBeneficiaryRepositoryAdapter}. */
public interface BeneficiaryJpaRepository extends JpaRepository<BeneficiaryEntity, UUID> {
}
