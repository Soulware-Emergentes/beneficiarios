package lat.soulware.beneficiarios.registry.domain.model.exceptions;

import java.util.Collection;

import lat.soulware.beneficiarios.registry.domain.model.valueobjects.BeneficiaryId;
import lat.soulware.beneficiarios.shared.domain.model.exceptions.EntityNotFoundException;

/** Thrown when no beneficiary carries the requested identity. */
public final class BeneficiaryNotFoundException extends EntityNotFoundException {

    /**
     * @param missing the identities that matched nothing
     */
    public BeneficiaryNotFoundException(Collection<BeneficiaryId> missing) {
        super(missing);
    }
}
