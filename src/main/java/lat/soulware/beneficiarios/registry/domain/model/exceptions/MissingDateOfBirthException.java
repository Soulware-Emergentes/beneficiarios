package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary is built without their date of birth. */
public final class MissingDateOfBirthException extends BusinessRuleViolationException {

    public MissingDateOfBirthException() {
        super();
    }
}
