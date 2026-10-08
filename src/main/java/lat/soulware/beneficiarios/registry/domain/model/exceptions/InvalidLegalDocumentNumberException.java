package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import lat.soulware.beneficiarios.shared.domain.model.exceptions.BusinessRuleViolationException;

/** Thrown when a legal document's number breaks the rules of its type. */
public final class InvalidLegalDocumentNumberException extends BusinessRuleViolationException {

    /**
     * @param type   the document's type, by name
     * @param number the number as given
     */
    public InvalidLegalDocumentNumberException(String type, String number) {
        super(type, number);
    }
}
