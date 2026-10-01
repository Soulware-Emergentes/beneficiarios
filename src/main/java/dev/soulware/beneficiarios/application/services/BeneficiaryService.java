package dev.soulware.beneficiarios.application.services;

import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocument;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocumentType;
import dev.soulware.beneficiarios.infrastructure.persistence.jpa.repositories.BeneficiaryRepository;
import dev.soulware.beneficiarios.interfaces.rest.dto.Page;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Page<Beneficiary> getBeneficiaries(
            String names, String paternalSurname, String maternalSurname,
            LegalDocumentType legalDocumentType, String legalDocumentNumber, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Beneficiary> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (names != null && !names.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("names")), "%" + names.toLowerCase() + "%"));
            }
            if (paternalSurname != null && !paternalSurname.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("paternalSurname")), "%" + paternalSurname.toLowerCase() + "%"));
            }
            if (maternalSurname != null && !maternalSurname.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("maternalSurname")), "%" + maternalSurname.toLowerCase() + "%"));
            }
            if (legalDocumentType != null) {
                predicates.add(criteriaBuilder.equal(root.get("legalDocument").get("type"), legalDocumentType));
            }
            if (legalDocumentNumber != null && !legalDocumentNumber.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("legalDocument").get("number"), legalDocumentNumber));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        org.springframework.data.domain.Page<Beneficiary> springPage = beneficiaryRepository.findAll(spec, pageRequest);

        return new Page<>(
                springPage.getContent(),
                springPage.getTotalPages(),
                springPage.getTotalElements(),
                springPage.getNumber(),
                springPage.getSize()
        );
    }

    /**
     * Resolves many identities in one query. Identities nobody holds are simply absent from the result.
     */
    public List<Beneficiary> findByLegalDocuments(Collection<LegalDocument> legalDocuments) {
        if (legalDocuments.isEmpty()) {
            return List.of();
        }
        Specification<Beneficiary> spec = (root, query, criteriaBuilder) -> criteriaBuilder.or(
                legalDocuments.stream()
                        .map(document -> criteriaBuilder.and(
                                criteriaBuilder.equal(root.get("legalDocument").get("type"), document.type()),
                                criteriaBuilder.equal(root.get("legalDocument").get("number"), document.number())))
                        .toArray(Predicate[]::new));

        return beneficiaryRepository.findAll(spec);
    }

    public Optional<Beneficiary> getBeneficiaryByLegalDocument(LegalDocumentType type, String number) {
        if (type == null || !type.matches(number)) {
            return Optional.empty();
        }
        return beneficiaryRepository.findByLegalDocument(new LegalDocument(type, number));
    }
}
