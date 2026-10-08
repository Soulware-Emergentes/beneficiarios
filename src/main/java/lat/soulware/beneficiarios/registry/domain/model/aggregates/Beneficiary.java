package lat.soulware.beneficiarios.registry.domain.model.aggregates;

import java.time.LocalDate;

import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingDateOfBirthException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingLegalDocumentException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingPersonNameException;
import lat.soulware.beneficiarios.registry.domain.model.exceptions.MissingResidenceException;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.BeneficiaryId;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.LegalDocument;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.PersonName;
import lat.soulware.beneficiarios.registry.domain.model.valueobjects.Ubigeo;
import lat.soulware.beneficiarios.shared.domain.model.aggregates.AggregateRoot;
import lat.soulware.beneficiarios.shared.domain.model.aggregates.PackagedAs;

/**
 * A person the program serves, identified by their legal document and placed by the district they
 * live in. The registry only reads beneficiaries, so one is only ever reconstituted from storage.
 */
@PackagedAs("beneficiary")
public class Beneficiary extends AggregateRoot<BeneficiaryId> {
    private final BeneficiaryId id;
    private final LegalDocument legalDocument;
    private final PersonName name;
    private final LocalDate dateOfBirth;
    private final Ubigeo residence;

    private Beneficiary(
        BeneficiaryId id,
        LegalDocument legalDocument,
        PersonName name,
        LocalDate dateOfBirth,
        Ubigeo residence
    ) {
        if (legalDocument == null) {
            throw new MissingLegalDocumentException();
        }
        if (name == null) {
            throw new MissingPersonNameException();
        }
        if (dateOfBirth == null) {
            throw new MissingDateOfBirthException();
        }
        if (residence == null) {
            throw new MissingResidenceException();
        }
        this.id = id;
        this.legalDocument = legalDocument;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.residence = residence;
    }

    /**
     * @param id            the beneficiary's identity
     * @param legalDocument the legal document identifying them
     * @param name          their name as the document states it
     * @param dateOfBirth   when they were born
     * @param residence     the district they live in
     * @return the beneficiary as stored
     */
    public static Beneficiary reconstitute(
        BeneficiaryId id,
        LegalDocument legalDocument,
        PersonName name,
        LocalDate dateOfBirth,
        Ubigeo residence
    ) {
        return new Beneficiary(id, legalDocument, name, dateOfBirth, residence);
    }

    @Override
    public BeneficiaryId getId() {
        return this.id;
    }

    public LegalDocument getLegalDocument() {
        return this.legalDocument;
    }

    public PersonName getName() {
        return this.name;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Ubigeo getResidence() {
        return this.residence;
    }
}
