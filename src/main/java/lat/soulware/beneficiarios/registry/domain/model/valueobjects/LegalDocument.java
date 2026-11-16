package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.InvalidLegalDocumentNumberException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingLegalDocumentTypeException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.ValueObject;

/**
 * The legal document identifying a beneficiary. No two beneficiaries hold the same one.
 *
 * @param type   the kind of document, never null
 * @param number its number, following the rules of its type
 */
public record LegalDocument(LegalDocumentType type, String number) implements ValueObject {

    public LegalDocument {
        if (type == null) {
            throw new MissingLegalDocumentTypeException();
        }
        if (!type.accepts(number)) {
            throw new InvalidLegalDocumentNumberException(type.name(), number);
        }
    }
}
