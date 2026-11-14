package lat.soulware.beneficiarios.registry.application.queries.beneficiary;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiariesPage;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria.BeneficiaryByDocument;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.projections.BeneficiaryProjection;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiariesPageResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryLookupResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.BeneficiaryResult;
import lat.soulware.beneficiarios.registry.application.queries.beneficiary.results.LegalDocumentResult;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.TooManyBeneficiariesRequestedException;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.LegalDocument;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.LegalDocumentType;
import lat.soulware.beneficiarios.shared.application.queries.QueryService;
import lat.soulware.beneficiarios.shared.domain.model.exceptions.InvalidPageException;
import lat.soulware.beneficiarios.shared.domain.model.exceptions.InvalidSortDirectionException;
import lat.soulware.beneficiarios.shared.domain.model.exceptions.UnsortableColumnException;

/** Answers the reads other systems of the program make of the registry. */
@Service
@Transactional(readOnly = true)
public class BeneficiaryQueryService implements QueryService {

    private static final int MAX_LOOKUP_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 100;

    /** The columns a page of beneficiaries sorts by, as the client names them. */
    private static final String SORTABLE = "paternalSurname, maternalSurname, names, legalDocumentNumber, dateOfBirth, ubigeo";
    private static final Pattern SORTABLE_COLUMN = Pattern.compile(
        "^(paternalSurname|maternalSurname|names|legalDocumentNumber|dateOfBirth|ubigeo)$"
    );

    private final BeneficiaryProjection beneficiaryProjection;

    public BeneficiaryQueryService(BeneficiaryProjection beneficiaryProjection) {
        this.beneficiaryProjection = beneficiaryProjection;
    }

    /**
     * @throws lat.soulware.beneficiarios.registry.domain.model.exceptions.UnknownLegalDocumentTypeException
     *         if the document type names no type the registry knows
     * @throws UnsortableColumnException     if the sort names no sortable column
     * @throws InvalidSortDirectionException if the direction is neither asc nor desc
     * @throws InvalidPageException          if the page is negative or the size out of range
     */
    public BeneficiariesPageResult page(BeneficiariesPage criteria) {
        if (criteria.legalDocumentType() != null) {
            LegalDocumentType.named(criteria.legalDocumentType());
        }
        boolean isSortable = criteria.sort() != null && SORTABLE_COLUMN.matcher(criteria.sort()).matches();
        if (!isSortable) {
            throw new UnsortableColumnException(criteria.sort(), SORTABLE);
        }
        boolean isADirection = "asc".equalsIgnoreCase(criteria.direction())
            || "desc".equalsIgnoreCase(criteria.direction());
        if (!isADirection) {
            throw new InvalidSortDirectionException(criteria.direction());
        }
        boolean isAPage = criteria.page() >= 0 && criteria.size() >= 1 && criteria.size() <= MAX_PAGE_SIZE;
        if (!isAPage) {
            throw new InvalidPageException(criteria.page(), criteria.size(), MAX_PAGE_SIZE);
        }

        return this.beneficiaryProjection.findPage(criteria);
    }

    /**
     * Resolves up to {@value #MAX_LOOKUP_SIZE} legal documents at once. A document nobody in the
     * registry holds comes back under {@code notFound} instead of failing the read.
     *
     * @throws TooManyBeneficiariesRequestedException if the lookup names more documents than that
     * @throws lat.soulware.beneficiarios.registry.domain.model.exceptions.UnknownLegalDocumentTypeException
     *         if a document's type names no type the registry knows
     * @throws lat.soulware.beneficiarios.registry.domain.model.exceptions.InvalidLegalDocumentNumberException
     *         if a document's number breaks the rules of its type
     */
    public BeneficiaryLookupResult lookup(BeneficiariesByDocument criteria) {
        if (criteria.documents().size() > MAX_LOOKUP_SIZE) {
            throw new TooManyBeneficiariesRequestedException(MAX_LOOKUP_SIZE, criteria.documents().size());
        }
        Set<LegalDocument> requested = new LinkedHashSet<>();
        criteria.documents().forEach(document -> requested.add(BeneficiaryQueryService.toLegalDocument(document)));
        List<BeneficiaryByDocument> distinct = requested.stream()
            .map(document -> new BeneficiaryByDocument(document.type().name(), document.number()))
            .toList();

        List<BeneficiaryResult> found = this.beneficiaryProjection.findByDocuments(new BeneficiariesByDocument(distinct));
        Set<LegalDocumentResult> foundDocuments = new LinkedHashSet<>();
        found.forEach(beneficiary -> foundDocuments.add(beneficiary.legalDocument()));
        List<LegalDocumentResult> notFound = distinct.stream()
            .map(document -> new LegalDocumentResult(document.type(), document.number()))
            .filter(document -> !foundDocuments.contains(document))
            .toList();

        return new BeneficiaryLookupResult(found, notFound);
    }

    private static LegalDocument toLegalDocument(BeneficiaryByDocument document) {
        return new LegalDocument(LegalDocumentType.named(document.type()), document.number());
    }
}
