package dev.soulware.beneficiarios.interfaces.rest.dto;

import java.util.List;

public record BeneficiaryLookupRequest(List<LegalDocumentResource> legalDocuments) {}
