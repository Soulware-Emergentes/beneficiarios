package dev.soulware.beneficiarios.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @Column(name = "dni", length = 8, nullable = false, unique = true)
    private String dni;

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

    public Beneficiary(String dni, String names, String paternalSurname, String maternalSurname, LocalDate dateOfBirth, String ubigeo) {
        this.dni = dni;
        this.names = names;
        this.paternalSurname = paternalSurname;
        this.maternalSurname = maternalSurname;
        this.dateOfBirth = dateOfBirth;
        this.ubigeo = ubigeo;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

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