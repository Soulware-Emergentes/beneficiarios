package lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import lat.soulware.beneficiarios.registry.domain.model.aggregates.Beneficiary;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.BeneficiaryNotFoundException;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.BeneficiaryId;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.LegalDocument;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.LegalDocumentType;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.PersonName;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.Ubigeo;
import lat.soulware.beneficiarios.registry.domain.repositories.BeneficiaryRepository;
import lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary.jpa.BeneficiaryEntity;
import lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary.jpa.BeneficiaryJpaRepository;
import lat.soulware.beneficiarios.shared.domain.model.exceptions.EntityNotFoundException;
import lat.soulware.beneficiarios.shared.infrastructure.persistence.adapter.JpaRepositoryToDomainRepositoryAdapter;

/** Stores beneficiaries in {@code beneficiaries}. */
@Repository
public class JpaBeneficiaryRepositoryAdapter
    extends JpaRepositoryToDomainRepositoryAdapter<Beneficiary, BeneficiaryId, BeneficiaryEntity, UUID>
    implements BeneficiaryRepository {

    private final BeneficiaryJpaRepository delegate;
    private final ApplicationEventPublisher publisher;

    public JpaBeneficiaryRepositoryAdapter(BeneficiaryJpaRepository delegate, ApplicationEventPublisher publisher) {
        this.delegate = delegate;
        this.publisher = publisher;
    }

    @Override
    protected BeneficiaryJpaRepository delegate() {
        return this.delegate;
    }

    @Override
    protected ApplicationEventPublisher publisher() {
        return this.publisher;
    }

    @Override
    protected BeneficiaryEntity toEntity(Beneficiary aggregate) {
        return new BeneficiaryEntity(
            aggregate.getId().value(),
            aggregate.getLegalDocument().type().name(),
            aggregate.getLegalDocument().number(),
            aggregate.getName().names(),
            aggregate.getName().paternalSurname(),
            aggregate.getName().maternalSurname(),
            aggregate.getDateOfBirth(),
            aggregate.getResidence().value()
        );
    }

    @Override
    protected Beneficiary toAggregate(BeneficiaryEntity entity) {
        return Beneficiary.reconstitute(
            new BeneficiaryId(entity.getId()),
            new LegalDocument(LegalDocumentType.named(entity.getLegalDocumentType()), entity.getLegalDocumentNumber()),
            new PersonName(entity.getNames(), entity.getPaternalSurname(), entity.getMaternalSurname()),
            entity.getDateOfBirth(),
            new Ubigeo(entity.getUbigeo())
        );
    }

    @Override
    protected UUID keyOf(BeneficiaryEntity entity) {
        return entity.getId();
    }

    @Override
    protected EntityNotFoundException notFound(Collection<BeneficiaryId> missing) {
        return new BeneficiaryNotFoundException(missing);
    }
}
