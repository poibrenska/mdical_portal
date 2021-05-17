package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Hospital;
import com.medical.portal.repository.HospitalRepository;
import com.medical.portal.service.HospitalService;
import com.medical.portal.service.criteria.HospitalCriteria;
import com.medical.portal.service.dto.HospitalDTO;
import com.medical.portal.service.mapper.HospitalMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HospitalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HospitalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADMINISTRATION_PHONES = "AAAAAAAAAA";
    private static final String UPDATED_ADMINISTRATION_PHONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NOI = false;
    private static final Boolean UPDATED_NOI = true;

    private static final Long DEFAULT_DIRECTOR = 1L;
    private static final Long UPDATED_DIRECTOR = 2L;
    private static final Long SMALLER_DIRECTOR = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/hospitals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HospitalRepository hospitalRepository;

    @Mock
    private HospitalRepository hospitalRepositoryMock;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Mock
    private HospitalService hospitalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalMockMvc;

    private Hospital hospital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .administrationPhones(DEFAULT_ADMINISTRATION_PHONES)
            .noi(DEFAULT_NOI)
            .director(DEFAULT_DIRECTOR);
        return hospital;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospital createUpdatedEntity(EntityManager em) {
        Hospital hospital = new Hospital()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .administrationPhones(UPDATED_ADMINISTRATION_PHONES)
            .noi(UPDATED_NOI)
            .director(UPDATED_DIRECTOR);
        return hospital;
    }

    @BeforeEach
    public void initTest() {
        hospital = createEntity(em);
    }

    @Test
    @Transactional
    void createHospital() throws Exception {
        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();
        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isCreated());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate + 1);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHospital.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHospital.getAdministrationPhones()).isEqualTo(DEFAULT_ADMINISTRATION_PHONES);
        assertThat(testHospital.getNoi()).isEqualTo(DEFAULT_NOI);
        assertThat(testHospital.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
    }

    @Test
    @Transactional
    void createHospitalWithExistingId() throws Exception {
        // Create the Hospital with an existing ID
        hospital.setId(1L);
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        int databaseSizeBeforeCreate = hospitalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setName(null);

        // Create the Hospital, which fails.
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setAddress(null);

        // Create the Hospital, which fails.
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setCity(null);

        // Create the Hospital, which fails.
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoiIsRequired() throws Exception {
        int databaseSizeBeforeTest = hospitalRepository.findAll().size();
        // set the field null
        hospital.setNoi(null);

        // Create the Hospital, which fails.
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        restHospitalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isBadRequest());

        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHospitals() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].administrationPhones").value(hasItem(DEFAULT_ADMINISTRATION_PHONES)))
            .andExpect(jsonPath("$.[*].noi").value(hasItem(DEFAULT_NOI.booleanValue())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(hospitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hospitalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hospitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hospitalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get the hospital
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL_ID, hospital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospital.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.administrationPhones").value(DEFAULT_ADMINISTRATION_PHONES))
            .andExpect(jsonPath("$.noi").value(DEFAULT_NOI.booleanValue()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR.intValue()));
    }

    @Test
    @Transactional
    void getHospitalsByIdFiltering() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        Long id = hospital.getId();

        defaultHospitalShouldBeFound("id.equals=" + id);
        defaultHospitalShouldNotBeFound("id.notEquals=" + id);

        defaultHospitalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.greaterThan=" + id);

        defaultHospitalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHospitalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name equals to DEFAULT_NAME
        defaultHospitalShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name not equals to DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the hospitalList where name not equals to UPDATED_NAME
        defaultHospitalShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHospitalShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the hospitalList where name equals to UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name is not null
        defaultHospitalShouldBeFound("name.specified=true");

        // Get all the hospitalList where name is null
        defaultHospitalShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByNameContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name contains DEFAULT_NAME
        defaultHospitalShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the hospitalList where name contains UPDATED_NAME
        defaultHospitalShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where name does not contain DEFAULT_NAME
        defaultHospitalShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the hospitalList where name does not contain UPDATED_NAME
        defaultHospitalShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address equals to DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address not equals to DEFAULT_ADDRESS
        defaultHospitalShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address not equals to UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the hospitalList where address equals to UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address is not null
        defaultHospitalShouldBeFound("address.specified=true");

        // Get all the hospitalList where address is null
        defaultHospitalShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address contains DEFAULT_ADDRESS
        defaultHospitalShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address contains UPDATED_ADDRESS
        defaultHospitalShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where address does not contain DEFAULT_ADDRESS
        defaultHospitalShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the hospitalList where address does not contain UPDATED_ADDRESS
        defaultHospitalShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHospitalsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city equals to DEFAULT_CITY
        defaultHospitalShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the hospitalList where city equals to UPDATED_CITY
        defaultHospitalShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHospitalsByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city not equals to DEFAULT_CITY
        defaultHospitalShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the hospitalList where city not equals to UPDATED_CITY
        defaultHospitalShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHospitalsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city in DEFAULT_CITY or UPDATED_CITY
        defaultHospitalShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the hospitalList where city equals to UPDATED_CITY
        defaultHospitalShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHospitalsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city is not null
        defaultHospitalShouldBeFound("city.specified=true");

        // Get all the hospitalList where city is null
        defaultHospitalShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByCityContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city contains DEFAULT_CITY
        defaultHospitalShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the hospitalList where city contains UPDATED_CITY
        defaultHospitalShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHospitalsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where city does not contain DEFAULT_CITY
        defaultHospitalShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the hospitalList where city does not contain UPDATED_CITY
        defaultHospitalShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones equals to DEFAULT_ADMINISTRATION_PHONES
        defaultHospitalShouldBeFound("administrationPhones.equals=" + DEFAULT_ADMINISTRATION_PHONES);

        // Get all the hospitalList where administrationPhones equals to UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldNotBeFound("administrationPhones.equals=" + UPDATED_ADMINISTRATION_PHONES);
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones not equals to DEFAULT_ADMINISTRATION_PHONES
        defaultHospitalShouldNotBeFound("administrationPhones.notEquals=" + DEFAULT_ADMINISTRATION_PHONES);

        // Get all the hospitalList where administrationPhones not equals to UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldBeFound("administrationPhones.notEquals=" + UPDATED_ADMINISTRATION_PHONES);
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones in DEFAULT_ADMINISTRATION_PHONES or UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldBeFound("administrationPhones.in=" + DEFAULT_ADMINISTRATION_PHONES + "," + UPDATED_ADMINISTRATION_PHONES);

        // Get all the hospitalList where administrationPhones equals to UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldNotBeFound("administrationPhones.in=" + UPDATED_ADMINISTRATION_PHONES);
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones is not null
        defaultHospitalShouldBeFound("administrationPhones.specified=true");

        // Get all the hospitalList where administrationPhones is null
        defaultHospitalShouldNotBeFound("administrationPhones.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones contains DEFAULT_ADMINISTRATION_PHONES
        defaultHospitalShouldBeFound("administrationPhones.contains=" + DEFAULT_ADMINISTRATION_PHONES);

        // Get all the hospitalList where administrationPhones contains UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldNotBeFound("administrationPhones.contains=" + UPDATED_ADMINISTRATION_PHONES);
    }

    @Test
    @Transactional
    void getAllHospitalsByAdministrationPhonesNotContainsSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where administrationPhones does not contain DEFAULT_ADMINISTRATION_PHONES
        defaultHospitalShouldNotBeFound("administrationPhones.doesNotContain=" + DEFAULT_ADMINISTRATION_PHONES);

        // Get all the hospitalList where administrationPhones does not contain UPDATED_ADMINISTRATION_PHONES
        defaultHospitalShouldBeFound("administrationPhones.doesNotContain=" + UPDATED_ADMINISTRATION_PHONES);
    }

    @Test
    @Transactional
    void getAllHospitalsByNoiIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where noi equals to DEFAULT_NOI
        defaultHospitalShouldBeFound("noi.equals=" + DEFAULT_NOI);

        // Get all the hospitalList where noi equals to UPDATED_NOI
        defaultHospitalShouldNotBeFound("noi.equals=" + UPDATED_NOI);
    }

    @Test
    @Transactional
    void getAllHospitalsByNoiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where noi not equals to DEFAULT_NOI
        defaultHospitalShouldNotBeFound("noi.notEquals=" + DEFAULT_NOI);

        // Get all the hospitalList where noi not equals to UPDATED_NOI
        defaultHospitalShouldBeFound("noi.notEquals=" + UPDATED_NOI);
    }

    @Test
    @Transactional
    void getAllHospitalsByNoiIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where noi in DEFAULT_NOI or UPDATED_NOI
        defaultHospitalShouldBeFound("noi.in=" + DEFAULT_NOI + "," + UPDATED_NOI);

        // Get all the hospitalList where noi equals to UPDATED_NOI
        defaultHospitalShouldNotBeFound("noi.in=" + UPDATED_NOI);
    }

    @Test
    @Transactional
    void getAllHospitalsByNoiIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where noi is not null
        defaultHospitalShouldBeFound("noi.specified=true");

        // Get all the hospitalList where noi is null
        defaultHospitalShouldNotBeFound("noi.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director equals to DEFAULT_DIRECTOR
        defaultHospitalShouldBeFound("director.equals=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director equals to UPDATED_DIRECTOR
        defaultHospitalShouldNotBeFound("director.equals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director not equals to DEFAULT_DIRECTOR
        defaultHospitalShouldNotBeFound("director.notEquals=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director not equals to UPDATED_DIRECTOR
        defaultHospitalShouldBeFound("director.notEquals=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsInShouldWork() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director in DEFAULT_DIRECTOR or UPDATED_DIRECTOR
        defaultHospitalShouldBeFound("director.in=" + DEFAULT_DIRECTOR + "," + UPDATED_DIRECTOR);

        // Get all the hospitalList where director equals to UPDATED_DIRECTOR
        defaultHospitalShouldNotBeFound("director.in=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director is not null
        defaultHospitalShouldBeFound("director.specified=true");

        // Get all the hospitalList where director is null
        defaultHospitalShouldNotBeFound("director.specified=false");
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director is greater than or equal to DEFAULT_DIRECTOR
        defaultHospitalShouldBeFound("director.greaterThanOrEqual=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director is greater than or equal to UPDATED_DIRECTOR
        defaultHospitalShouldNotBeFound("director.greaterThanOrEqual=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director is less than or equal to DEFAULT_DIRECTOR
        defaultHospitalShouldBeFound("director.lessThanOrEqual=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director is less than or equal to SMALLER_DIRECTOR
        defaultHospitalShouldNotBeFound("director.lessThanOrEqual=" + SMALLER_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsLessThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director is less than DEFAULT_DIRECTOR
        defaultHospitalShouldNotBeFound("director.lessThan=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director is less than UPDATED_DIRECTOR
        defaultHospitalShouldBeFound("director.lessThan=" + UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDirectorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        // Get all the hospitalList where director is greater than DEFAULT_DIRECTOR
        defaultHospitalShouldNotBeFound("director.greaterThan=" + DEFAULT_DIRECTOR);

        // Get all the hospitalList where director is greater than SMALLER_DIRECTOR
        defaultHospitalShouldBeFound("director.greaterThan=" + SMALLER_DIRECTOR);
    }

    @Test
    @Transactional
    void getAllHospitalsByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        hospital.addDoctor(doctor);
        hospitalRepository.saveAndFlush(hospital);
        Long doctorId = doctor.getId();

        // Get all the hospitalList where doctor equals to doctorId
        defaultHospitalShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the hospitalList where doctor equals to (doctorId + 1)
        defaultHospitalShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHospitalShouldBeFound(String filter) throws Exception {
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospital.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].administrationPhones").value(hasItem(DEFAULT_ADMINISTRATION_PHONES)))
            .andExpect(jsonPath("$.[*].noi").value(hasItem(DEFAULT_NOI.booleanValue())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR.intValue())));

        // Check, that the count call also returns 1
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHospitalShouldNotBeFound(String filter) throws Exception {
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHospitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHospital() throws Exception {
        // Get the hospital
        restHospitalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital
        Hospital updatedHospital = hospitalRepository.findById(hospital.getId()).get();
        // Disconnect from session so that the updates on updatedHospital are not directly saved in db
        em.detach(updatedHospital);
        updatedHospital
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .administrationPhones(UPDATED_ADMINISTRATION_PHONES)
            .noi(UPDATED_NOI)
            .director(UPDATED_DIRECTOR);
        HospitalDTO hospitalDTO = hospitalMapper.toDto(updatedHospital);

        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHospital.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHospital.getAdministrationPhones()).isEqualTo(UPDATED_ADMINISTRATION_PHONES);
        assertThat(testHospital.getNoi()).isEqualTo(UPDATED_NOI);
        assertThat(testHospital.getDirector()).isEqualTo(UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void putNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hospitalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        partialUpdatedHospital.address(UPDATED_ADDRESS).administrationPhones(UPDATED_ADMINISTRATION_PHONES).noi(UPDATED_NOI);

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHospital.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHospital.getAdministrationPhones()).isEqualTo(UPDATED_ADMINISTRATION_PHONES);
        assertThat(testHospital.getNoi()).isEqualTo(UPDATED_NOI);
        assertThat(testHospital.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
    }

    @Test
    @Transactional
    void fullUpdateHospitalWithPatch() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();

        // Update the hospital using partial update
        Hospital partialUpdatedHospital = new Hospital();
        partialUpdatedHospital.setId(hospital.getId());

        partialUpdatedHospital
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .administrationPhones(UPDATED_ADMINISTRATION_PHONES)
            .noi(UPDATED_NOI)
            .director(UPDATED_DIRECTOR);

        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHospital))
            )
            .andExpect(status().isOk());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
        Hospital testHospital = hospitalList.get(hospitalList.size() - 1);
        assertThat(testHospital.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHospital.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHospital.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHospital.getAdministrationPhones()).isEqualTo(UPDATED_ADMINISTRATION_PHONES);
        assertThat(testHospital.getNoi()).isEqualTo(UPDATED_NOI);
        assertThat(testHospital.getDirector()).isEqualTo(UPDATED_DIRECTOR);
    }

    @Test
    @Transactional
    void patchNonExistingHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hospitalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHospital() throws Exception {
        int databaseSizeBeforeUpdate = hospitalRepository.findAll().size();
        hospital.setId(count.incrementAndGet());

        // Create the Hospital
        HospitalDTO hospitalDTO = hospitalMapper.toDto(hospital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hospitalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospital in the database
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHospital() throws Exception {
        // Initialize the database
        hospitalRepository.saveAndFlush(hospital);

        int databaseSizeBeforeDelete = hospitalRepository.findAll().size();

        // Delete the hospital
        restHospitalMockMvc
            .perform(delete(ENTITY_API_URL_ID, hospital.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hospital> hospitalList = hospitalRepository.findAll();
        assertThat(hospitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
