package dev.soulware.beneficiarios.interfaces.rest.dto;

import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;

import java.time.LocalDate;

public record BeneficiaryResourceV2(
        LegalDocumentResource legalDocument,
        String names,
        String paternalSurname,
        String maternalSurname,
        LocalDate dateOfBirth,
        String ubigeo
) {
    public static BeneficiaryResourceV2 from(Beneficiary beneficiary) {
        return new BeneficiaryResourceV2(
                LegalDocumentResource.from(beneficiary.getLegalDocument()),
                beneficiary.getNames(),
                beneficiary.getPaternalSurname(),
                beneficiary.getMaternalSurname(),
                beneficiary.getDateOfBirth(),
                beneficiary.getUbigeo()
        );
    }
}
