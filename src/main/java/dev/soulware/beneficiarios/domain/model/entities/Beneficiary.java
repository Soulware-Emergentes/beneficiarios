package dev.soulware.beneficiarios.domain.model.entities;

import dev.soulware.beneficiarios.domain.model.valueobjects.LegalDocument;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "beneficiaries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"legal_document_type", "legal_document"})
)
public class Beneficiary {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    private LegalDocument legalDocument;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "paternal_surname", nullable = false)
    private String paternalSurname;

    @Column(name = "maternal_surname", nullable = false)
    private String maternalSurname;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "ubigeo", length = 6, nullable = false)
    private String ubigeo;

    public Beneficiary() {}

    public Beneficiary(LegalDocument legalDocument, String names, String paternalSurname, String maternalSurname, LocalDate dateOfBirth, String ubigeo) {
        this.id = UUID.randomUUID();
        this.legalDocument = legalDocument;
        this.names = names;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.dateOfBirth = dateOfBirth;
        this.ubigeo = ubigeo;
    }

    public UUID getId() { return id; }

    public LegalDocument getLegalDocument() { return legalDocument; }
    public void setLegalDocument(LegalDocument legalDocument) { this.legalDocument = legalDocument; }

    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }

    public String getPaternalSurname() { return paternalSurname; }
    public void setPaternalSurname(String paternalSurname) { this.paternalSurname = paternalSurname; }

    public String getMaternalSurname() { return maternalSurname; }
    public void setMaternalSurname(String maternalSurname) { this.maternalSurname = maternalSurname; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }
}
