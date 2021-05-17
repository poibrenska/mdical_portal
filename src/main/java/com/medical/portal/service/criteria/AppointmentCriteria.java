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
 * Criteria class for the {@link com.medical.portal.domain.Appointment} entity. This class is used
 * in {@link com.medical.portal.web.rest.AppointmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appointments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppointmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter appointmentDate;

    private BooleanFilter active;

    private BooleanFilter finished;

    private LongFilter doctorId;

    private LongFilter patientId;

    public AppointmentCriteria() {}

    public AppointmentCriteria(AppointmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.appointmentDate = other.appointmentDate == null ? null : other.appointmentDate.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.finished = other.finished == null ? null : other.finished.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
    }

    @Override
    public AppointmentCriteria copy() {
        return new AppointmentCriteria(this);
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

    public InstantFilter getAppointmentDate() {
        return appointmentDate;
    }

    public InstantFilter appointmentDate() {
        if (appointmentDate == null) {
            appointmentDate = new InstantFilter();
        }
        return appointmentDate;
    }

    public void setAppointmentDate(InstantFilter appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public BooleanFilter getFinished() {
        return finished;
    }

    public BooleanFilter finished() {
        if (finished == null) {
            finished = new BooleanFilter();
        }
        return finished;
    }

    public void setFinished(BooleanFilter finished) {
        this.finished = finished;
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
        final AppointmentCriteria that = (AppointmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(appointmentDate, that.appointmentDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(finished, that.finished) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(patientId, that.patientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appointmentDate, active, finished, doctorId, patientId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (appointmentDate != null ? "appointmentDate=" + appointmentDate + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (finished != null ? "finished=" + finished + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }
}
