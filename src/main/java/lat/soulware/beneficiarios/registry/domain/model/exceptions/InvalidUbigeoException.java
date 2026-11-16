package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a ubigeo is not six digits. */
public final class InvalidUbigeoException extends BusinessRuleViolationException {

    /**
     * @param ubigeo the code as given
     */
    public InvalidUbigeoException(String ubigeo) {
        super(ubigeo);
    }
}
