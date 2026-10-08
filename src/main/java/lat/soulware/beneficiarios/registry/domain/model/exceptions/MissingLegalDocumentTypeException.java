package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a legal document does not say what kind of document it is. */
public final class MissingLegalDocumentTypeException extends BusinessRuleViolationException {

    public MissingLegalDocumentTypeException() {
        super();
    }
}
