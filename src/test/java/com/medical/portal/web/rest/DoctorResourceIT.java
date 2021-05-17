package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Appointment;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Documents;
import com.medical.portal.domain.ExaminationHistory;
import com.medical.portal.domain.Hospital;
import com.medical.portal.domain.Patient;
import com.medical.portal.domain.Vaccine;
import com.medical.portal.repository.DoctorRepository;
import com.medical.portal.service.DoctorService;
import com.medical.portal.service.dto.DoctorDTO;
import com.medical.portal.service.mapper.DoctorMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DoctorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALIZATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIZATION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_META = "AAAAAAAAAA";
    private static final String UPDATED_META = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/doctors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Autowired
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorService doctorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .specialization(DEFAULT_SPECIALIZATION)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO)
            .meta(DEFAULT_META);
        return doctor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .specialization(UPDATED_SPECIALIZATION)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .meta(UPDATED_META);
        return doctor;
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();
        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoctor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDoctor.getSpecialization()).isEqualTo(DEFAULT_SPECIALIZATION);
        assertThat(testDoctor.getAdditionalInfo()).isEqualTo(DEFAULT_ADDITIONAL_INFO);
        assertThat(testDoctor.getMeta()).isEqualTo(DEFAULT_META);
    }

    @Test
    @Transactional
    void createDoctorWithExistingId() throws Exception {
        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setFirstName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorRepository.findAll().size();
        // set the field null
        doctor.setLastName(null);

        // Create the Doctor, which fails.
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        restDoctorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(doctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doctorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(doctorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doctorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL_ID, doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO))
            .andExpect(jsonPath("$.meta").value(DEFAULT_META));
    }

    @Test
    @Transactional
    void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorShouldBeFound("id.equals=" + id);
        defaultDoctorShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName equals to DEFAULT_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName not equals to DEFAULT_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName not equals to UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the doctorList where firstName equals to UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName is not null
        defaultDoctorShouldBeFound("firstName.specified=true");

        // Get all the doctorList where firstName is null
        defaultDoctorShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName contains DEFAULT_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName contains UPDATED_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where firstName does not contain DEFAULT_FIRST_NAME
        defaultDoctorShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the doctorList where firstName does not contain UPDATED_FIRST_NAME
        defaultDoctorShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName equals to DEFAULT_LAST_NAME
        defaultDoctorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName not equals to DEFAULT_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName not equals to UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the doctorList where lastName equals to UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName is not null
        defaultDoctorShouldBeFound("lastName.specified=true");

        // Get all the doctorList where lastName is null
        defaultDoctorShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName contains DEFAULT_LAST_NAME
        defaultDoctorShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName contains UPDATED_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where lastName does not contain DEFAULT_LAST_NAME
        defaultDoctorShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the doctorList where lastName does not contain UPDATED_LAST_NAME
        defaultDoctorShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email equals to DEFAULT_EMAIL
        defaultDoctorShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the doctorList where email equals to UPDATED_EMAIL
        defaultDoctorShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email not equals to DEFAULT_EMAIL
        defaultDoctorShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the doctorList where email not equals to UPDATED_EMAIL
        defaultDoctorShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultDoctorShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the doctorList where email equals to UPDATED_EMAIL
        defaultDoctorShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email is not null
        defaultDoctorShouldBeFound("email.specified=true");

        // Get all the doctorList where email is null
        defaultDoctorShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email contains DEFAULT_EMAIL
        defaultDoctorShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the doctorList where email contains UPDATED_EMAIL
        defaultDoctorShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllDoctorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where email does not contain DEFAULT_EMAIL
        defaultDoctorShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the doctorList where email does not contain UPDATED_EMAIL
        defaultDoctorShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone equals to DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone not equals to DEFAULT_PHONE
        defaultDoctorShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone not equals to UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone is not null
        defaultDoctorShouldBeFound("phone.specified=true");

        // Get all the doctorList where phone is null
        defaultDoctorShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone contains DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the doctorList where phone contains UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone does not contain DEFAULT_PHONE
        defaultDoctorShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the doctorList where phone does not contain UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization equals to DEFAULT_SPECIALIZATION
        defaultDoctorShouldBeFound("specialization.equals=" + DEFAULT_SPECIALIZATION);

        // Get all the doctorList where specialization equals to UPDATED_SPECIALIZATION
        defaultDoctorShouldNotBeFound("specialization.equals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization not equals to DEFAULT_SPECIALIZATION
        defaultDoctorShouldNotBeFound("specialization.notEquals=" + DEFAULT_SPECIALIZATION);

        // Get all the doctorList where specialization not equals to UPDATED_SPECIALIZATION
        defaultDoctorShouldBeFound("specialization.notEquals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization in DEFAULT_SPECIALIZATION or UPDATED_SPECIALIZATION
        defaultDoctorShouldBeFound("specialization.in=" + DEFAULT_SPECIALIZATION + "," + UPDATED_SPECIALIZATION);

        // Get all the doctorList where specialization equals to UPDATED_SPECIALIZATION
        defaultDoctorShouldNotBeFound("specialization.in=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization is not null
        defaultDoctorShouldBeFound("specialization.specified=true");

        // Get all the doctorList where specialization is null
        defaultDoctorShouldNotBeFound("specialization.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization contains DEFAULT_SPECIALIZATION
        defaultDoctorShouldBeFound("specialization.contains=" + DEFAULT_SPECIALIZATION);

        // Get all the doctorList where specialization contains UPDATED_SPECIALIZATION
        defaultDoctorShouldNotBeFound("specialization.contains=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsBySpecializationNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where specialization does not contain DEFAULT_SPECIALIZATION
        defaultDoctorShouldNotBeFound("specialization.doesNotContain=" + DEFAULT_SPECIALIZATION);

        // Get all the doctorList where specialization does not contain UPDATED_SPECIALIZATION
        defaultDoctorShouldBeFound("specialization.doesNotContain=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo equals to DEFAULT_ADDITIONAL_INFO
        defaultDoctorShouldBeFound("additionalInfo.equals=" + DEFAULT_ADDITIONAL_INFO);

        // Get all the doctorList where additionalInfo equals to UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldNotBeFound("additionalInfo.equals=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo not equals to DEFAULT_ADDITIONAL_INFO
        defaultDoctorShouldNotBeFound("additionalInfo.notEquals=" + DEFAULT_ADDITIONAL_INFO);

        // Get all the doctorList where additionalInfo not equals to UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldBeFound("additionalInfo.notEquals=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo in DEFAULT_ADDITIONAL_INFO or UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldBeFound("additionalInfo.in=" + DEFAULT_ADDITIONAL_INFO + "," + UPDATED_ADDITIONAL_INFO);

        // Get all the doctorList where additionalInfo equals to UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldNotBeFound("additionalInfo.in=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo is not null
        defaultDoctorShouldBeFound("additionalInfo.specified=true");

        // Get all the doctorList where additionalInfo is null
        defaultDoctorShouldNotBeFound("additionalInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo contains DEFAULT_ADDITIONAL_INFO
        defaultDoctorShouldBeFound("additionalInfo.contains=" + DEFAULT_ADDITIONAL_INFO);

        // Get all the doctorList where additionalInfo contains UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldNotBeFound("additionalInfo.contains=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllDoctorsByAdditionalInfoNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where additionalInfo does not contain DEFAULT_ADDITIONAL_INFO
        defaultDoctorShouldNotBeFound("additionalInfo.doesNotContain=" + DEFAULT_ADDITIONAL_INFO);

        // Get all the doctorList where additionalInfo does not contain UPDATED_ADDITIONAL_INFO
        defaultDoctorShouldBeFound("additionalInfo.doesNotContain=" + UPDATED_ADDITIONAL_INFO);
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta equals to DEFAULT_META
        defaultDoctorShouldBeFound("meta.equals=" + DEFAULT_META);

        // Get all the doctorList where meta equals to UPDATED_META
        defaultDoctorShouldNotBeFound("meta.equals=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta not equals to DEFAULT_META
        defaultDoctorShouldNotBeFound("meta.notEquals=" + DEFAULT_META);

        // Get all the doctorList where meta not equals to UPDATED_META
        defaultDoctorShouldBeFound("meta.notEquals=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta in DEFAULT_META or UPDATED_META
        defaultDoctorShouldBeFound("meta.in=" + DEFAULT_META + "," + UPDATED_META);

        // Get all the doctorList where meta equals to UPDATED_META
        defaultDoctorShouldNotBeFound("meta.in=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta is not null
        defaultDoctorShouldBeFound("meta.specified=true");

        // Get all the doctorList where meta is null
        defaultDoctorShouldNotBeFound("meta.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta contains DEFAULT_META
        defaultDoctorShouldBeFound("meta.contains=" + DEFAULT_META);

        // Get all the doctorList where meta contains UPDATED_META
        defaultDoctorShouldNotBeFound("meta.contains=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDoctorsByMetaNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where meta does not contain DEFAULT_META
        defaultDoctorShouldNotBeFound("meta.doesNotContain=" + DEFAULT_META);

        // Get all the doctorList where meta does not contain UPDATED_META
        defaultDoctorShouldBeFound("meta.doesNotContain=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDoctorsByDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Documents documents = DocumentsResourceIT.createEntity(em);
        em.persist(documents);
        em.flush();
        doctor.addDocuments(documents);
        doctorRepository.saveAndFlush(doctor);
        Long documentsId = documents.getId();

        // Get all the doctorList where documents equals to documentsId
        defaultDoctorShouldBeFound("documentsId.equals=" + documentsId);

        // Get all the doctorList where documents equals to (documentsId + 1)
        defaultDoctorShouldNotBeFound("documentsId.equals=" + (documentsId + 1));
    }

    @Test
    @Transactional
    void getAllDoctorsByAppointmentIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Appointment appointment = AppointmentResourceIT.createEntity(em);
        em.persist(appointment);
        em.flush();
        doctor.addAppointment(appointment);
        doctorRepository.saveAndFlush(doctor);
        Long appointmentId = appointment.getId();

        // Get all the doctorList where appointment equals to appointmentId
        defaultDoctorShouldBeFound("appointmentId.equals=" + appointmentId);

        // Get all the doctorList where appointment equals to (appointmentId + 1)
        defaultDoctorShouldNotBeFound("appointmentId.equals=" + (appointmentId + 1));
    }

    @Test
    @Transactional
    void getAllDoctorsByExaminationHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        ExaminationHistory examinationHistory = ExaminationHistoryResourceIT.createEntity(em);
        em.persist(examinationHistory);
        em.flush();
        doctor.addExaminationHistory(examinationHistory);
        doctorRepository.saveAndFlush(doctor);
        Long examinationHistoryId = examinationHistory.getId();

        // Get all the doctorList where examinationHistory equals to examinationHistoryId
        defaultDoctorShouldBeFound("examinationHistoryId.equals=" + examinationHistoryId);

        // Get all the doctorList where examinationHistory equals to (examinationHistoryId + 1)
        defaultDoctorShouldNotBeFound("examinationHistoryId.equals=" + (examinationHistoryId + 1));
    }

    @Test
    @Transactional
    void getAllDoctorsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        doctor.addPatient(patient);
        doctorRepository.saveAndFlush(doctor);
        Long patientId = patient.getId();

        // Get all the doctorList where patient equals to patientId
        defaultDoctorShouldBeFound("patientId.equals=" + patientId);

        // Get all the doctorList where patient equals to (patientId + 1)
        defaultDoctorShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    @Test
    @Transactional
    void getAllDoctorsByHospitalIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Hospital hospital = HospitalResourceIT.createEntity(em);
        em.persist(hospital);
        em.flush();
        doctor.addHospital(hospital);
        doctorRepository.saveAndFlush(doctor);
        Long hospitalId = hospital.getId();

        // Get all the doctorList where hospital equals to hospitalId
        defaultDoctorShouldBeFound("hospitalId.equals=" + hospitalId);

        // Get all the doctorList where hospital equals to (hospitalId + 1)
        defaultDoctorShouldNotBeFound("hospitalId.equals=" + (hospitalId + 1));
    }

    @Test
    @Transactional
    void getAllDoctorsByVaccineIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Vaccine vaccine = VaccineResourceIT.createEntity(em);
        em.persist(vaccine);
        em.flush();
        doctor.addVaccine(vaccine);
        doctorRepository.saveAndFlush(doctor);
        Long vaccineId = vaccine.getId();

        // Get all the doctorList where vaccine equals to vaccineId
        defaultDoctorShouldBeFound("vaccineId.equals=" + vaccineId);

        // Get all the doctorList where vaccine equals to (vaccineId + 1)
        defaultDoctorShouldNotBeFound("vaccineId.equals=" + (vaccineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META)));

        // Check, that the count call also returns 1
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .specialization(UPDATED_SPECIALIZATION)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .meta(UPDATED_META);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
        assertThat(testDoctor.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testDoctor.getMeta()).isEqualTo(UPDATED_META);
    }

    @Test
    @Transactional
    void putNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phone(UPDATED_PHONE)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .meta(UPDATED_META);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getSpecialization()).isEqualTo(DEFAULT_SPECIALIZATION);
        assertThat(testDoctor.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testDoctor.getMeta()).isEqualTo(UPDATED_META);
    }

    @Test
    @Transactional
    void fullUpdateDoctorWithPatch() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor using partial update
        Doctor partialUpdatedDoctor = new Doctor();
        partialUpdatedDoctor.setId(doctor.getId());

        partialUpdatedDoctor
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .specialization(UPDATED_SPECIALIZATION)
            .additionalInfo(UPDATED_ADDITIONAL_INFO)
            .meta(UPDATED_META);

        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctor))
            )
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDoctor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDoctor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
        assertThat(testDoctor.getAdditionalInfo()).isEqualTo(UPDATED_ADDITIONAL_INFO);
        assertThat(testDoctor.getMeta()).isEqualTo(UPDATED_META);
    }

    @Test
    @Transactional
    void patchNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();
        doctor.setId(count.incrementAndGet());

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doctorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Delete the doctor
        restDoctorMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
