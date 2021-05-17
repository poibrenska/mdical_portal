package com.medical.portal.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.medical.portal.domain.Patient} entity. This class is used
 * in {@link com.medical.portal.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter addressText;

    private LocalDateFilter birthDate;

    private StringFilter egn;

    private StringFilter phone;

    private BooleanFilter active;

    private LongFilter gp;

    private LongFilter userId;

    private LongFilter documentsId;

    private LongFilter appointmentId;

    private LongFilter examinationHistoryId;

    private LongFilter doctorId;

    private LongFilter vaccineId;

    public PatientCriteria() {}

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.addressText = other.addressText == null ? null : other.addressText.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.egn = other.egn == null ? null : other.egn.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.gp = other.gp == null ? null : other.gp.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.documentsId = other.documentsId == null ? null : other.documentsId.copy();
        this.appointmentId = other.appointmentId == null ? null : other.appointmentId.copy();
        this.examinationHistoryId = other.examinationHistoryId == null ? null : other.examinationHistoryId.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
        this.vaccineId = other.vaccineId == null ? null : other.vaccineId.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
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

    public StringFilter getAddressText() {
        return addressText;
    }

    public StringFilter addressText() {
        if (addressText == null) {
            addressText = new StringFilter();
        }
        return addressText;
    }

    public void setAddressText(StringFilter addressText) {
        this.addressText = addressText;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            birthDate = new LocalDateFilter();
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getEgn() {
        return egn;
    }

    public StringFilter egn() {
        if (egn == null) {
            egn = new StringFilter();
        }
        return egn;
    }

    public void setEgn(StringFilter egn) {
        this.egn = egn;
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

    public LongFilter getGp() {
        return gp;
    }

    public LongFilter gp() {
        if (gp == null) {
            gp = new LongFilter();
        }
        return gp;
    }

    public void setGp(LongFilter gp) {
        this.gp = gp;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final PatientCriteria that = (PatientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(addressText, that.addressText) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(egn, that.egn) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(active, that.active) &&
            Objects.equals(gp, that.gp) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(appointmentId, that.appointmentId) &&
            Objects.equals(examinationHistoryId, that.examinationHistoryId) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(vaccineId, that.vaccineId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            addressText,
            birthDate,
            egn,
            phone,
            active,
            gp,
            userId,
            documentsId,
            appointmentId,
            examinationHistoryId,
            doctorId,
            vaccineId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (addressText != null ? "addressText=" + addressText + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (egn != null ? "egn=" + egn + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (gp != null ? "gp=" + gp + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (documentsId != null ? "documentsId=" + documentsId + ", " : "") +
            (appointmentId != null ? "appointmentId=" + appointmentId + ", " : "") +
            (examinationHistoryId != null ? "examinationHistoryId=" + examinationHistoryId + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            (vaccineId != null ? "vaccineId=" + vaccineId + ", " : "") +
            "}";
    }
}
