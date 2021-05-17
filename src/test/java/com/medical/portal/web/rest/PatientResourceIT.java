package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Appointment;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Documents;
import com.medical.portal.domain.ExaminationHistory;
import com.medical.portal.domain.Patient;
import com.medical.portal.domain.User;
import com.medical.portal.domain.Vaccine;
import com.medical.portal.repository.PatientRepository;
import com.medical.portal.service.criteria.PatientCriteria;
import com.medical.portal.service.dto.PatientDTO;
import com.medical.portal.service.mapper.PatientMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final String DEFAULT_FIRST_NAME = "BBB";
    private static final String UPDATED_FIRST_NAME = "CCC";

    private static final String DEFAULT_LAST_NAME = "DDDD";
    private static final String UPDATED_LAST_NAME = "FFFF";

    private static final String DEFAULT_ADDRESSTEXT = "BBBBCCC";
    private static final String UPDATED_ADDRESSTEXT = "CCCCCCCCC";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(2L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EGN = "8802026666";
    private static final String UPDATED_EGN = "8802026667";

    private static final String DEFAULT_PHONE = "088844";
    private static final String UPDATED_PHONE = "099933";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Long DEFAULT_GP = 2L;
    private static final Long UPDATED_GP = 3L;
    private static final Long SMALLER_GP = 2L - 2L;

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .addressText(DEFAULT_ADDRESSTEXT)
            .birthDate(DEFAULT_BIRTH_DATE)
            .egn(DEFAULT_EGN)
            .phone(DEFAULT_PHONE)
            .active(DEFAULT_ACTIVE)
            .gp(DEFAULT_GP);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .addressText(UPDATED_ADDRESSTEXT)
            .birthDate(UPDATED_BIRTH_DATE)
            .egn(UPDATED_EGN)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE)
            .gp(UPDATED_GP);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }


    @Test
    @Transactional
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatient.getAddressText()).isEqualTo(DEFAULT_ADDRESSTEXT);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatient.getEgn()).isEqualTo(DEFAULT_EGN);
        assertThat(testPatient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPatient.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPatient.getGp()).isEqualTo(DEFAULT_GP);
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);
        PatientDTO patientDTO = patientMapper.toDto(patient);

        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFirstName(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setLastName(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddresstextIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setAddressText(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEgnIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setEgn(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].addressText").value(hasItem(DEFAULT_ADDRESSTEXT)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].egn").value(hasItem(DEFAULT_EGN)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].gp").value(hasItem(DEFAULT_GP.intValue())));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.addressText").value(DEFAULT_ADDRESSTEXT))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.egn").value(DEFAULT_EGN))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.gp").value(DEFAULT_GP.intValue()));
    }

    @Test
    @Disabled
    @Transactional
    void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName equals to DEFAULT_FIRST_NAME
        defaultPatientShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName equals to UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName not equals to DEFAULT_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName not equals to UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the patientList where firstName equals to UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName is not null
        defaultPatientShouldBeFound("firstName.specified=true");

        // Get all the patientList where firstName is null
        defaultPatientShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName contains DEFAULT_FIRST_NAME
        defaultPatientShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName contains UPDATED_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where firstName does not contain DEFAULT_FIRST_NAME
        defaultPatientShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the patientList where firstName does not contain UPDATED_FIRST_NAME
        defaultPatientShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName equals to DEFAULT_LAST_NAME
        defaultPatientShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName equals to UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName not equals to DEFAULT_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName not equals to UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the patientList where lastName equals to UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName is not null
        defaultPatientShouldBeFound("lastName.specified=true");

        // Get all the patientList where lastName is null
        defaultPatientShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName contains DEFAULT_LAST_NAME
        defaultPatientShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName contains UPDATED_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where lastName does not contain DEFAULT_LAST_NAME
        defaultPatientShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the patientList where lastName does not contain UPDATED_LAST_NAME
        defaultPatientShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPatientsByAddressTextIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText equals to DEFAULT_ADDRESSTEXT
        defaultPatientShouldBeFound("addressText.equals=" + DEFAULT_ADDRESSTEXT);

        // Get all the patientList where addressText equals to UPDATED_ADDRESSTEXT
        defaultPatientShouldNotBeFound("addressText.equals=" + UPDATED_ADDRESSTEXT);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByAddressTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText not equals to DEFAULT_ADDRESSTEXT
        defaultPatientShouldNotBeFound("addressText.notEquals=" + DEFAULT_ADDRESSTEXT);

        // Get all the patientList where addressText not equals to UPDATED_ADDRESSTEXT
        defaultPatientShouldBeFound("addressText.notEquals=" + UPDATED_ADDRESSTEXT);
    }

    @Test
    @Transactional
    void getAllPatientsByAddressTextIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText in DEFAULT_ADDRESSTEXT or UPDATED_ADDRESSTEXT
        defaultPatientShouldBeFound("addressText.in=" + DEFAULT_ADDRESSTEXT + "," + UPDATED_ADDRESSTEXT);

        // Get all the patientList where addressText equals to UPDATED_ADDRESSTEXT
        defaultPatientShouldNotBeFound("addressText.in=" + UPDATED_ADDRESSTEXT);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByAddressTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText is not null
        defaultPatientShouldBeFound("addressText.specified=true");

        // Get all the patientList where addressText is null
        defaultPatientShouldNotBeFound("addressText.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByAddressTextContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText contains DEFAULT_ADDRESSTEXT
        defaultPatientShouldBeFound("addressText.contains=" + DEFAULT_ADDRESSTEXT);

        // Get all the patientList where addressText contains UPDATED_ADDRESSTEXT
        defaultPatientShouldNotBeFound("addressText.contains=" + UPDATED_ADDRESSTEXT);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByAddressTextNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where addressText does not contain DEFAULT_ADDRESSTEXT
        defaultPatientShouldNotBeFound("addressText.doesNotContain=" + DEFAULT_ADDRESSTEXT);

        // Get all the patientList where addressText does not contain UPDATED_ADDRESSTEXT
        defaultPatientShouldBeFound("addressText.doesNotContain=" + UPDATED_ADDRESSTEXT);
    }

    @Test
    @Transactional
    void getAllPatientsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPatientsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is not null
        defaultPatientShouldBeFound("birthDate.specified=true");

        // Get all the patientList where birthDate is null
        defaultPatientShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is less than UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPatientsByEgnIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn equals to DEFAULT_EGN
        defaultPatientShouldBeFound("egn.equals=" + DEFAULT_EGN);

        // Get all the patientList where egn equals to UPDATED_EGN
        defaultPatientShouldNotBeFound("egn.equals=" + UPDATED_EGN);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByEgnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn not equals to DEFAULT_EGN
        defaultPatientShouldNotBeFound("egn.notEquals=" + DEFAULT_EGN);

        // Get all the patientList where egn not equals to UPDATED_EGN
        defaultPatientShouldBeFound("egn.notEquals=" + UPDATED_EGN);
    }

    @Test
    @Transactional
    void getAllPatientsByEgnIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn in DEFAULT_EGN or UPDATED_EGN
        defaultPatientShouldBeFound("egn.in=" + DEFAULT_EGN + "," + UPDATED_EGN);

        // Get all the patientList where egn equals to UPDATED_EGN
        defaultPatientShouldNotBeFound("egn.in=" + UPDATED_EGN);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByEgnIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn is not null
        defaultPatientShouldBeFound("egn.specified=true");

        // Get all the patientList where egn is null
        defaultPatientShouldNotBeFound("egn.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByEgnContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn contains DEFAULT_EGN
        defaultPatientShouldBeFound("egn.contains=" + DEFAULT_EGN);

        // Get all the patientList where egn contains UPDATED_EGN
        defaultPatientShouldNotBeFound("egn.contains=" + UPDATED_EGN);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByEgnNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where egn does not contain DEFAULT_EGN
        defaultPatientShouldNotBeFound("egn.doesNotContain=" + DEFAULT_EGN);

        // Get all the patientList where egn does not contain UPDATED_EGN
        defaultPatientShouldBeFound("egn.doesNotContain=" + UPDATED_EGN);
    }

    @Test
    @Transactional
    void getAllPatientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone equals to DEFAULT_PHONE
        defaultPatientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone not equals to DEFAULT_PHONE
        defaultPatientShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the patientList where phone not equals to UPDATED_PHONE
        defaultPatientShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllPatientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPatientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone is not null
        defaultPatientShouldBeFound("phone.specified=true");

        // Get all the patientList where phone is null
        defaultPatientShouldNotBeFound("phone.specified=false");
    }


    @Test
    @Disabled
    @Transactional
    void getAllPatientsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where active equals to DEFAULT_ACTIVE
        defaultPatientShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the patientList where active equals to UPDATED_ACTIVE
        defaultPatientShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where active not equals to DEFAULT_ACTIVE
        defaultPatientShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the patientList where active not equals to UPDATED_ACTIVE
        defaultPatientShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPatientShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the patientList where active equals to UPDATED_ACTIVE
        defaultPatientShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where active is not null
        defaultPatientShouldBeFound("active.specified=true");

        // Get all the patientList where active is null
        defaultPatientShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByGpIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp equals to DEFAULT_GP
        defaultPatientShouldBeFound("gp.equals=" + DEFAULT_GP);

        // Get all the patientList where gp equals to UPDATED_GP
        defaultPatientShouldNotBeFound("gp.equals=" + UPDATED_GP);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByGpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp not equals to DEFAULT_GP
        defaultPatientShouldNotBeFound("gp.notEquals=" + DEFAULT_GP);

        // Get all the patientList where gp not equals to UPDATED_GP
        defaultPatientShouldBeFound("gp.notEquals=" + UPDATED_GP);
    }

    @Test
    @Transactional
    void getAllPatientsByGpIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp in DEFAULT_GP or UPDATED_GP
        defaultPatientShouldBeFound("gp.in=" + DEFAULT_GP + "," + UPDATED_GP);

        // Get all the patientList where gp equals to UPDATED_GP
        defaultPatientShouldNotBeFound("gp.in=" + UPDATED_GP);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByGpIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp is not null
        defaultPatientShouldBeFound("gp.specified=true");

        // Get all the patientList where gp is null
        defaultPatientShouldNotBeFound("gp.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientsByGpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp is greater than or equal to DEFAULT_GP
        defaultPatientShouldBeFound("gp.greaterThanOrEqual=" + DEFAULT_GP);

        // Get all the patientList where gp is greater than or equal to UPDATED_GP
        defaultPatientShouldNotBeFound("gp.greaterThanOrEqual=" + UPDATED_GP);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByGpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp is less than or equal to DEFAULT_GP
        defaultPatientShouldBeFound("gp.lessThanOrEqual=" + DEFAULT_GP);

        // Get all the patientList where gp is less than or equal to SMALLER_GP
        defaultPatientShouldNotBeFound("gp.lessThanOrEqual=" + SMALLER_GP);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByGpIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp is less than DEFAULT_GP
        defaultPatientShouldNotBeFound("gp.lessThan=" + DEFAULT_GP);

        // Get all the patientList where gp is less than UPDATED_GP
        defaultPatientShouldBeFound("gp.lessThan=" + UPDATED_GP);
    }

    @Test
    @Disabled
    @Transactional
    void getAllPatientsByGpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gp is greater than DEFAULT_GP
        defaultPatientShouldNotBeFound("gp.greaterThan=" + DEFAULT_GP);

        // Get all the patientList where gp is greater than SMALLER_GP
        defaultPatientShouldBeFound("gp.greaterThan=" + SMALLER_GP);
    }

    @Test
    @Transactional
    void getAllPatientsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        patient.setUser(user);
        patientRepository.saveAndFlush(patient);
        Long userId = user.getId();

        // Get all the patientList where user equals to userId
        defaultPatientShouldBeFound("userId.equals=" + userId);

        // Get all the patientList where user equals to (userId + 1)
        defaultPatientShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Documents documents = DocumentsResourceIT.createEntity(em);
        em.persist(documents);
        em.flush();
        patient.addDocuments(documents);
        patientRepository.saveAndFlush(patient);
        Long documentsId = documents.getId();

        // Get all the patientList where documents equals to documentsId
        defaultPatientShouldBeFound("documentsId.equals=" + documentsId);

        // Get all the patientList where documents equals to (documentsId + 1)
        defaultPatientShouldNotBeFound("documentsId.equals=" + (documentsId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByAppointmentIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Appointment appointment = AppointmentResourceIT.createEntity(em);
        em.persist(appointment);
        em.flush();
        patient.addAppointment(appointment);
        patientRepository.saveAndFlush(patient);
        Long appointmentId = appointment.getId();

        // Get all the patientList where appointment equals to appointmentId
        defaultPatientShouldBeFound("appointmentId.equals=" + appointmentId);

        // Get all the patientList where appointment equals to (appointmentId + 1)
        defaultPatientShouldNotBeFound("appointmentId.equals=" + (appointmentId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByExaminationHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        ExaminationHistory examinationHistory = ExaminationHistoryResourceIT.createEntity(em);
        em.persist(examinationHistory);
        em.flush();
        patient.addExaminationHistory(examinationHistory);
        patientRepository.saveAndFlush(patient);
        Long examinationHistoryId = examinationHistory.getId();

        // Get all the patientList where examinationHistory equals to examinationHistoryId
        defaultPatientShouldBeFound("examinationHistoryId.equals=" + examinationHistoryId);

        // Get all the patientList where examinationHistory equals to (examinationHistoryId + 1)
        defaultPatientShouldNotBeFound("examinationHistoryId.equals=" + (examinationHistoryId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        patient.addDoctor(doctor);
        patientRepository.saveAndFlush(patient);
        Long doctorId = doctor.getId();

        // Get all the patientList where doctor equals to doctorId
        defaultPatientShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the patientList where doctor equals to (doctorId + 1)
        defaultPatientShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllPatientsByVaccineIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Vaccine vaccine = VaccineResourceIT.createEntity(em);
        em.persist(vaccine);
        em.flush();
        patient.addVaccine(vaccine);
        patientRepository.saveAndFlush(patient);
        Long vaccineId = vaccine.getId();

        // Get all the patientList where vaccine equals to vaccineId
        defaultPatientShouldBeFound("vaccineId.equals=" + vaccineId);

        // Get all the patientList where vaccine equals to (vaccineId + 1)
        defaultPatientShouldNotBeFound("vaccineId.equals=" + (vaccineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].addressText").value(hasItem(DEFAULT_ADDRESSTEXT)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].egn").value(hasItem(DEFAULT_EGN)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].gp").value(hasItem(DEFAULT_GP.intValue())));

        // Check, that the count call also returns 1
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .addressText(UPDATED_ADDRESSTEXT)
            .birthDate(UPDATED_BIRTH_DATE)
            .egn(UPDATED_EGN)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE)
            .gp(UPDATED_GP);
        PatientDTO patientDTO = patientMapper.toDto(updatedPatient);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatient.getAddressText()).isEqualTo(UPDATED_ADDRESSTEXT);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatient.getEgn()).isEqualTo(UPDATED_EGN);
        assertThat(testPatient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGp()).isEqualTo(UPDATED_GP);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient.firstName(UPDATED_FIRST_NAME).active(UPDATED_ACTIVE).gp(UPDATED_GP);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatient.getAddressText()).isEqualTo(DEFAULT_ADDRESSTEXT);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatient.getEgn()).isEqualTo(DEFAULT_EGN);
        assertThat(testPatient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGp()).isEqualTo(UPDATED_GP);
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .addressText(UPDATED_ADDRESSTEXT)
            .birthDate(UPDATED_BIRTH_DATE)
            .egn(UPDATED_EGN)
            .phone(UPDATED_PHONE)
            .active(UPDATED_ACTIVE)
            .gp(UPDATED_GP);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatient.getAddressText()).isEqualTo(UPDATED_ADDRESSTEXT);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatient.getEgn()).isEqualTo(UPDATED_EGN);
        assertThat(testPatient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPatient.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPatient.getGp()).isEqualTo(UPDATED_GP);
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
