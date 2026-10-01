package dev.soulware.beneficiarios.infrastructure.persistence.jpa.repositories;

import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends
        JpaRepository<Beneficiary, String>,
        JpaSpecificationExecutor<Beneficiary> {
}
