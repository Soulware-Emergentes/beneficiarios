package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary's names or either surname is missing or blank. */
public final class BlankPersonNameException extends BusinessRuleViolationException {

    public BlankPersonNameException() {
        super();
    }
}
