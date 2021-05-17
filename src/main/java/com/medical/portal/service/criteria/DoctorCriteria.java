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
 * Criteria class for the {@link com.medical.portal.domain.Doctor} entity. This class is used
 * in {@link com.medical.portal.web.rest.DoctorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doctors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DoctorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter specialization;

    private StringFilter additionalInfo;

    private StringFilter meta;

    private LongFilter documentsId;

    private LongFilter appointmentId;

    private LongFilter examinationHistoryId;

    private LongFilter patientId;

    private LongFilter hospitalId;

    private LongFilter vaccineId;

    public DoctorCriteria() {}

    public DoctorCriteria(DoctorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.specialization = other.specialization == null ? null : other.specialization.copy();
        this.additionalInfo = other.additionalInfo == null ? null : other.additionalInfo.copy();
        this.meta = other.meta == null ? null : other.meta.copy();
        this.documentsId = other.documentsId == null ? null : other.documentsId.copy();
        this.appointmentId = other.appointmentId == null ? null : other.appointmentId.copy();
        this.examinationHistoryId = other.examinationHistoryId == null ? null : other.examinationHistoryId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.hospitalId = other.hospitalId == null ? null : other.hospitalId.copy();
        this.vaccineId = other.vaccineId == null ? null : other.vaccineId.copy();
    }

    @Override
    public DoctorCriteria copy() {
        return new DoctorCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getSpecialization() {
        return specialization;
    }

    public StringFilter specialization() {
        if (specialization == null) {
            specialization = new StringFilter();
        }
        return specialization;
    }

    public void setSpecialization(StringFilter specialization) {
        this.specialization = specialization;
    }

    public StringFilter getAdditionalInfo() {
        return additionalInfo;
    }

    public StringFilter additionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new StringFilter();
        }
        return additionalInfo;
    }

    public void setAdditionalInfo(StringFilter additionalInfo) {
        this.additionalInfo = additionalInfo;
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

    public LongFilter getDocumentsId() {
        return documentsId;
    }

    public LongFilter documentsId() {
        if (documentsId == null) {
            documentsId = new LongFilter();
        }
        return documentsId;
    }

    public void setDocumentsId(LongFilter documentsId) {
        this.documentsId = documentsId;
    }

    public LongFilter getAppointmentId() {
        return appointmentId;
    }

    public LongFilter appointmentId() {
        if (appointmentId == null) {
            appointmentId = new LongFilter();
        }
        return appointmentId;
    }

    public void setAppointmentId(LongFilter appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LongFilter getExaminationHistoryId() {
        return examinationHistoryId;
    }

    public LongFilter examinationHistoryId() {
        if (examinationHistoryId == null) {
            examinationHistoryId = new LongFilter();
        }
        return examinationHistoryId;
    }

    public void setExaminationHistoryId(LongFilter examinationHistoryId) {
        this.examinationHistoryId = examinationHistoryId;
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

    public LongFilter getHospitalId() {
        return hospitalId;
    }

    public LongFilter hospitalId() {
        if (hospitalId == null) {
            hospitalId = new LongFilter();
        }
        return hospitalId;
    }

    public void setHospitalId(LongFilter hospitalId) {
        this.hospitalId = hospitalId;
    }

    public LongFilter getVaccineId() {
        return vaccineId;
    }

    public LongFilter vaccineId() {
        if (vaccineId == null) {
            vaccineId = new LongFilter();
        }
        return vaccineId;
    }

    public void setVaccineId(LongFilter vaccineId) {
        this.vaccineId = vaccineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoctorCriteria that = (DoctorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(specialization, that.specialization) &&
            Objects.equals(additionalInfo, that.additionalInfo) &&
            Objects.equals(meta, that.meta) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(appointmentId, that.appointmentId) &&
            Objects.equals(examinationHistoryId, that.examinationHistoryId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(hospitalId, that.hospitalId) &&
            Objects.equals(vaccineId, that.vaccineId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            email,
            phone,
            specialization,
            additionalInfo,
            meta,
            documentsId,
            appointmentId,
            examinationHistoryId,
            patientId,
            hospitalId,
            vaccineId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (specialization != null ? "specialization=" + specialization + ", " : "") +
            (additionalInfo != null ? "additionalInfo=" + additionalInfo + ", " : "") +
            (meta != null ? "meta=" + meta + ", " : "") +
            (documentsId != null ? "documentsId=" + documentsId + ", " : "") +
            (appointmentId != null ? "appointmentId=" + appointmentId + ", " : "") +
            (examinationHistoryId != null ? "examinationHistoryId=" + examinationHistoryId + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (hospitalId != null ? "hospitalId=" + hospitalId + ", " : "") +
            (vaccineId != null ? "vaccineId=" + vaccineId + ", " : "") +
            "}";
    }
}
