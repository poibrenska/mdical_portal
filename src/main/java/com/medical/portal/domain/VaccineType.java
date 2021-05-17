package com.medical.portal.domain;

import com.medical.portal.domain.enumeration.CalendarUnit;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Vaccine Type entity.
 */
@Entity
@Table(name = "vaccine_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VaccineType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "doses", nullable = false)
    private String doses;

    @NotNull
    @Column(name = "duration_between_doses_time", nullable = false)
    private Integer durationBetweenDosesTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "duration_between_doses_unit", nullable = false)
    private CalendarUnit durationBetweenDosesUnit;

    @Column(name = "manufacturer")
    private String manufacturer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VaccineType id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public VaccineType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoses() {
        return this.doses;
    }

    public VaccineType doses(String doses) {
        this.doses = doses;
        return this;
    }

    public void setDoses(String doses) {
        this.doses = doses;
    }

    public Integer getDurationBetweenDosesTime() {
        return this.durationBetweenDosesTime;
    }

    public VaccineType durationBetweenDosesTime(Integer durationBetweenDosesTime) {
        this.durationBetweenDosesTime = durationBetweenDosesTime;
        return this;
    }

    public void setDurationBetweenDosesTime(Integer durationBetweenDosesTime) {
        this.durationBetweenDosesTime = durationBetweenDosesTime;
    }

    public CalendarUnit getDurationBetweenDosesUnit() {
        return this.durationBetweenDosesUnit;
    }

    public VaccineType durationBetweenDosesUnit(CalendarUnit durationBetweenDosesUnit) {
        this.durationBetweenDosesUnit = durationBetweenDosesUnit;
        return this;
    }

    public void setDurationBetweenDosesUnit(CalendarUnit durationBetweenDosesUnit) {
        this.durationBetweenDosesUnit = durationBetweenDosesUnit;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public VaccineType manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccineType)) {
            return false;
        }
        return id != null && id.equals(((VaccineType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", doses='" + getDoses() + "'" +
            ", durationBetweenDosesTime=" + getDurationBetweenDosesTime() +
            ", durationBetweenDosesUnit='" + getDurationBetweenDosesUnit() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            "}";
    }
}
