package com.medical.portal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Patient entity.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient extends AbstractAuditingEntity implements Serializable {

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

    @NotNull
    @Column(name = "address_text", nullable = false)
    private String addressText;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Column(name = "egn", nullable = false)
    private String egn;

    @Column(name = "phone")
    private String phone;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "gp")
    private Long gp;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Documents> documents = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExaminationHistory> examinationHistories = new HashSet<>();

    @ManyToMany(mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties(
        value = { "documents", "appointments", "examinationHistories", "patients", "hospitals", "vaccines" },
        allowSetters = true
    )
    private Set<Doctor> doctors = new HashSet<>();

    @OneToMany(mappedBy = "patient")
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

    public Patient id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Patient firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Patient lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressText() {
        return this.addressText;
    }

    public Patient addressText(String addressText) {
        this.addressText = addressText;
        return this;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Patient birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEgn() {
        return this.egn;
    }

    public Patient egn(String egn) {
        this.egn = egn;
        return this;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getPhone() {
        return this.phone;
    }

    public Patient phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Patient active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getGp() {
        return this.gp;
    }

    public Patient gp(Long gp) {
        this.gp = gp;
        return this;
    }

    public void setGp(Long gp) {
        this.gp = gp;
    }

    public User getUser() {
        return this.user;
    }

    public Patient user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Documents> getDocuments() {
        return this.documents;
    }

    public Patient documents(Set<Documents> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Patient addDocuments(Documents documents) {
        this.documents.add(documents);
        documents.setPatient(this);
        return this;
    }

    public Patient removeDocuments(Documents documents) {
        this.documents.remove(documents);
        documents.setPatient(null);
        return this;
    }

    public void setDocuments(Set<Documents> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setPatient(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setPatient(this));
        }
        this.documents = documents;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public Patient appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Patient addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setPatient(this);
        return this;
    }

    public Patient removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setPatient(null);
        return this;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.setPatient(null));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.setPatient(this));
        }
        this.appointments = appointments;
    }

    public Set<ExaminationHistory> getExaminationHistories() {
        return this.examinationHistories;
    }

    public Patient examinationHistories(Set<ExaminationHistory> examinationHistories) {
        this.setExaminationHistories(examinationHistories);
        return this;
    }

    public Patient addExaminationHistory(ExaminationHistory examinationHistory) {
        this.examinationHistories.add(examinationHistory);
        examinationHistory.setPatient(this);
        return this;
    }

    public Patient removeExaminationHistory(ExaminationHistory examinationHistory) {
        this.examinationHistories.remove(examinationHistory);
        examinationHistory.setPatient(null);
        return this;
    }

    public void setExaminationHistories(Set<ExaminationHistory> examinationHistories) {
        if (this.examinationHistories != null) {
            this.examinationHistories.forEach(i -> i.setPatient(null));
        }
        if (examinationHistories != null) {
            examinationHistories.forEach(i -> i.setPatient(this));
        }
        this.examinationHistories = examinationHistories;
    }

    public Set<Doctor> getDoctors() {
        return this.doctors;
    }

    public Patient doctors(Set<Doctor> doctors) {
        this.setDoctors(doctors);
        return this;
    }

    public Patient addDoctor(Doctor doctor) {
        this.doctors.add(doctor);
        doctor.getPatients().add(this);
        return this;
    }

    public Patient removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
        doctor.getPatients().remove(this);
        return this;
    }

    public void setDoctors(Set<Doctor> doctors) {
        if (this.doctors != null) {
            this.doctors.forEach(i -> i.removePatient(this));
        }
        if (doctors != null) {
            doctors.forEach(i -> i.addPatient(this));
        }
        this.doctors = doctors;
    }

    public Set<Vaccine> getVaccines() {
        return this.vaccines;
    }

    public Patient vaccines(Set<Vaccine> vaccines) {
        this.setVaccines(vaccines);
        return this;
    }

    public Patient addVaccine(Vaccine vaccine) {
        this.vaccines.add(vaccine);
        vaccine.setPatient(this);
        return this;
    }

    public Patient removeVaccine(Vaccine vaccine) {
        this.vaccines.remove(vaccine);
        vaccine.setPatient(null);
        return this;
    }

    public void setVaccines(Set<Vaccine> vaccines) {
        if (this.vaccines != null) {
            this.vaccines.forEach(i -> i.setPatient(null));
        }
        if (vaccines != null) {
            vaccines.forEach(i -> i.setPatient(this));
        }
        this.vaccines = vaccines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", addressText='" + getAddressText() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", egn='" + getEgn() + "'" +
            ", phone=" + getPhone() +
            ", active='" + getActive() + "'" +
            ", gp=" + getGp() +
            "}";
    }
}
