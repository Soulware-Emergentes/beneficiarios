package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a ubigeo is built without its code. */
public final class MissingUbigeoException extends BusinessRuleViolationException {

    public MissingUbigeoException() {
        super();
    }
}
