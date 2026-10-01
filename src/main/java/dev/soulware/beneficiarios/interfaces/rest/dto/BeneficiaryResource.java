package dev.soulware.beneficiarios.interfaces.rest.dto;

import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;

import java.time.LocalDate;

/** The v1 shape, which only knows DNI holders and exposes the number as {@code dni}. */
public record BeneficiaryResource(
        String dni,
        String names,
        String paternalSurname,
        String maternalSurname,
        LocalDate dateOfBirth,
        String ubigeo
) {
    public static BeneficiaryResource from(Beneficiary beneficiary) {
        return new BeneficiaryResource(
                beneficiary.getLegalDocument().number(),
                beneficiary.getNames(),
                beneficiary.getPaternalSurname(),
                beneficiary.getMaternalSurname(),
                beneficiary.getDateOfBirth(),
                beneficiary.getUbigeo()
        );
    }
}
