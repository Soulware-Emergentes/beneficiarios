package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import java.util.regex.Pattern;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.InvalidUbigeoException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingUbigeoException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.ValueObject;

/**
 * A district of Peru by its ubigeo: two digits each for the department, the province and the
 * district, such as {@code 150101}.
 *
 * @param value the six digits
 */
public record Ubigeo(String value) implements ValueObject {

    private static final Pattern FORMAT = Pattern.compile("^\\d{6}$");

    public Ubigeo {
        if (value == null) {
            throw new MissingUbigeoException();
        }
        if (!FORMAT.matcher(value).matches()) {
            throw new InvalidUbigeoException(value);
        }
    }
}
