package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Appointments entity.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Appointment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date")
    private Instant appointmentDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "finished")
    private Boolean finished;

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

    public Appointment id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getAppointmentDate() {
        return this.appointmentDate;
    }

    public Appointment appointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public void setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Appointment active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFinished() {
        return this.finished;
    }

    public Appointment finished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public Appointment doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Appointment patient(Patient patient) {
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
        if (!(o instanceof Appointment)) {
            return false;
        }
        return id != null && id.equals(((Appointment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", active='" + getActive() + "'" +
            ", finished='" + getFinished() + "'" +
            "}";
    }
}
