package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Appointment;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Patient;
import com.medical.portal.repository.AppointmentRepository;
import com.medical.portal.service.criteria.AppointmentCriteria;
import com.medical.portal.service.dto.AppointmentDTO;
import com.medical.portal.service.mapper.AppointmentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppointmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentResourceIT {

    private static final Instant DEFAULT_APPOINTMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPOINTMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_FINISHED = false;
    private static final Boolean UPDATED_FINISHED = true;

    private static final String ENTITY_API_URL = "/api/appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .appointmentDate(DEFAULT_APPOINTMENT_DATE)
            .active(DEFAULT_ACTIVE)
            .finished(DEFAULT_FINISHED);
        return appointment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createUpdatedEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .appointmentDate(UPDATED_APPOINTMENT_DATE)
            .active(UPDATED_ACTIVE)
            .finished(UPDATED_FINISHED);
        return appointment;
    }

    @BeforeEach
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();
        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getAppointmentDate()).isEqualTo(DEFAULT_APPOINTMENT_DATE);
        assertThat(testAppointment.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAppointment.getFinished()).isEqualTo(DEFAULT_FINISHED);
    }

    @Test
    @Transactional
    void createAppointmentWithExistingId() throws Exception {
        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].appointmentDate").value(hasItem(DEFAULT_APPOINTMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].finished").value(hasItem(DEFAULT_FINISHED.booleanValue())));
    }

    @Test
    @Transactional
    void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL_ID, appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.appointmentDate").value(DEFAULT_APPOINTMENT_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.finished").value(DEFAULT_FINISHED.booleanValue()));
    }

    @Test
    @Transactional
    void getAppointmentsByIdFiltering() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        Long id = appointment.getId();

        defaultAppointmentShouldBeFound("id.equals=" + id);
        defaultAppointmentShouldNotBeFound("id.notEquals=" + id);

        defaultAppointmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppointmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAppointmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppointmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppointmentsByAppointmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where appointmentDate equals to DEFAULT_APPOINTMENT_DATE
        defaultAppointmentShouldBeFound("appointmentDate.equals=" + DEFAULT_APPOINTMENT_DATE);

        // Get all the appointmentList where appointmentDate equals to UPDATED_APPOINTMENT_DATE
        defaultAppointmentShouldNotBeFound("appointmentDate.equals=" + UPDATED_APPOINTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByAppointmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where appointmentDate not equals to DEFAULT_APPOINTMENT_DATE
        defaultAppointmentShouldNotBeFound("appointmentDate.notEquals=" + DEFAULT_APPOINTMENT_DATE);

        // Get all the appointmentList where appointmentDate not equals to UPDATED_APPOINTMENT_DATE
        defaultAppointmentShouldBeFound("appointmentDate.notEquals=" + UPDATED_APPOINTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByAppointmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where appointmentDate in DEFAULT_APPOINTMENT_DATE or UPDATED_APPOINTMENT_DATE
        defaultAppointmentShouldBeFound("appointmentDate.in=" + DEFAULT_APPOINTMENT_DATE + "," + UPDATED_APPOINTMENT_DATE);

        // Get all the appointmentList where appointmentDate equals to UPDATED_APPOINTMENT_DATE
        defaultAppointmentShouldNotBeFound("appointmentDate.in=" + UPDATED_APPOINTMENT_DATE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByAppointmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where appointmentDate is not null
        defaultAppointmentShouldBeFound("appointmentDate.specified=true");

        // Get all the appointmentList where appointmentDate is null
        defaultAppointmentShouldNotBeFound("appointmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where active equals to DEFAULT_ACTIVE
        defaultAppointmentShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the appointmentList where active equals to UPDATED_ACTIVE
        defaultAppointmentShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where active not equals to DEFAULT_ACTIVE
        defaultAppointmentShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the appointmentList where active not equals to UPDATED_ACTIVE
        defaultAppointmentShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultAppointmentShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the appointmentList where active equals to UPDATED_ACTIVE
        defaultAppointmentShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAppointmentsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where active is not null
        defaultAppointmentShouldBeFound("active.specified=true");

        // Get all the appointmentList where active is null
        defaultAppointmentShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByFinishedIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where finished equals to DEFAULT_FINISHED
        defaultAppointmentShouldBeFound("finished.equals=" + DEFAULT_FINISHED);

        // Get all the appointmentList where finished equals to UPDATED_FINISHED
        defaultAppointmentShouldNotBeFound("finished.equals=" + UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void getAllAppointmentsByFinishedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where finished not equals to DEFAULT_FINISHED
        defaultAppointmentShouldNotBeFound("finished.notEquals=" + DEFAULT_FINISHED);

        // Get all the appointmentList where finished not equals to UPDATED_FINISHED
        defaultAppointmentShouldBeFound("finished.notEquals=" + UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void getAllAppointmentsByFinishedIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where finished in DEFAULT_FINISHED or UPDATED_FINISHED
        defaultAppointmentShouldBeFound("finished.in=" + DEFAULT_FINISHED + "," + UPDATED_FINISHED);

        // Get all the appointmentList where finished equals to UPDATED_FINISHED
        defaultAppointmentShouldNotBeFound("finished.in=" + UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void getAllAppointmentsByFinishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where finished is not null
        defaultAppointmentShouldBeFound("finished.specified=true");

        // Get all the appointmentList where finished is null
        defaultAppointmentShouldNotBeFound("finished.specified=false");
    }

    @Test
    @Transactional
    void getAllAppointmentsByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        appointment.setDoctor(doctor);
        appointmentRepository.saveAndFlush(appointment);
        Long doctorId = doctor.getId();

        // Get all the appointmentList where doctor equals to doctorId
        defaultAppointmentShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the appointmentList where doctor equals to (doctorId + 1)
        defaultAppointmentShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllAppointmentsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        appointment.setPatient(patient);
        appointmentRepository.saveAndFlush(appointment);
        Long patientId = patient.getId();

        // Get all the appointmentList where patient equals to patientId
        defaultAppointmentShouldBeFound("patientId.equals=" + patientId);

        // Get all the appointmentList where patient equals to (patientId + 1)
        defaultAppointmentShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppointmentShouldBeFound(String filter) throws Exception {
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].appointmentDate").value(hasItem(DEFAULT_APPOINTMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].finished").value(hasItem(DEFAULT_FINISHED.booleanValue())));

        // Check, that the count call also returns 1
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppointmentShouldNotBeFound(String filter) throws Exception {
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).get();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment.appointmentDate(UPDATED_APPOINTMENT_DATE).active(UPDATED_ACTIVE).finished(UPDATED_FINISHED);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getAppointmentDate()).isEqualTo(UPDATED_APPOINTMENT_DATE);
        assertThat(testAppointment.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAppointment.getFinished()).isEqualTo(UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void putNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment.appointmentDate(UPDATED_APPOINTMENT_DATE).finished(UPDATED_FINISHED);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getAppointmentDate()).isEqualTo(UPDATED_APPOINTMENT_DATE);
        assertThat(testAppointment.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAppointment.getFinished()).isEqualTo(UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment.appointmentDate(UPDATED_APPOINTMENT_DATE).active(UPDATED_ACTIVE).finished(UPDATED_FINISHED);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getAppointmentDate()).isEqualTo(UPDATED_APPOINTMENT_DATE);
        assertThat(testAppointment.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAppointment.getFinished()).isEqualTo(UPDATED_FINISHED);
    }

    @Test
    @Transactional
    void patchNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Delete the appointment
        restAppointmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
