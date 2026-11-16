package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.BlankPersonNameException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.ValueObject;

/**
 * A beneficiary's name as their legal document states it.
 *
 * @param names           their given names
 * @param paternalSurname their father's surname
 * @param maternalSurname their mother's surname
 */
public record PersonName(String names, String paternalSurname, String maternalSurname) implements ValueObject {

    public PersonName {
        boolean isComplete = PersonName.isPresent(names)
            && PersonName.isPresent(paternalSurname)
            && PersonName.isPresent(maternalSurname);
        if (!isComplete) {
            throw new BlankPersonNameException();
        }
    }

    private static boolean isPresent(String part) {
        return part != null && !part.isBlank();
    }
}
