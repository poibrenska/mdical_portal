package com.medical.portal.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medical.portal.domain.Appointment} entity.
 */
@ApiModel(description = "The Appointments entity.")
public class AppointmentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Instant appointmentDate;

    private Boolean active;

    private Boolean finished;

    private DoctorDTO doctor;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Instant appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDTO)) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", active='" + getActive() + "'" +
            ", finished='" + getFinished() + "'" +
            ", doctor=" + getDoctor() +
            ", patient=" + getPatient() +
            "}";
    }
}
