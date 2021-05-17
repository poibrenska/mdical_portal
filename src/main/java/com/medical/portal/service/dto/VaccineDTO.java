package com.medical.portal.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medical.portal.domain.Vaccine} entity.
 */
@ApiModel(description = "The Vaccine entity.")
public class VaccineDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    private Integer dose;

    private Instant nextDoseDate;

    private Integer dosesLeft;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public Instant getNextDoseDate() {
        return nextDoseDate;
    }

    public void setNextDoseDate(Instant nextDoseDate) {
        this.nextDoseDate = nextDoseDate;
    }

    public Integer getDosesLeft() {
        return dosesLeft;
    }

    public void setDosesLeft(Integer dosesLeft) {
        this.dosesLeft = dosesLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccineDTO)) {
            return false;
        }

        VaccineDTO vaccineDTO = (VaccineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccineDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dose=" + getDose() +
            ", nextDoseDate='" + getNextDoseDate() + "'" +
            ", dosesLeft=" + getDosesLeft() +
            "}";
    }
}
