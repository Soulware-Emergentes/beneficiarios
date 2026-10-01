package dev.soulware.beneficiarios.interfaces.rest.controllers;

import dev.soulware.beneficiarios.application.services.BeneficiaryService;
import dev.soulware.beneficiarios.domain.model.entities.Beneficiary;
import dev.soulware.beneficiarios.interfaces.rest.dto.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<Page<Beneficiary>> getBeneficiaries(
            @RequestParam(required = false) String names,
            @RequestParam(required = false) String paternalSurname,
            @RequestParam(required = false) String maternalSurname,
            @RequestParam(required = false) String dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Beneficiary> result = beneficiaryService.getBeneficiaries(
                names, paternalSurname, maternalSurname, dni, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Beneficiary> getBeneficiaryByDni(@PathVariable String dni) {
        return beneficiaryService.getBeneficiaryByDni(dni)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
