package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Hospital entity.
 */
@Entity
@Table(name = "hospital")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "administration_phones")
    private String administrationPhones;

    @NotNull
    @Column(name = "noi", nullable = false)
    private Boolean noi;

    @Column(name = "director")
    private Long director;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_hospital__doctor",
        joinColumns = @JoinColumn(name = "hospital_id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnoreProperties(
        value = { "documents", "appointments", "examinationHistories", "patients", "hospitals", "vaccines" },
        allowSetters = true
    )
    private Set<Doctor> doctors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hospital id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Hospital name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Hospital address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Hospital city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdministrationPhones() {
        return this.administrationPhones;
    }

    public Hospital administrationPhones(String administrationPhones) {
        this.administrationPhones = administrationPhones;
        return this;
    }

    public void setAdministrationPhones(String administrationPhones) {
        this.administrationPhones = administrationPhones;
    }

    public Boolean getNoi() {
        return this.noi;
    }

    public Hospital noi(Boolean noi) {
        this.noi = noi;
        return this;
    }

    public void setNoi(Boolean noi) {
        this.noi = noi;
    }

    public Long getDirector() {
        return this.director;
    }

    public Hospital director(Long director) {
        this.director = director;
        return this;
    }

    public void setDirector(Long director) {
        this.director = director;
    }

    public Set<Doctor> getDoctors() {
        return this.doctors;
    }

    public Hospital doctors(Set<Doctor> doctors) {
        this.setDoctors(doctors);
        return this;
    }

    public Hospital addDoctor(Doctor doctor) {
        this.doctors.add(doctor);
        doctor.getHospitals().add(this);
        return this;
    }

    public Hospital removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
        doctor.getHospitals().remove(this);
        return this;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospital)) {
            return false;
        }
        return id != null && id.equals(((Hospital) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hospital{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", administrationPhones='" + getAdministrationPhones() + "'" +
            ", noi='" + getNoi() + "'" +
            ", director=" + getDirector() +
            "}";
    }
}
