package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary is built without their name. */
public final class MissingPersonNameException extends BusinessRuleViolationException {

    public MissingPersonNameException() {
        super();
    }
}
