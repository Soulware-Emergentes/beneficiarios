package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a beneficiary is built without the legal document identifying them. */
public final class MissingLegalDocumentException extends BusinessRuleViolationException {

    public MissingLegalDocumentException() {
        super();
    }
}
