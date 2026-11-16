package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.InvalidLegalDocumentNumberException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingLegalDocumentTypeException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.UnknownLegalDocumentTypeException;

/** Covers the numbers each type of legal document carries, and the types the registry knows. */
class LegalDocumentTest {

    @ParameterizedTest
    @CsvSource({"DNI, 12345678", "FOREIGNER_ID_CARD, 8DBMJ7XACK", "PASSPORT, AB1234"})
    void eachTypeCarriesItsOwnNumbers(String type, String number) {
        assertDoesNotThrow(() -> new LegalDocument(LegalDocumentType.named(type), number));
    }

    @ParameterizedTest
    @CsvSource({"DNI, 1234567", "DNI, 1234567A", "FOREIGNER_ID_CARD, 12345678", "PASSPORT, AB12", "PASSPORT, AB-1234"})
    void aNumberBreakingItsTypeIsRefused(String type, String number) {
        assertThrows(
            InvalidLegalDocumentNumberException.class,
            () -> new LegalDocument(LegalDocumentType.named(type), number)
        );
    }

    @Test
    void aDocumentNeedsAType() {
        assertThrows(MissingLegalDocumentTypeException.class, () -> new LegalDocument(null, "12345678"));
    }

    @Test
    void aTypeIsNamedExactly() {
        assertEquals(LegalDocumentType.DNI, LegalDocumentType.named("DNI"));
        assertThrows(UnknownLegalDocumentTypeException.class, () -> LegalDocumentType.named("dni"));
    }
}
