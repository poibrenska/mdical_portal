package com.medical.portal.service.criteria;

import com.medical.portal.domain.enumeration.DocumentType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.medical.portal.domain.Documents} entity. This class is used
 * in {@link com.medical.portal.web.rest.DocumentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {

        public DocumentTypeFilter() {}

        public DocumentTypeFilter(DocumentTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocumentTypeFilter copy() {
            return new DocumentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileName;

    private DocumentTypeFilter docType;

    private StringFilter meta;

    private LongFilter doctorId;

    private LongFilter patientId;

    public DocumentsCriteria() {}

    public DocumentsCriteria(DocumentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.docType = other.docType == null ? null : other.docType.copy();
        this.meta = other.meta == null ? null : other.meta.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
    }

    @Override
    public DocumentsCriteria copy() {
        return new DocumentsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public DocumentTypeFilter getDocType() {
        return docType;
    }

    public DocumentTypeFilter docType() {
        if (docType == null) {
            docType = new DocumentTypeFilter();
        }
        return docType;
    }

    public void setDocType(DocumentTypeFilter docType) {
        this.docType = docType;
    }

    public StringFilter getMeta() {
        return meta;
    }

    public StringFilter meta() {
        if (meta == null) {
            meta = new StringFilter();
        }
        return meta;
    }

    public void setMeta(StringFilter meta) {
        this.meta = meta;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public LongFilter doctorId() {
        if (doctorId == null) {
            doctorId = new LongFilter();
        }
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public LongFilter patientId() {
        if (patientId == null) {
            patientId = new LongFilter();
        }
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentsCriteria that = (DocumentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(docType, that.docType) &&
            Objects.equals(meta, that.meta) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(patientId, that.patientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, docType, meta, doctorId, patientId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (docType != null ? "docType=" + docType + ", " : "") +
            (meta != null ? "meta=" + meta + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }
}
