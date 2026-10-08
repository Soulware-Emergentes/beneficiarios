package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import java.util.Arrays;
import java.util.regex.Pattern;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.UnknownLegalDocumentTypeException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.ValueObject;

/** The kinds of legal document a beneficiary is identified by, each with the numbers it accepts. */
public enum LegalDocumentType implements ValueObject {
    DNI(Pattern.compile("^\\d{8}$")),
    FOREIGNER_ID_CARD(Pattern.compile("^[A-Za-z0-9]{9,12}$")),
    PASSPORT(Pattern.compile("^[A-Za-z0-9]{6,12}$"));

    private final Pattern numberPattern;

    LegalDocumentType(Pattern numberPattern) {
        this.numberPattern = numberPattern;
    }

    /**
     * @param number a document number
     * @return whether a document of this type can carry it
     */
    public boolean accepts(String number) {
        return number != null && this.numberPattern.matcher(number).matches();
    }

    /**
     * @param name a type as written, such as {@code DNI}
     * @return the type it names
     * @throws UnknownLegalDocumentTypeException if it names none
     */
    public static LegalDocumentType named(String name) {
        return Arrays.stream(LegalDocumentType.values())
            .filter(type -> type.name().equals(name))
            .findFirst()
            .orElseThrow(() -> new UnknownLegalDocumentTypeException(name));
    }
}
