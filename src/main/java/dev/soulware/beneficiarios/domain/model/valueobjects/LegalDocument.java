package dev.soulware.beneficiarios.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record LegalDocument(
        @Enumerated(EnumType.STRING)
        @Column(name = "legal_document_type", length = 20, nullable = false)
        LegalDocumentType type,

        @Column(name = "legal_document", length = LegalDocumentType.MAX_NUMBER_LENGTH, nullable = false)
        String number
) {
    public LegalDocument {
        if (type == null) {
            throw new IllegalArgumentException("Legal document type is required");
        }
        if (!type.matches(number)) {
            throw new IllegalArgumentException("Number does not match the format of " + type);
        }
    }
}
