package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Vaccine entity.
 */
@Entity
@Table(name = "vaccine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vaccine extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "dose")
    private Integer dose;

    @Column(name = "next_dose_date")
    private Instant nextDoseDate;

    @Column(name = "doses_left")
    private Integer dosesLeft;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "documents", "appointments", "examinationHistories", "doctors", "vaccines" },
        allowSetters = true
    )
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "documents", "appointments", "examinationHistories", "patients", "hospitals", "vaccines" },
        allowSetters = true
    )
    private Doctor doctor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vaccine id(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Vaccine type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDose() {
        return this.dose;
    }

    public Vaccine dose(Integer dose) {
        this.dose = dose;
        return this;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public Instant getNextDoseDate() {
        return this.nextDoseDate;
    }

    public Vaccine nextDoseDate(Instant nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
        return this;
    }

    public void setNextDoseDate(Instant nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
    }

    public Integer getDosesLeft() {
        return this.dosesLeft;
    }

    public Vaccine dosesLeft(Integer dosesLeft) {
        this.dosesLeft = dosesLeft;
        return this;
    }

    public void setDosesLeft(Integer dosesLeft) {
        this.dosesLeft = dosesLeft;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Vaccine patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
//        if (this.patient != null) {
//            this.patient.removeVaccine(this);
//        }
//        if (patient != null) {
//            patient.addVaccine(this);
//        }
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public Vaccine doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public void setDoctor(Doctor doctor) {
//        if (this.doctor != null) {
//            this.doctor.removeVaccine(this);
//        }
//        if (doctor != null) {
//            doctor.addVaccine(this);
//        }
        this.doctor = doctor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaccine)) {
            return false;
        }
        return id != null && id.equals(((Vaccine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaccine{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dose=" + getDose() +
            ", nextDoseDate='" + getNextDoseDate() + "'" +
            ", dosesLeft=" + getDosesLeft() +
            "}";
    }
}
