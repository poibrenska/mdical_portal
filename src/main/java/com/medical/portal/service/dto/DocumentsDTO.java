package com.medical.portal.service.dto;

import com.medical.portal.domain.enumeration.DocumentType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medical.portal.domain.Documents} entity.
 */
@ApiModel(description = "The Documents entity.")
public class DocumentsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String fileName;

    @NotNull
    private DocumentType docType;

    @Lob
    private byte[] streamData;

    private String streamDataContentType;
    private String meta;

    private DoctorDTO doctor;

    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public byte[] getStreamData() {
        return streamData;
    }

    public void setStreamData(byte[] streamData) {
        this.streamData = streamData;
    }

    public String getStreamDataContentType() {
        return streamDataContentType;
    }

    public void setStreamDataContentType(String streamDataContentType) {
        this.streamDataContentType = streamDataContentType;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
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
        if (!(o instanceof DocumentsDTO)) {
            return false;
        }

        DocumentsDTO documentsDTO = (DocumentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentsDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", docType='" + getDocType() + "'" +
            ", streamData='" + getStreamData() + "'" +
            ", meta='" + getMeta() + "'" +
            ", doctor=" + getDoctor() +
            ", patient=" + getPatient() +
            "}";
    }
}
