package lat.soulware.beneficiarios.registry.interfaces.rest.beneficiary.responses;

import java.util.List;

/**
 * One page of beneficiaries.
 *
 * @param content       the beneficiaries on the page, in the order asked for
 * @param totalPages    how many pages the whole selection fills
 * @param totalElements how many beneficiaries the whole selection holds
 * @param actualPage    the page, counted from zero
 * @param pageSize      how many beneficiaries a page holds
 */
public record BeneficiariesPageResponse(
    List<BeneficiaryResponse> content,
    int totalPages,
    long totalElements,
    int actualPage,
    int pageSize
) {
}
