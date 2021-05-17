package com.medical.portal.service.criteria;

import com.medical.portal.domain.enumeration.CalendarUnit;
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
 * Criteria class for the {@link com.medical.portal.domain.VaccineType} entity. This class is used
 * in {@link com.medical.portal.web.rest.VaccineTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vaccine-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VaccineTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CalendarUnit
     */
    public static class CalendarUnitFilter extends Filter<CalendarUnit> {

        public CalendarUnitFilter() {}

        public CalendarUnitFilter(CalendarUnitFilter filter) {
            super(filter);
        }

        @Override
        public CalendarUnitFilter copy() {
            return new CalendarUnitFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter doses;

    private IntegerFilter durationBetweenDosesTime;

    private CalendarUnitFilter durationBetweenDosesUnit;

    private StringFilter manufacturer;

    public VaccineTypeCriteria() {}

    public VaccineTypeCriteria(VaccineTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.doses = other.doses == null ? null : other.doses.copy();
        this.durationBetweenDosesTime = other.durationBetweenDosesTime == null ? null : other.durationBetweenDosesTime.copy();
        this.durationBetweenDosesUnit = other.durationBetweenDosesUnit == null ? null : other.durationBetweenDosesUnit.copy();
        this.manufacturer = other.manufacturer == null ? null : other.manufacturer.copy();
    }

    @Override
    public VaccineTypeCriteria copy() {
        return new VaccineTypeCriteria(this);
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

    public StringFilter getDoses() {
        return doses;
    }

    public StringFilter doses() {
        if (doses == null) {
            doses = new StringFilter();
        }
        return doses;
    }

    public void setDoses(StringFilter doses) {
        this.doses = doses;
    }

    public IntegerFilter getDurationBetweenDosesTime() {
        return durationBetweenDosesTime;
    }

    public IntegerFilter durationBetweenDosesTime() {
        if (durationBetweenDosesTime == null) {
            durationBetweenDosesTime = new IntegerFilter();
        }
        return durationBetweenDosesTime;
    }

    public void setDurationBetweenDosesTime(IntegerFilter durationBetweenDosesTime) {
        this.durationBetweenDosesTime = durationBetweenDosesTime;
    }

    public CalendarUnitFilter getDurationBetweenDosesUnit() {
        return durationBetweenDosesUnit;
    }

    public CalendarUnitFilter durationBetweenDosesUnit() {
        if (durationBetweenDosesUnit == null) {
            durationBetweenDosesUnit = new CalendarUnitFilter();
        }
        return durationBetweenDosesUnit;
    }

    public void setDurationBetweenDosesUnit(CalendarUnitFilter durationBetweenDosesUnit) {
        this.durationBetweenDosesUnit = durationBetweenDosesUnit;
    }

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public StringFilter manufacturer() {
        if (manufacturer == null) {
            manufacturer = new StringFilter();
        }
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VaccineTypeCriteria that = (VaccineTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(doses, that.doses) &&
            Objects.equals(durationBetweenDosesTime, that.durationBetweenDosesTime) &&
            Objects.equals(durationBetweenDosesUnit, that.durationBetweenDosesUnit) &&
            Objects.equals(manufacturer, that.manufacturer)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, doses, durationBetweenDosesTime, durationBetweenDosesUnit, manufacturer);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (doses != null ? "doses=" + doses + ", " : "") +
            (durationBetweenDosesTime != null ? "durationBetweenDosesTime=" + durationBetweenDosesTime + ", " : "") +
            (durationBetweenDosesUnit != null ? "durationBetweenDosesUnit=" + durationBetweenDosesUnit + ", " : "") +
            (manufacturer != null ? "manufacturer=" + manufacturer + ", " : "") +
            "}";
    }
}
