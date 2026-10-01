package dev.soulware.beneficiarios.domain.model.valueobjects;

import java.util.regex.Pattern;

public enum LegalDocumentType {
    DNI(Pattern.compile("^\\d{8}$")),
    FOREIGNER_ID_CARD(Pattern.compile("^[A-Za-z0-9]{9,12}$")),
    PASSPORT(Pattern.compile("^[A-Za-z0-9]{6,12}$"));

    /** Length of the longest number any type accepts. */
    public static final int MAX_NUMBER_LENGTH = 12;

    private final Pattern numberPattern;

    LegalDocumentType(Pattern numberPattern) {
        this.numberPattern = numberPattern;
    }

    public boolean matches(String number) {
        return number != null && numberPattern.matcher(number).matches();
    }
}
