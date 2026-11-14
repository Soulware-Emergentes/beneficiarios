package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import lat.soulware.beneficiarios.registry.application.queries.beneficiary.BeneficiaryQueryService;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.mappers.BeneficiaryWireMapper;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.requests.BeneficiaryLookupRequest;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.BeneficiariesPageResponse;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.BeneficiaryLookupResponse;

/** The registry's beneficiaries, listed a page at a time and looked up by legal document. */
@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryQueryService beneficiaryQueryService;

    public BeneficiaryController(BeneficiaryQueryService beneficiaryQueryService) {
        this.beneficiaryQueryService = beneficiaryQueryService;
    }

    /** One page of beneficiaries for a table: every filter optional, ordered by name unless sorted otherwise. */
    @Operation(summary = "Returns a paginated set of beneficiaries")
    @GetMapping
    public BeneficiariesPageResponse page(
        @RequestParam(required = false) String names,
        @RequestParam(required = false) String paternalSurname,
        @RequestParam(required = false) String maternalSurname,
        @RequestParam(required = false) String legalDocumentType,
        @RequestParam(required = false) String legalDocumentNumber,
        @RequestParam(defaultValue = "paternalSurname") String sort,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return BeneficiaryWireMapper.toBeneficiariesPageResponse(this.beneficiaryQueryService.page(
            BeneficiaryWireMapper.toBeneficiariesPage(
                names,
                paternalSurname,
                maternalSurname,
                legalDocumentType,
                legalDocumentNumber,
                sort,
                direction,
                page,
                size
            )
        ));
    }

    /**
     * Resolves one or many legal documents in a single request. Documents nobody holds come back
     * under {@code notFound} instead of failing the request.
     */
    @Operation(summary = "Returns the beneficiaries holding the given legal documents, and the documents nobody holds")
    @PostMapping("/lookup")
    public BeneficiaryLookupResponse lookup(@RequestBody BeneficiaryLookupRequest request) {
        return BeneficiaryWireMapper.toBeneficiaryLookupResponse(
            this.beneficiaryQueryService.lookup(BeneficiaryWireMapper.toBeneficiariesByDocument(request))
        );
    }
}
