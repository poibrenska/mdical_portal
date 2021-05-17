package com.medical.portal.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.medical.portal.domain.ExaminationHistory} entity.
 */
@ApiModel(description = "The Examination entity.")
public class ExaminationHistoryDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String documents;

    private String notes;

    private DoctorDTO doctor;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(o instanceof ExaminationHistoryDTO)) {
            return false;
        }

        ExaminationHistoryDTO examinationHistoryDTO = (ExaminationHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examinationHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationHistoryDTO{" +
            "id=" + getId() +
            ", documents='" + getDocuments() + "'" +
            ", notes='" + getNotes() + "'" +
            ", doctor=" + getDoctor() +
            ", patient=" + getPatient() +
            "}";
    }
}
