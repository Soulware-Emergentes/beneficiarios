package dev.soulware.beneficiarios.interfaces.rest.dto;

import java.util.List;

public record BeneficiaryLookupResponse(
        List<BeneficiaryResourceV2> found,
        List<LegalDocumentResource> notFound
) {}
