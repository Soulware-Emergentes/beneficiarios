package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import java.time.LocalDate;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingDateOfBirthException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.ValueObject;

/**
 * The day a beneficiary was born, as their legal document states it.
 *
 * @param value the day, never null
 */
public record DateOfBirth(LocalDate value) implements ValueObject {

    public DateOfBirth {
        if (value == null) {
            throw new MissingDateOfBirthException();
        }
    }
}
