package com.medical.portal.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.medical.portal.domain.Vaccine} entity. This class is used
 * in {@link com.medical.portal.web.rest.VaccineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vaccines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VaccineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private IntegerFilter dose;

    private InstantFilter nextDoseDate;

    private IntegerFilter dosesLeft;

    private LongFilter patientId;

    private LongFilter doctorId;

    public VaccineCriteria() {}

    public VaccineCriteria(VaccineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.dose = other.dose == null ? null : other.dose.copy();
        this.nextDoseDate = other.nextDoseDate == null ? null : other.nextDoseDate.copy();
        this.dosesLeft = other.dosesLeft == null ? null : other.dosesLeft.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
    }

    @Override
    public VaccineCriteria copy() {
        return new VaccineCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public IntegerFilter getDose() {
        return dose;
    }

    public IntegerFilter dose() {
        if (dose == null) {
            dose = new IntegerFilter();
        }
        return dose;
    }

    public void setDose(IntegerFilter dose) {
        this.dose = dose;
    }

    public InstantFilter getNextDoseDate() {
        return nextDoseDate;
    }

    public InstantFilter nextDoseDate() {
        if (nextDoseDate == null) {
            nextDoseDate = new InstantFilter();
        }
        return nextDoseDate;
    }

    public void setNextDoseDate(InstantFilter nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
    }

    public IntegerFilter getDosesLeft() {
        return dosesLeft;
    }

    public IntegerFilter dosesLeft() {
        if (dosesLeft == null) {
            dosesLeft = new IntegerFilter();
        }
        return dosesLeft;
    }

    public void setDosesLeft(IntegerFilter dosesLeft) {
        this.dosesLeft = dosesLeft;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VaccineCriteria that = (VaccineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dose, that.dose) &&
            Objects.equals(nextDoseDate, that.nextDoseDate) &&
            Objects.equals(dosesLeft, that.dosesLeft) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(doctorId, that.doctorId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, dose, nextDoseDate, dosesLeft, patientId, doctorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (dose != null ? "dose=" + dose + ", " : "") +
            (nextDoseDate != null ? "nextDoseDate=" + nextDoseDate + ", " : "") +
            (dosesLeft != null ? "dosesLeft=" + dosesLeft + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            "}";
    }
}
