package com.medical.portal.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medical.portal.domain.Hospital} entity.
 */
@ApiModel(description = "The Hospital entity.")
public class HospitalDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String city;

    private String administrationPhones;

    @NotNull
    private Boolean noi;

    private Long director;

    private Set<DoctorDTO> doctors = new HashSet<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdministrationPhones() {
        return administrationPhones;
    }

    public void setAdministrationPhones(String administrationPhones) {
        this.administrationPhones = administrationPhones;
    }

    public Boolean getNoi() {
        return noi;
    }

    public void setNoi(Boolean noi) {
        this.noi = noi;
    }

    public Long getDirector() {
        return director;
    }

    public void setDirector(Long director) {
        this.director = director;
    }

    public Set<DoctorDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<DoctorDTO> doctors) {
        this.doctors = doctors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HospitalDTO)) {
            return false;
        }

        HospitalDTO hospitalDTO = (HospitalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hospitalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HospitalDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", administrationPhones='" + getAdministrationPhones() + "'" +
            ", noi='" + getNoi() + "'" +
            ", director=" + getDirector() +
            ", doctors=" + getDoctors() +
            "}";
    }
}
