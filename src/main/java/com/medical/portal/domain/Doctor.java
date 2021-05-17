package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Doctor entity.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "meta")
    private String meta;

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "patient" }, allowSetters = true)
    private Set<Documents> documents = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "patient" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctor", "patient" }, allowSetters = true)
    private Set<ExaminationHistory> examinationHistories = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_doctor__patient",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @JsonIgnoreProperties(
        value = { "user", "documents", "appointments", "examinationHistories", "doctors", "vaccines" },
        allowSetters = true
    )
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany(mappedBy = "doctors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "doctors" }, allowSetters = true)
    private Set<Hospital> hospitals = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patient", "doctor" }, allowSetters = true)
    private Set<Vaccine> vaccines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Doctor firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Doctor lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Doctor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Doctor phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public Doctor specialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public Doctor additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getMeta() {
        return this.meta;
    }

    public Doctor meta(String meta) {
        this.meta = meta;
        return this;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Set<Documents> getDocuments() {
        return this.documents;
    }

    public Doctor documents(Set<Documents> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Doctor addDocuments(Documents documents) {
        this.documents.add(documents);
        documents.setDoctor(this);
        return this;
    }

    public Doctor removeDocuments(Documents documents) {
        this.documents.remove(documents);
        documents.setDoctor(null);
        return this;
    }

    public void setDocuments(Set<Documents> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setDoctor(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setDoctor(this));
        }
        this.documents = documents;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public Doctor appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Doctor addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setDoctor(this);
        return this;
    }

    public Doctor removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setDoctor(null);
        return this;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setDoctor(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setDoctor(this));
        }
        this.appointments = appointments;
    }

    public Set<ExaminationHistory> getExaminationHistories() {
        return this.examinationHistories;
    }

    public Doctor examinationHistories(Set<ExaminationHistory> examinationHistories) {
        this.setExaminationHistories(examinationHistories);
        return this;
    }

    public Doctor addExaminationHistory(ExaminationHistory examinationHistory) {
        this.examinationHistories.add(examinationHistory);
        examinationHistory.setDoctor(this);
        return this;
    }

    public Doctor removeExaminationHistory(ExaminationHistory examinationHistory) {
        this.examinationHistories.remove(examinationHistory);
        examinationHistory.setDoctor(null);
        return this;
    }

    public void setExaminationHistories(Set<ExaminationHistory> examinationHistories) {
        if (this.examinationHistories != null) {
            this.examinationHistories.forEach(i -> i.setDoctor(null));
        }
        if (examinationHistories != null) {
            examinationHistories.forEach(i -> i.setDoctor(this));
        }
        this.examinationHistories = examinationHistories;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public Doctor patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Doctor addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getDoctors().add(this);
        return this;
    }

    public Doctor removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getDoctors().remove(this);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Set<Hospital> getHospitals() {
        return this.hospitals;
    }

    public Doctor hospitals(Set<Hospital> hospitals) {
        this.setHospitals(hospitals);
        return this;
    }

    public Doctor addHospital(Hospital hospital) {
        this.hospitals.add(hospital);
        hospital.getDoctors().add(this);
        return this;
    }

    public Doctor removeHospital(Hospital hospital) {
        this.hospitals.remove(hospital);
        hospital.getDoctors().remove(this);
        return this;
    }

    public void setHospitals(Set<Hospital> hospitals) {
//        if (this.hospitals != null) {
//            this.hospitals.forEach(i -> i.removeDoctor(this));
//        }
//        if (hospitals != null) {
//            hospitals.forEach(i -> i.addDoctor(this));
//        }
        this.hospitals = hospitals;
    }

    public Set<Vaccine> getVaccines() {
        return this.vaccines;
    }

    public Doctor vaccines(Set<Vaccine> vaccines) {
        this.setVaccines(vaccines);
        return this;
    }

    public Doctor addVaccine(Vaccine vaccine) {
        this.vaccines.add(vaccine);
        vaccine.setDoctor(this);
        return this;
    }

    public Doctor removeVaccine(Vaccine vaccine) {
        this.vaccines.remove(vaccine);
        vaccine.setDoctor(null);
        return this;
    }

    public void setVaccines(Set<Vaccine> vaccines) {
//        if (this.vaccines != null) {
//            this.vaccines.forEach(i -> i.setDoctor(null));
//        }
//        if (vaccines != null) {
//            vaccines.forEach(i -> i.setDoctor(this));
//        }
        this.vaccines = vaccines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doctor)) {
            return false;
        }
        return id != null && id.equals(((Doctor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            ", meta='" + getMeta() + "'" +
            "}";
    }
}
