package com.medical.portal.service.criteria;

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
 * Criteria class for the {@link com.medical.portal.domain.ExaminationHistory} entity. This class is used
 * in {@link com.medical.portal.web.rest.ExaminationHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /examination-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExaminationHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documents;

    private StringFilter notes;

    private LongFilter doctorId;

    private LongFilter patientId;

    public ExaminationHistoryCriteria() {}

    public ExaminationHistoryCriteria(ExaminationHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.documents = other.documents == null ? null : other.documents.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
    }

    @Override
    public ExaminationHistoryCriteria copy() {
        return new ExaminationHistoryCriteria(this);
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

    public StringFilter getDocuments() {
        return documents;
    }

    public StringFilter documents() {
        if (documents == null) {
            documents = new StringFilter();
        }
        return documents;
    }

    public void setDocuments(StringFilter documents) {
        this.documents = documents;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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
        final ExaminationHistoryCriteria that = (ExaminationHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documents, that.documents) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(patientId, that.patientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documents, notes, doctorId, patientId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExaminationHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (documents != null ? "documents=" + documents + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }
}
