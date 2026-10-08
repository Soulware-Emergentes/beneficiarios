package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when one lookup names more beneficiaries than the registry resolves at once. */
public final class TooManyBeneficiariesRequestedException extends BusinessRuleViolationException {

    /**
     * @param limit     how many one lookup may name
     * @param requested how many it named
     */
    public TooManyBeneficiariesRequestedException(int limit, int requested) {
        super(Integer.toString(limit), Integer.toString(requested));
    }
}
