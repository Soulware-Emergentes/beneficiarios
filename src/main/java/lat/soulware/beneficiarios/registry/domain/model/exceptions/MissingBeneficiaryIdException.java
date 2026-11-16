package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary is built without its id. */
public final class MissingBeneficiaryIdException extends BusinessRuleViolationException {

    public MissingBeneficiaryIdException() {
        super();
    }
}
