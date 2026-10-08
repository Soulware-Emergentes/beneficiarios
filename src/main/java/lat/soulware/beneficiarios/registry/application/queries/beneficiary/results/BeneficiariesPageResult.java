package lat.soulware.beneficiarios.registry.application.queries.beneficiary.results;

import java.util.List;

import lat.soulware.beneficiarios.shared.application.queries.results.QueryResult;

/**
 * One page of beneficiaries.
 *
 * @param content       the beneficiaries on the page, in the order asked for
 * @param totalPages    how many pages the whole selection fills
 * @param totalElements how many beneficiaries the whole selection holds
 * @param actualPage    the page, counted from zero
 * @param pageSize      how many beneficiaries a page holds
 */
public record BeneficiariesPageResult(
    List<BeneficiaryResult> content,
    int totalPages,
    long totalElements,
    int actualPage,
    int pageSize
) implements QueryResult {

    public BeneficiariesPageResult {
        content = List.copyOf(content);
    }
}
