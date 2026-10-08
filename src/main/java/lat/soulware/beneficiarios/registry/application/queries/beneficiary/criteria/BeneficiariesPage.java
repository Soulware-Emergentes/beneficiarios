package lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria;

import lat.soulware.beneficiarios.shared.application.queries.criteria.QueryCriteria;

/**
 * One page of beneficiaries, optionally narrowed. Every filter left null matches every beneficiary.
 *
 * @param names               text the given names contain, ignoring case
 * @param paternalSurname     text the paternal surname contains, ignoring case
 * @param maternalSurname     text the maternal surname contains, ignoring case
 * @param legalDocumentType   the type of legal document, by name
 * @param legalDocumentNumber text the document number contains
 * @param sort                the column to order by: {@code paternalSurname}, {@code maternalSurname},
 *                            {@code names}, {@code legalDocumentNumber}, {@code dateOfBirth} or
 *                            {@code ubigeo}
 * @param direction           {@code asc} or {@code desc}
 * @param page                the page, counted from zero
 * @param size                how many beneficiaries a page holds
 */
public record BeneficiariesPage(
    String names,
    String paternalSurname,
    String maternalSurname,
    String legalDocumentType,
    String legalDocumentNumber,
    String sort,
    String direction,
    int page,
    int size
) implements QueryCriteria {
}
