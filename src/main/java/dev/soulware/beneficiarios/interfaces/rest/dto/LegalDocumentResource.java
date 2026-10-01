package dev.soulware.beneficiarios.interfaces.rest.dto;

import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocument;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocumentType;

public record LegalDocumentResource(LegalDocumentType type, String number) {

    public static LegalDocumentResource from(LegalDocument legalDocument) {
        return new LegalDocumentResource(legalDocument.type(), legalDocument.number());
    }
}
