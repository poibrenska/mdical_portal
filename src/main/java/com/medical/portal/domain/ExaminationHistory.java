package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Examination entity.
 */
@Entity
@Table(name = "examination_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExaminationHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documents")
    private String documents;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "documents", "appointments", "examinationHistories", "patients", "hospitals", "vaccines" },
        allowSetters = true
    )
    private Doctor doctor;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "user", "documents", "appointments", "examinationHistories", "doctors", "vaccines" },
        allowSetters = true
    )
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExaminationHistory id(Long id) {
        this.id = id;
        return this;
    }

    public String getDocuments() {
        return this.documents;
    }

    public ExaminationHistory documents(String documents) {
        this.documents = documents;
        return this;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getNotes() {
        return this.notes;
    }

    public ExaminationHistory notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public ExaminationHistory doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public ExaminationHistory patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExaminationHistory)) {
            return false;
        }
        return id != null && id.equals(((ExaminationHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationHistory{" +
            "id=" + getId() +
            ", documents='" + getDocuments() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
