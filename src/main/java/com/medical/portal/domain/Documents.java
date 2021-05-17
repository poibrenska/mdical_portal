package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medical.portal.domain.enumeration.DocumentType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Documents entity.
 */
@Entity
@Table(name = "documents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Documents extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @Lob
    @Column(name = "stream_data", nullable = false)
    private byte[] streamData;

    @Column(name = "stream_data_content_type", nullable = false)
    private String streamDataContentType;

    @Column(name = "meta")
    private String meta;

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

    public Documents id(Long id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Documents fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocumentType getDocType() {
        return this.docType;
    }

    public Documents docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public byte[] getStreamData() {
        return this.streamData;
    }

    public Documents streamData(byte[] streamData) {
        this.streamData = streamData;
        return this;
    }

    public void setStreamData(byte[] streamData) {
        this.streamData = streamData;
    }

    public String getStreamDataContentType() {
        return this.streamDataContentType;
    }

    public Documents streamDataContentType(String streamDataContentType) {
        this.streamDataContentType = streamDataContentType;
        return this;
    }

    public void setStreamDataContentType(String streamDataContentType) {
        this.streamDataContentType = streamDataContentType;
    }

    public String getMeta() {
        return this.meta;
    }

    public Documents meta(String meta) {
        this.meta = meta;
        return this;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public Documents doctor(Doctor doctor) {
        this.setDoctor(doctor);
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Documents patient(Patient patient) {
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
        if (!(o instanceof Documents)) {
            return false;
        }
        return id != null && id.equals(((Documents) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documents{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", docType='" + getDocType() + "'" +
            ", streamData='" + getStreamData() + "'" +
            ", streamDataContentType='" + getStreamDataContentType() + "'" +
            ", meta='" + getMeta() + "'" +
            "}";
    }
}
