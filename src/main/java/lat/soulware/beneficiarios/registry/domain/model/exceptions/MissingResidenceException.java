package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary is built without the ubigeo they live in. */
public final class MissingResidenceException extends BusinessRuleViolationException {

    public MissingResidenceException() {
        super();
    }
}
