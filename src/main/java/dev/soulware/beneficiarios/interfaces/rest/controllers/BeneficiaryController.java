package dev.soulware.beneficiarios.interfaces.rest.controllers;

import dev.soulware.beneficiarios.application.services.BeneficiaryService;
import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocumentType;
import dev.soulware.beneficiarios.interfaces.rest.dto.BeneficiaryResource;
import dev.soulware.beneficiarios.interfaces.rest.dto.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Kept for existing clients: it only sees DNI holders and keeps the original response shape. */
@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<Page<BeneficiaryResource>> getBeneficiaries(
            @RequestParam(required = false) String names,
            @RequestParam(required = false) String paternalSurname,
            @RequestParam(required = false) String maternalSurname,
            @RequestParam(required = false) String dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Beneficiary> result = beneficiaryService.getBeneficiaries(
                names, paternalSurname, maternalSurname, LegalDocumentType.DNI, dni, page, size);

        return ResponseEntity.ok(new Page<>(
                result.content().stream().map(BeneficiaryResource::from).toList(),
                result.totalPages(),
                result.totalElements(),
                result.actualPage(),
                result.pageSize()
        ));
    }

    @GetMapping("/{dni}")
    public ResponseEntity<BeneficiaryResource> getBeneficiaryByDni(@PathVariable String dni) {
        return beneficiaryService.getBeneficiaryByLegalDocument(LegalDocumentType.DNI, dni)
                .map(BeneficiaryResource::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
