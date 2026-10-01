package dev.soulware.beneficiarios.infrastructure.persistence.jpa.repositories;

import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeneficiaryRepository extends
        JpaRepository<Beneficiary, UUID>,
        JpaSpecificationExecutor<Beneficiary> {

    Optional<Beneficiary> findByLegalDocument(LegalDocument legalDocument);
}
