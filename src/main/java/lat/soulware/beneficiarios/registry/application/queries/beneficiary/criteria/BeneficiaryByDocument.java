package lat.soulware.beneficiarios.registry.application.queries.beneficiary.criteria;

import lat.soulware.beneficiarios.shared.application.queries.criteria.QueryCriteria;

/**
 * One beneficiary, by the legal document identifying them.
 *
 * @param type   the kind of document, by name
 * @param number the document number
 */
public record BeneficiaryByDocument(String type, String number) implements QueryCriteria {
}
