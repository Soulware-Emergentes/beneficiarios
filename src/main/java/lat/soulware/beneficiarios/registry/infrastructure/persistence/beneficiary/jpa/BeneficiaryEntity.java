package lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary.jpa;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** The row storing a beneficiary. */
@Entity
@Table(name = "beneficiaries")
public class BeneficiaryEntity {

    @Id
    private UUID id;

    @Column(name = "legal_document_type", nullable = false)
    private String legalDocumentType;

    @Column(name = "legal_document", nullable = false)
    private String legalDocumentNumber;

    @Column(nullable = false)
    private String names;

    @Column(name = "paternal_surname", nullable = false)
    private String paternalSurname;

    @Column(name = "maternal_surname", nullable = false)
    private String maternalSurname;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String ubigeo;

    /** For JPA. */
    protected BeneficiaryEntity() {
    }

    public BeneficiaryEntity(
        UUID id,
        String legalDocumentType,
        String legalDocumentNumber,
        String names,
        String paternalSurname,
        String maternalSurname,
        LocalDate dateOfBirth,
        String ubigeo
    ) {
        this.id = id;
        this.legalDocumentType = legalDocumentType;
        this.legalDocumentNumber = legalDocumentNumber;
        this.names = names;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.dateOfBirth = dateOfBirth;
        this.ubigeo = ubigeo;
    }

    public UUID getId() {
        return this.id;
    }

    public String getLegalDocumentType() {
        return this.legalDocumentType;
    }

    public String getLegalDocumentNumber() {
        return this.legalDocumentNumber;
    }

    public String getNames() {
        return this.names;
    }

    public String getPaternalSurname() {
        return this.paternalSurname;
    }

    public String getMaternalSurname() {
        return this.maternalSurname;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getUbigeo() {
        return this.ubigeo;
    }
}
