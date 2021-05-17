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
 * Criteria class for the {@link com.medical.portal.domain.Hospital} entity. This class is used
 * in {@link com.medical.portal.web.rest.HospitalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hospitals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HospitalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private StringFilter city;

    private StringFilter administrationPhones;

    private BooleanFilter noi;

    private LongFilter director;

    private LongFilter doctorId;

    public HospitalCriteria() {}

    public HospitalCriteria(HospitalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.administrationPhones = other.administrationPhones == null ? null : other.administrationPhones.copy();
        this.noi = other.noi == null ? null : other.noi.copy();
        this.director = other.director == null ? null : other.director.copy();
        this.doctorId = other.doctorId == null ? null : other.doctorId.copy();
    }

    @Override
    public HospitalCriteria copy() {
        return new HospitalCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getAdministrationPhones() {
        return administrationPhones;
    }

    public StringFilter administrationPhones() {
        if (administrationPhones == null) {
            administrationPhones = new StringFilter();
        }
        return administrationPhones;
    }

    public void setAdministrationPhones(StringFilter administrationPhones) {
        this.administrationPhones = administrationPhones;
    }

    public BooleanFilter getNoi() {
        return noi;
    }

    public BooleanFilter noi() {
        if (noi == null) {
            noi = new BooleanFilter();
        }
        return noi;
    }

    public void setNoi(BooleanFilter noi) {
        this.noi = noi;
    }

    public LongFilter getDirector() {
        return director;
    }

    public LongFilter director() {
        if (director == null) {
            director = new LongFilter();
        }
        return director;
    }

    public void setDirector(LongFilter director) {
        this.director = director;
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
        final HospitalCriteria that = (HospitalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(city, that.city) &&
            Objects.equals(administrationPhones, that.administrationPhones) &&
            Objects.equals(noi, that.noi) &&
            Objects.equals(director, that.director) &&
            Objects.equals(doctorId, that.doctorId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, city, administrationPhones, noi, director, doctorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HospitalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (administrationPhones != null ? "administrationPhones=" + administrationPhones + ", " : "") +
            (noi != null ? "noi=" + noi + ", " : "") +
            (director != null ? "director=" + director + ", " : "") +
            (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            "}";
    }
}
