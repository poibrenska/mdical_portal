package com.medical.portal.service.dto;

import com.medical.portal.domain.enumeration.CalendarUnit;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medical.portal.domain.VaccineType} entity.
 */
@ApiModel(description = "The Vaccine Type entity.")
public class VaccineTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String doses;

    @NotNull
    private Integer durationBetweenDosesTime;

    @NotNull
    private CalendarUnit durationBetweenDosesUnit;

    private String manufacturer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoses() {
        return doses;
    }

    public void setDoses(String doses) {
        this.doses = doses;
    }

    public Integer getDurationBetweenDosesTime() {
        return durationBetweenDosesTime;
    }

    public void setDurationBetweenDosesTime(Integer durationBetweenDosesTime) {
        this.durationBetweenDosesTime = durationBetweenDosesTime;
    }

    public CalendarUnit getDurationBetweenDosesUnit() {
        return durationBetweenDosesUnit;
    }

    public void setDurationBetweenDosesUnit(CalendarUnit durationBetweenDosesUnit) {
        this.durationBetweenDosesUnit = durationBetweenDosesUnit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccineTypeDTO)) {
            return false;
        }

        VaccineTypeDTO vaccineTypeDTO = (VaccineTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccineTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", doses='" + getDoses() + "'" +
            ", durationBetweenDosesTime=" + getDurationBetweenDosesTime() +
            ", durationBetweenDosesUnit='" + getDurationBetweenDosesUnit() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            "}";
    }
}
