package lat.soulware.beneficiarios.registry.domain.model.valueobjects;

import java.util.UUID;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingBeneficiaryIdException;
import lat.soulware.beneficiarios.shared.domain.model.valueobjects.AggregateId;

/**
 * Identity of a {@link lat.soulware.beneficiarios.registry.domain.model.aggregates.Beneficiary}.
 *
 * @param value the underlying key, never null
 */
public record BeneficiaryId(UUID value) implements AggregateId<UUID> {

    public BeneficiaryId {
        if (value == null) {
            throw new MissingBeneficiaryIdException();
        }
    }
}
