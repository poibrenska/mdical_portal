package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.VaccineType;
import com.medical.portal.domain.enumeration.CalendarUnit;
import com.medical.portal.repository.VaccineTypeRepository;
import com.medical.portal.service.criteria.VaccineTypeCriteria;
import com.medical.portal.service.dto.VaccineTypeDTO;
import com.medical.portal.service.mapper.VaccineTypeMapper;
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
 * Integration tests for the {@link VaccineTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccineTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOSES = "AAAAAAAAAA";
    private static final String UPDATED_DOSES = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION_BETWEEN_DOSES_TIME = 1;
    private static final Integer UPDATED_DURATION_BETWEEN_DOSES_TIME = 2;
    private static final Integer SMALLER_DURATION_BETWEEN_DOSES_TIME = 1 - 1;

    private static final CalendarUnit DEFAULT_DURATION_BETWEEN_DOSES_UNIT = CalendarUnit.SECONDS;
    private static final CalendarUnit UPDATED_DURATION_BETWEEN_DOSES_UNIT = CalendarUnit.MINUTES;

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vaccine-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccineTypeRepository vaccineTypeRepository;

    @Autowired
    private VaccineTypeMapper vaccineTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccineTypeMockMvc;

    private VaccineType vaccineType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccineType createEntity(EntityManager em) {
        VaccineType vaccineType = new VaccineType()
            .name(DEFAULT_NAME)
            .doses(DEFAULT_DOSES)
            .durationBetweenDosesTime(DEFAULT_DURATION_BETWEEN_DOSES_TIME)
            .durationBetweenDosesUnit(DEFAULT_DURATION_BETWEEN_DOSES_UNIT)
            .manufacturer(DEFAULT_MANUFACTURER);
        return vaccineType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccineType createUpdatedEntity(EntityManager em) {
        VaccineType vaccineType = new VaccineType()
            .name(UPDATED_NAME)
            .doses(UPDATED_DOSES)
            .durationBetweenDosesTime(UPDATED_DURATION_BETWEEN_DOSES_TIME)
            .durationBetweenDosesUnit(UPDATED_DURATION_BETWEEN_DOSES_UNIT)
            .manufacturer(UPDATED_MANUFACTURER);
        return vaccineType;
    }

    @BeforeEach
    public void initTest() {
        vaccineType = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccineType() throws Exception {
        int databaseSizeBeforeCreate = vaccineTypeRepository.findAll().size();
        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);
        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeCreate + 1);
        VaccineType testVaccineType = vaccineTypeList.get(vaccineTypeList.size() - 1);
        assertThat(testVaccineType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccineType.getDoses()).isEqualTo(DEFAULT_DOSES);
        assertThat(testVaccineType.getDurationBetweenDosesTime()).isEqualTo(DEFAULT_DURATION_BETWEEN_DOSES_TIME);
        assertThat(testVaccineType.getDurationBetweenDosesUnit()).isEqualTo(DEFAULT_DURATION_BETWEEN_DOSES_UNIT);
        assertThat(testVaccineType.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
    }

    @Test
    @Transactional
    void createVaccineTypeWithExistingId() throws Exception {
        // Create the VaccineType with an existing ID
        vaccineType.setId(1L);
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        int databaseSizeBeforeCreate = vaccineTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineTypeRepository.findAll().size();
        // set the field null
        vaccineType.setName(null);

        // Create the VaccineType, which fails.
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDosesIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineTypeRepository.findAll().size();
        // set the field null
        vaccineType.setDoses(null);

        // Create the VaccineType, which fails.
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationBetweenDosesTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineTypeRepository.findAll().size();
        // set the field null
        vaccineType.setDurationBetweenDosesTime(null);

        // Create the VaccineType, which fails.
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationBetweenDosesUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineTypeRepository.findAll().size();
        // set the field null
        vaccineType.setDurationBetweenDosesUnit(null);

        // Create the VaccineType, which fails.
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        restVaccineTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccineTypes() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccineType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].doses").value(hasItem(DEFAULT_DOSES)))
            .andExpect(jsonPath("$.[*].durationBetweenDosesTime").value(hasItem(DEFAULT_DURATION_BETWEEN_DOSES_TIME)))
            .andExpect(jsonPath("$.[*].durationBetweenDosesUnit").value(hasItem(DEFAULT_DURATION_BETWEEN_DOSES_UNIT.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)));
    }

    @Test
    @Transactional
    void getVaccineType() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get the vaccineType
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccineType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccineType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.doses").value(DEFAULT_DOSES))
            .andExpect(jsonPath("$.durationBetweenDosesTime").value(DEFAULT_DURATION_BETWEEN_DOSES_TIME))
            .andExpect(jsonPath("$.durationBetweenDosesUnit").value(DEFAULT_DURATION_BETWEEN_DOSES_UNIT.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER));
    }

    @Test
    @Transactional
    void getVaccineTypesByIdFiltering() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        Long id = vaccineType.getId();

        defaultVaccineTypeShouldBeFound("id.equals=" + id);
        defaultVaccineTypeShouldNotBeFound("id.notEquals=" + id);

        defaultVaccineTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVaccineTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultVaccineTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVaccineTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name equals to DEFAULT_NAME
        defaultVaccineTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vaccineTypeList where name equals to UPDATED_NAME
        defaultVaccineTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name not equals to DEFAULT_NAME
        defaultVaccineTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the vaccineTypeList where name not equals to UPDATED_NAME
        defaultVaccineTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVaccineTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vaccineTypeList where name equals to UPDATED_NAME
        defaultVaccineTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name is not null
        defaultVaccineTypeShouldBeFound("name.specified=true");

        // Get all the vaccineTypeList where name is null
        defaultVaccineTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name contains DEFAULT_NAME
        defaultVaccineTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vaccineTypeList where name contains UPDATED_NAME
        defaultVaccineTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where name does not contain DEFAULT_NAME
        defaultVaccineTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vaccineTypeList where name does not contain UPDATED_NAME
        defaultVaccineTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses equals to DEFAULT_DOSES
        defaultVaccineTypeShouldBeFound("doses.equals=" + DEFAULT_DOSES);

        // Get all the vaccineTypeList where doses equals to UPDATED_DOSES
        defaultVaccineTypeShouldNotBeFound("doses.equals=" + UPDATED_DOSES);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses not equals to DEFAULT_DOSES
        defaultVaccineTypeShouldNotBeFound("doses.notEquals=" + DEFAULT_DOSES);

        // Get all the vaccineTypeList where doses not equals to UPDATED_DOSES
        defaultVaccineTypeShouldBeFound("doses.notEquals=" + UPDATED_DOSES);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses in DEFAULT_DOSES or UPDATED_DOSES
        defaultVaccineTypeShouldBeFound("doses.in=" + DEFAULT_DOSES + "," + UPDATED_DOSES);

        // Get all the vaccineTypeList where doses equals to UPDATED_DOSES
        defaultVaccineTypeShouldNotBeFound("doses.in=" + UPDATED_DOSES);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses is not null
        defaultVaccineTypeShouldBeFound("doses.specified=true");

        // Get all the vaccineTypeList where doses is null
        defaultVaccineTypeShouldNotBeFound("doses.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses contains DEFAULT_DOSES
        defaultVaccineTypeShouldBeFound("doses.contains=" + DEFAULT_DOSES);

        // Get all the vaccineTypeList where doses contains UPDATED_DOSES
        defaultVaccineTypeShouldNotBeFound("doses.contains=" + UPDATED_DOSES);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDosesNotContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where doses does not contain DEFAULT_DOSES
        defaultVaccineTypeShouldNotBeFound("doses.doesNotContain=" + DEFAULT_DOSES);

        // Get all the vaccineTypeList where doses does not contain UPDATED_DOSES
        defaultVaccineTypeShouldBeFound("doses.doesNotContain=" + UPDATED_DOSES);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime equals to DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.equals=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime equals to UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.equals=" + UPDATED_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime not equals to DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.notEquals=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime not equals to UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.notEquals=" + UPDATED_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime in DEFAULT_DURATION_BETWEEN_DOSES_TIME or UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound(
            "durationBetweenDosesTime.in=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME + "," + UPDATED_DURATION_BETWEEN_DOSES_TIME
        );

        // Get all the vaccineTypeList where durationBetweenDosesTime equals to UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.in=" + UPDATED_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime is not null
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.specified=true");

        // Get all the vaccineTypeList where durationBetweenDosesTime is null
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime is greater than or equal to DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.greaterThanOrEqual=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime is greater than or equal to UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.greaterThanOrEqual=" + UPDATED_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime is less than or equal to DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.lessThanOrEqual=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime is less than or equal to SMALLER_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.lessThanOrEqual=" + SMALLER_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime is less than DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.lessThan=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime is less than UPDATED_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.lessThan=" + UPDATED_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesTime is greater than DEFAULT_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesTime.greaterThan=" + DEFAULT_DURATION_BETWEEN_DOSES_TIME);

        // Get all the vaccineTypeList where durationBetweenDosesTime is greater than SMALLER_DURATION_BETWEEN_DOSES_TIME
        defaultVaccineTypeShouldBeFound("durationBetweenDosesTime.greaterThan=" + SMALLER_DURATION_BETWEEN_DOSES_TIME);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesUnit equals to DEFAULT_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldBeFound("durationBetweenDosesUnit.equals=" + DEFAULT_DURATION_BETWEEN_DOSES_UNIT);

        // Get all the vaccineTypeList where durationBetweenDosesUnit equals to UPDATED_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesUnit.equals=" + UPDATED_DURATION_BETWEEN_DOSES_UNIT);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesUnit not equals to DEFAULT_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesUnit.notEquals=" + DEFAULT_DURATION_BETWEEN_DOSES_UNIT);

        // Get all the vaccineTypeList where durationBetweenDosesUnit not equals to UPDATED_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldBeFound("durationBetweenDosesUnit.notEquals=" + UPDATED_DURATION_BETWEEN_DOSES_UNIT);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesUnitIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesUnit in DEFAULT_DURATION_BETWEEN_DOSES_UNIT or UPDATED_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldBeFound(
            "durationBetweenDosesUnit.in=" + DEFAULT_DURATION_BETWEEN_DOSES_UNIT + "," + UPDATED_DURATION_BETWEEN_DOSES_UNIT
        );

        // Get all the vaccineTypeList where durationBetweenDosesUnit equals to UPDATED_DURATION_BETWEEN_DOSES_UNIT
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesUnit.in=" + UPDATED_DURATION_BETWEEN_DOSES_UNIT);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByDurationBetweenDosesUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where durationBetweenDosesUnit is not null
        defaultVaccineTypeShouldBeFound("durationBetweenDosesUnit.specified=true");

        // Get all the vaccineTypeList where durationBetweenDosesUnit is null
        defaultVaccineTypeShouldNotBeFound("durationBetweenDosesUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer equals to DEFAULT_MANUFACTURER
        defaultVaccineTypeShouldBeFound("manufacturer.equals=" + DEFAULT_MANUFACTURER);

        // Get all the vaccineTypeList where manufacturer equals to UPDATED_MANUFACTURER
        defaultVaccineTypeShouldNotBeFound("manufacturer.equals=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer not equals to DEFAULT_MANUFACTURER
        defaultVaccineTypeShouldNotBeFound("manufacturer.notEquals=" + DEFAULT_MANUFACTURER);

        // Get all the vaccineTypeList where manufacturer not equals to UPDATED_MANUFACTURER
        defaultVaccineTypeShouldBeFound("manufacturer.notEquals=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerIsInShouldWork() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer in DEFAULT_MANUFACTURER or UPDATED_MANUFACTURER
        defaultVaccineTypeShouldBeFound("manufacturer.in=" + DEFAULT_MANUFACTURER + "," + UPDATED_MANUFACTURER);

        // Get all the vaccineTypeList where manufacturer equals to UPDATED_MANUFACTURER
        defaultVaccineTypeShouldNotBeFound("manufacturer.in=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer is not null
        defaultVaccineTypeShouldBeFound("manufacturer.specified=true");

        // Get all the vaccineTypeList where manufacturer is null
        defaultVaccineTypeShouldNotBeFound("manufacturer.specified=false");
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer contains DEFAULT_MANUFACTURER
        defaultVaccineTypeShouldBeFound("manufacturer.contains=" + DEFAULT_MANUFACTURER);

        // Get all the vaccineTypeList where manufacturer contains UPDATED_MANUFACTURER
        defaultVaccineTypeShouldNotBeFound("manufacturer.contains=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void getAllVaccineTypesByManufacturerNotContainsSomething() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        // Get all the vaccineTypeList where manufacturer does not contain DEFAULT_MANUFACTURER
        defaultVaccineTypeShouldNotBeFound("manufacturer.doesNotContain=" + DEFAULT_MANUFACTURER);

        // Get all the vaccineTypeList where manufacturer does not contain UPDATED_MANUFACTURER
        defaultVaccineTypeShouldBeFound("manufacturer.doesNotContain=" + UPDATED_MANUFACTURER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVaccineTypeShouldBeFound(String filter) throws Exception {
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccineType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].doses").value(hasItem(DEFAULT_DOSES)))
            .andExpect(jsonPath("$.[*].durationBetweenDosesTime").value(hasItem(DEFAULT_DURATION_BETWEEN_DOSES_TIME)))
            .andExpect(jsonPath("$.[*].durationBetweenDosesUnit").value(hasItem(DEFAULT_DURATION_BETWEEN_DOSES_UNIT.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)));

        // Check, that the count call also returns 1
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVaccineTypeShouldNotBeFound(String filter) throws Exception {
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVaccineTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVaccineType() throws Exception {
        // Get the vaccineType
        restVaccineTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccineType() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();

        // Update the vaccineType
        VaccineType updatedVaccineType = vaccineTypeRepository.findById(vaccineType.getId()).get();
        // Disconnect from session so that the updates on updatedVaccineType are not directly saved in db
        em.detach(updatedVaccineType);
        updatedVaccineType
            .name(UPDATED_NAME)
            .doses(UPDATED_DOSES)
            .durationBetweenDosesTime(UPDATED_DURATION_BETWEEN_DOSES_TIME)
            .durationBetweenDosesUnit(UPDATED_DURATION_BETWEEN_DOSES_UNIT)
            .manufacturer(UPDATED_MANUFACTURER);
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(updatedVaccineType);

        restVaccineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
        VaccineType testVaccineType = vaccineTypeList.get(vaccineTypeList.size() - 1);
        assertThat(testVaccineType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccineType.getDoses()).isEqualTo(UPDATED_DOSES);
        assertThat(testVaccineType.getDurationBetweenDosesTime()).isEqualTo(UPDATED_DURATION_BETWEEN_DOSES_TIME);
        assertThat(testVaccineType.getDurationBetweenDosesUnit()).isEqualTo(UPDATED_DURATION_BETWEEN_DOSES_UNIT);
        assertThat(testVaccineType.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void putNonExistingVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccineTypeWithPatch() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();

        // Update the vaccineType using partial update
        VaccineType partialUpdatedVaccineType = new VaccineType();
        partialUpdatedVaccineType.setId(vaccineType.getId());

        partialUpdatedVaccineType.manufacturer(UPDATED_MANUFACTURER);

        restVaccineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccineType))
            )
            .andExpect(status().isOk());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
        VaccineType testVaccineType = vaccineTypeList.get(vaccineTypeList.size() - 1);
        assertThat(testVaccineType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVaccineType.getDoses()).isEqualTo(DEFAULT_DOSES);
        assertThat(testVaccineType.getDurationBetweenDosesTime()).isEqualTo(DEFAULT_DURATION_BETWEEN_DOSES_TIME);
        assertThat(testVaccineType.getDurationBetweenDosesUnit()).isEqualTo(DEFAULT_DURATION_BETWEEN_DOSES_UNIT);
        assertThat(testVaccineType.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void fullUpdateVaccineTypeWithPatch() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();

        // Update the vaccineType using partial update
        VaccineType partialUpdatedVaccineType = new VaccineType();
        partialUpdatedVaccineType.setId(vaccineType.getId());

        partialUpdatedVaccineType
            .name(UPDATED_NAME)
            .doses(UPDATED_DOSES)
            .durationBetweenDosesTime(UPDATED_DURATION_BETWEEN_DOSES_TIME)
            .durationBetweenDosesUnit(UPDATED_DURATION_BETWEEN_DOSES_UNIT)
            .manufacturer(UPDATED_MANUFACTURER);

        restVaccineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccineType))
            )
            .andExpect(status().isOk());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
        VaccineType testVaccineType = vaccineTypeList.get(vaccineTypeList.size() - 1);
        assertThat(testVaccineType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVaccineType.getDoses()).isEqualTo(UPDATED_DOSES);
        assertThat(testVaccineType.getDurationBetweenDosesTime()).isEqualTo(UPDATED_DURATION_BETWEEN_DOSES_TIME);
        assertThat(testVaccineType.getDurationBetweenDosesUnit()).isEqualTo(UPDATED_DURATION_BETWEEN_DOSES_UNIT);
        assertThat(testVaccineType.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    void patchNonExistingVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccineTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccineType() throws Exception {
        int databaseSizeBeforeUpdate = vaccineTypeRepository.findAll().size();
        vaccineType.setId(count.incrementAndGet());

        // Create the VaccineType
        VaccineTypeDTO vaccineTypeDTO = vaccineTypeMapper.toDto(vaccineType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vaccineTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccineType in the database
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccineType() throws Exception {
        // Initialize the database
        vaccineTypeRepository.saveAndFlush(vaccineType);

        int databaseSizeBeforeDelete = vaccineTypeRepository.findAll().size();

        // Delete the vaccineType
        restVaccineTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccineType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VaccineType> vaccineTypeList = vaccineTypeRepository.findAll();
        assertThat(vaccineTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
