package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a legal document type is named that the registry does not know. */
public final class UnknownLegalDocumentTypeException extends BusinessRuleViolationException {

    /**
     * @param type the type as given
     */
    public UnknownLegalDocumentTypeException(String type) {
        super(type);
    }
}
