package dev.soulware.beneficiarios.interfaces.rest.controllers;

import dev.soulware.beneficiarios.application.services.BeneficiaryService;
import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocument;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocumentType;
import dev.soulware.beneficiarios.interfaces.rest.dto.BeneficiaryLookupRequest;
import dev.soulware.beneficiarios.interfaces.rest.dto.BeneficiaryLookupResponse;
import dev.soulware.beneficiarios.interfaces.rest.dto.BeneficiaryResourceV2;
import dev.soulware.beneficiarios.interfaces.rest.dto.LegalDocumentResource;
import dev.soulware.beneficiarios.interfaces.rest.dto.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/beneficiaries")
public class BeneficiaryControllerV2 {

    private static final int MAX_LOOKUP_SIZE = 100;

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryControllerV2(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<Page<BeneficiaryResourceV2>> getBeneficiaries(
            @RequestParam(required = false) String names,
            @RequestParam(required = false) String paternalSurname,
            @RequestParam(required = false) String maternalSurname,
            @RequestParam(required = false) LegalDocumentType legalDocumentType,
            @RequestParam(required = false) String legalDocumentNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Beneficiary> result = beneficiaryService.getBeneficiaries(
                names, paternalSurname, maternalSurname, legalDocumentType, legalDocumentNumber, page, size);

        return ResponseEntity.ok(new Page<>(
                result.content().stream().map(BeneficiaryResourceV2::from).toList(),
                result.totalPages(),
                result.totalElements(),
                result.actualPage(),
                result.pageSize()
        ));
    }

    /**
     * Resolves one or many identities in a single request. Identities nobody holds come back under
     * {@code notFound} instead of failing the request.
     */
    @PostMapping("/lookup")
    public ResponseEntity<BeneficiaryLookupResponse> lookup(@RequestBody BeneficiaryLookupRequest request) {
        List<LegalDocumentResource> requested = request.legalDocuments() == null
                ? List.of()
                : request.legalDocuments();
        if (requested.size() > MAX_LOOKUP_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "At most " + MAX_LOOKUP_SIZE + " legal documents can be looked up at once");
        }

        Set<LegalDocument> documents = new LinkedHashSet<>();
        for (LegalDocumentResource resource : requested) {
            try {
                documents.add(new LegalDocument(resource.type(), resource.number()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid legal document " + resource + ": " + e.getMessage());
            }
        }

        List<BeneficiaryResourceV2> found = beneficiaryService.findByLegalDocuments(documents).stream()
                .map(BeneficiaryResourceV2::from)
                .toList();
        Set<LegalDocument> foundDocuments = found.stream()
                .map(resource -> new LegalDocument(resource.legalDocument().type(), resource.legalDocument().number()))
                .collect(Collectors.toSet());
        List<LegalDocumentResource> notFound = documents.stream()
                .filter(document -> !foundDocuments.contains(document))
                .map(LegalDocumentResource::from)
                .toList();

        return ResponseEntity.ok(new BeneficiaryLookupResponse(found, notFound));
    }
}
