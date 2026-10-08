package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.mappers;

import java.util.List;

import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesPage;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiaryByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiariesPageResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryLookupResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.LegalDocumentResult;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.requests.BeneficiaryLookupRequest;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.BeneficiariesPageResponse;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.BeneficiaryLookupResponse;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.BeneficiaryResponse;
import lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses.LegalDocumentResponse;
import lat.soulware.beneficiarios.shared.interfaces.rest.mappers.WireMapper;

/** Converts what {@code BeneficiaryController} receives and answers. */
public final class BeneficiaryWireMapper implements WireMapper {

    private BeneficiaryWireMapper() {
    }

    /** Blank filters, as a table sends an emptied field, match everything like absent ones. */
    public static BeneficiariesPage toBeneficiariesPage(
        String names,
        String paternalSurname,
        String maternalSurname,
        String legalDocumentType,
        String legalDocumentNumber,
        String sort,
        String direction,
        int page,
        int size
    ) {
        return new BeneficiariesPage(
            BeneficiaryWireMapper.blankToNull(names),
            BeneficiaryWireMapper.blankToNull(paternalSurname),
            BeneficiaryWireMapper.blankToNull(maternalSurname),
            BeneficiaryWireMapper.blankToNull(legalDocumentType),
            BeneficiaryWireMapper.blankToNull(legalDocumentNumber),
            sort,
            direction,
            page,
            size
        );
    }

    public static BeneficiariesByDocument toBeneficiariesByDocument(BeneficiaryLookupRequest request) {
        boolean namesDocuments = request.legalDocuments() != null;
        List<BeneficiaryByDocument> documents = namesDocuments
            ? request.legalDocuments().stream()
                .map(document -> new BeneficiaryByDocument(document.type(), document.number()))
                .toList()
            : List.of();

        return new BeneficiariesByDocument(documents);
    }

    public static BeneficiariesPageResponse toBeneficiariesPageResponse(BeneficiariesPageResult result) {
        return new BeneficiariesPageResponse(
            BeneficiaryWireMapper.toBeneficiaryResponses(result.content()),
            result.totalPages(),
            result.totalElements(),
            result.actualPage(),
            result.pageSize()
        );
    }

    public static BeneficiaryLookupResponse toBeneficiaryLookupResponse(BeneficiaryLookupResult result) {
        List<LegalDocumentResponse> notFound = result.notFound().stream()
            .map(BeneficiaryWireMapper::toLegalDocumentResponse)
            .toList();

        return new BeneficiaryLookupResponse(BeneficiaryWireMapper.toBeneficiaryResponses(result.found()), notFound);
    }

    private static List<BeneficiaryResponse> toBeneficiaryResponses(List<BeneficiaryResult> results) {
        return results.stream()
            .map(beneficiary -> new BeneficiaryResponse(
                BeneficiaryWireMapper.toLegalDocumentResponse(beneficiary.legalDocument()),
                beneficiary.names(),
                beneficiary.paternalSurname(),
                beneficiary.maternalSurname(),
                beneficiary.dateOfBirth(),
                beneficiary.ubigeo()
            ))
            .toList();
    }

    private static LegalDocumentResponse toLegalDocumentResponse(LegalDocumentResult document) {
        return new LegalDocumentResponse(document.type(), document.number());
    }

    private static String blankToNull(String value) {
        boolean isBlank = value == null || value.isBlank();
        return isBlank ? null : value;
    }
}
