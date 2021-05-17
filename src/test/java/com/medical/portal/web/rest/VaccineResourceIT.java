package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Patient;
import com.medical.portal.domain.Vaccine;
import com.medical.portal.repository.VaccineRepository;
import com.medical.portal.service.criteria.VaccineCriteria;
import com.medical.portal.service.dto.VaccineDTO;
import com.medical.portal.service.mapper.VaccineMapper;
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
 * Integration tests for the {@link VaccineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccineResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOSE = 1;
    private static final Integer UPDATED_DOSE = 2;
    private static final Integer SMALLER_DOSE = 1 - 1;

    private static final Instant DEFAULT_NEXT_DOSE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_DOSE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DOSES_LEFT = 1;
    private static final Integer UPDATED_DOSES_LEFT = 2;
    private static final Integer SMALLER_DOSES_LEFT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/vaccines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccineMockMvc;

    private Vaccine vaccine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createEntity(EntityManager em) {
        Vaccine vaccine = new Vaccine()
            .type(DEFAULT_TYPE)
            .dose(DEFAULT_DOSE)
            .nextDoseDate(DEFAULT_NEXT_DOSE_DATE)
            .dosesLeft(DEFAULT_DOSES_LEFT);
        return vaccine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createUpdatedEntity(EntityManager em) {
        Vaccine vaccine = new Vaccine()
            .type(UPDATED_TYPE)
            .dose(UPDATED_DOSE)
            .nextDoseDate(UPDATED_NEXT_DOSE_DATE)
            .dosesLeft(UPDATED_DOSES_LEFT);
        return vaccine;
    }

    @BeforeEach
    public void initTest() {
        vaccine = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccine() throws Exception {
        int databaseSizeBeforeCreate = vaccineRepository.findAll().size();
        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);
        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineDTO)))
            .andExpect(status().isCreated());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeCreate + 1);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVaccine.getDose()).isEqualTo(DEFAULT_DOSE);
        assertThat(testVaccine.getNextDoseDate()).isEqualTo(DEFAULT_NEXT_DOSE_DATE);
        assertThat(testVaccine.getDosesLeft()).isEqualTo(DEFAULT_DOSES_LEFT);
    }

    @Test
    @Transactional
    void createVaccineWithExistingId() throws Exception {
        // Create the Vaccine with an existing ID
        vaccine.setId(1L);
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        int databaseSizeBeforeCreate = vaccineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineRepository.findAll().size();
        // set the field null
        vaccine.setType(null);

        // Create the Vaccine, which fails.
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineDTO)))
            .andExpect(status().isBadRequest());

        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccines() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccine.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE)))
            .andExpect(jsonPath("$.[*].nextDoseDate").value(hasItem(DEFAULT_NEXT_DOSE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dosesLeft").value(hasItem(DEFAULT_DOSES_LEFT)));
    }

    @Test
    @Transactional
    void getVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get the vaccine
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccine.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.dose").value(DEFAULT_DOSE))
            .andExpect(jsonPath("$.nextDoseDate").value(DEFAULT_NEXT_DOSE_DATE.toString()))
            .andExpect(jsonPath("$.dosesLeft").value(DEFAULT_DOSES_LEFT));
    }

    @Test
    @Transactional
    void getVaccinesByIdFiltering() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        Long id = vaccine.getId();

        defaultVaccineShouldBeFound("id.equals=" + id);
        defaultVaccineShouldNotBeFound("id.notEquals=" + id);

        defaultVaccineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVaccineShouldNotBeFound("id.greaterThan=" + id);

        defaultVaccineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVaccineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type equals to DEFAULT_TYPE
        defaultVaccineShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the vaccineList where type equals to UPDATED_TYPE
        defaultVaccineShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type not equals to DEFAULT_TYPE
        defaultVaccineShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the vaccineList where type not equals to UPDATED_TYPE
        defaultVaccineShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultVaccineShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the vaccineList where type equals to UPDATED_TYPE
        defaultVaccineShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type is not null
        defaultVaccineShouldBeFound("type.specified=true");

        // Get all the vaccineList where type is null
        defaultVaccineShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeContainsSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type contains DEFAULT_TYPE
        defaultVaccineShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the vaccineList where type contains UPDATED_TYPE
        defaultVaccineShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllVaccinesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where type does not contain DEFAULT_TYPE
        defaultVaccineShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the vaccineList where type does not contain UPDATED_TYPE
        defaultVaccineShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose equals to DEFAULT_DOSE
        defaultVaccineShouldBeFound("dose.equals=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose equals to UPDATED_DOSE
        defaultVaccineShouldNotBeFound("dose.equals=" + UPDATED_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose not equals to DEFAULT_DOSE
        defaultVaccineShouldNotBeFound("dose.notEquals=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose not equals to UPDATED_DOSE
        defaultVaccineShouldBeFound("dose.notEquals=" + UPDATED_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose in DEFAULT_DOSE or UPDATED_DOSE
        defaultVaccineShouldBeFound("dose.in=" + DEFAULT_DOSE + "," + UPDATED_DOSE);

        // Get all the vaccineList where dose equals to UPDATED_DOSE
        defaultVaccineShouldNotBeFound("dose.in=" + UPDATED_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose is not null
        defaultVaccineShouldBeFound("dose.specified=true");

        // Get all the vaccineList where dose is null
        defaultVaccineShouldNotBeFound("dose.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose is greater than or equal to DEFAULT_DOSE
        defaultVaccineShouldBeFound("dose.greaterThanOrEqual=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose is greater than or equal to UPDATED_DOSE
        defaultVaccineShouldNotBeFound("dose.greaterThanOrEqual=" + UPDATED_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose is less than or equal to DEFAULT_DOSE
        defaultVaccineShouldBeFound("dose.lessThanOrEqual=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose is less than or equal to SMALLER_DOSE
        defaultVaccineShouldNotBeFound("dose.lessThanOrEqual=" + SMALLER_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose is less than DEFAULT_DOSE
        defaultVaccineShouldNotBeFound("dose.lessThan=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose is less than UPDATED_DOSE
        defaultVaccineShouldBeFound("dose.lessThan=" + UPDATED_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByDoseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dose is greater than DEFAULT_DOSE
        defaultVaccineShouldNotBeFound("dose.greaterThan=" + DEFAULT_DOSE);

        // Get all the vaccineList where dose is greater than SMALLER_DOSE
        defaultVaccineShouldBeFound("dose.greaterThan=" + SMALLER_DOSE);
    }

    @Test
    @Transactional
    void getAllVaccinesByNextDoseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where nextDoseDate equals to DEFAULT_NEXT_DOSE_DATE
        defaultVaccineShouldBeFound("nextDoseDate.equals=" + DEFAULT_NEXT_DOSE_DATE);

        // Get all the vaccineList where nextDoseDate equals to UPDATED_NEXT_DOSE_DATE
        defaultVaccineShouldNotBeFound("nextDoseDate.equals=" + UPDATED_NEXT_DOSE_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinesByNextDoseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where nextDoseDate not equals to DEFAULT_NEXT_DOSE_DATE
        defaultVaccineShouldNotBeFound("nextDoseDate.notEquals=" + DEFAULT_NEXT_DOSE_DATE);

        // Get all the vaccineList where nextDoseDate not equals to UPDATED_NEXT_DOSE_DATE
        defaultVaccineShouldBeFound("nextDoseDate.notEquals=" + UPDATED_NEXT_DOSE_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinesByNextDoseDateIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where nextDoseDate in DEFAULT_NEXT_DOSE_DATE or UPDATED_NEXT_DOSE_DATE
        defaultVaccineShouldBeFound("nextDoseDate.in=" + DEFAULT_NEXT_DOSE_DATE + "," + UPDATED_NEXT_DOSE_DATE);

        // Get all the vaccineList where nextDoseDate equals to UPDATED_NEXT_DOSE_DATE
        defaultVaccineShouldNotBeFound("nextDoseDate.in=" + UPDATED_NEXT_DOSE_DATE);
    }

    @Test
    @Transactional
    void getAllVaccinesByNextDoseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where nextDoseDate is not null
        defaultVaccineShouldBeFound("nextDoseDate.specified=true");

        // Get all the vaccineList where nextDoseDate is null
        defaultVaccineShouldNotBeFound("nextDoseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft equals to DEFAULT_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.equals=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft equals to UPDATED_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.equals=" + UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft not equals to DEFAULT_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.notEquals=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft not equals to UPDATED_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.notEquals=" + UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft in DEFAULT_DOSES_LEFT or UPDATED_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.in=" + DEFAULT_DOSES_LEFT + "," + UPDATED_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft equals to UPDATED_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.in=" + UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft is not null
        defaultVaccineShouldBeFound("dosesLeft.specified=true");

        // Get all the vaccineList where dosesLeft is null
        defaultVaccineShouldNotBeFound("dosesLeft.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft is greater than or equal to DEFAULT_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.greaterThanOrEqual=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft is greater than or equal to UPDATED_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.greaterThanOrEqual=" + UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft is less than or equal to DEFAULT_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.lessThanOrEqual=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft is less than or equal to SMALLER_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.lessThanOrEqual=" + SMALLER_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft is less than DEFAULT_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.lessThan=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft is less than UPDATED_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.lessThan=" + UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByDosesLeftIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList where dosesLeft is greater than DEFAULT_DOSES_LEFT
        defaultVaccineShouldNotBeFound("dosesLeft.greaterThan=" + DEFAULT_DOSES_LEFT);

        // Get all the vaccineList where dosesLeft is greater than SMALLER_DOSES_LEFT
        defaultVaccineShouldBeFound("dosesLeft.greaterThan=" + SMALLER_DOSES_LEFT);
    }

    @Test
    @Transactional
    void getAllVaccinesByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        vaccine.setPatient(patient);
        patient.addVaccine(vaccine);
        vaccineRepository.saveAndFlush(vaccine);
        Long patientId = patient.getId();

        // Get all the vaccineList where patient equals to patientId
        defaultVaccineShouldBeFound("patientId.equals=" + patientId);

        // Get all the vaccineList where patient equals to (patientId + 1)
        defaultVaccineShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    @Test
    @Transactional
    void getAllVaccinesByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        vaccine.setDoctor(doctor);
        doctor.addVaccine(vaccine);
        vaccineRepository.saveAndFlush(vaccine);
        Long doctorId = doctor.getId();

        // Get all the vaccineList where doctor equals to doctorId
        defaultVaccineShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the vaccineList where doctor equals to (doctorId + 1)
        defaultVaccineShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVaccineShouldBeFound(String filter) throws Exception {
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccine.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE)))
            .andExpect(jsonPath("$.[*].nextDoseDate").value(hasItem(DEFAULT_NEXT_DOSE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dosesLeft").value(hasItem(DEFAULT_DOSES_LEFT)));

        // Check, that the count call also returns 1
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVaccineShouldNotBeFound(String filter) throws Exception {
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVaccine() throws Exception {
        // Get the vaccine
        restVaccineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine
        Vaccine updatedVaccine = vaccineRepository.findById(vaccine.getId()).get();
        // Disconnect from session so that the updates on updatedVaccine are not directly saved in db
        em.detach(updatedVaccine);
        updatedVaccine.type(UPDATED_TYPE).dose(UPDATED_DOSE).nextDoseDate(UPDATED_NEXT_DOSE_DATE).dosesLeft(UPDATED_DOSES_LEFT);
        VaccineDTO vaccineDTO = vaccineMapper.toDto(updatedVaccine);

        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVaccine.getDose()).isEqualTo(UPDATED_DOSE);
        assertThat(testVaccine.getNextDoseDate()).isEqualTo(UPDATED_NEXT_DOSE_DATE);
        assertThat(testVaccine.getDosesLeft()).isEqualTo(UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void putNonExistingVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine.type(UPDATED_TYPE).dose(UPDATED_DOSE).nextDoseDate(UPDATED_NEXT_DOSE_DATE).dosesLeft(UPDATED_DOSES_LEFT);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVaccine.getDose()).isEqualTo(UPDATED_DOSE);
        assertThat(testVaccine.getNextDoseDate()).isEqualTo(UPDATED_NEXT_DOSE_DATE);
        assertThat(testVaccine.getDosesLeft()).isEqualTo(UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void fullUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine.type(UPDATED_TYPE).dose(UPDATED_DOSE).nextDoseDate(UPDATED_NEXT_DOSE_DATE).dosesLeft(UPDATED_DOSES_LEFT);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVaccine.getDose()).isEqualTo(UPDATED_DOSE);
        assertThat(testVaccine.getNextDoseDate()).isEqualTo(UPDATED_NEXT_DOSE_DATE);
        assertThat(testVaccine.getDosesLeft()).isEqualTo(UPDATED_DOSES_LEFT);
    }

    @Test
    @Transactional
    void patchNonExistingVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vaccineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeDelete = vaccineRepository.findAll().size();

        // Delete the vaccine
        restVaccineMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
