package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.ExaminationHistory;
import com.medical.portal.domain.Patient;
import com.medical.portal.repository.ExaminationHistoryRepository;
import com.medical.portal.service.criteria.ExaminationHistoryCriteria;
import com.medical.portal.service.dto.ExaminationHistoryDTO;
import com.medical.portal.service.mapper.ExaminationHistoryMapper;
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
 * Integration tests for the {@link ExaminationHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExaminationHistoryResourceIT {

    private static final String DEFAULT_DOCUMENTS = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/examination-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExaminationHistoryRepository examinationHistoryRepository;

    @Autowired
    private ExaminationHistoryMapper examinationHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExaminationHistoryMockMvc;

    private ExaminationHistory examinationHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationHistory createEntity(EntityManager em) {
        ExaminationHistory examinationHistory = new ExaminationHistory().documents(DEFAULT_DOCUMENTS).notes(DEFAULT_NOTES);
        return examinationHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExaminationHistory createUpdatedEntity(EntityManager em) {
        ExaminationHistory examinationHistory = new ExaminationHistory().documents(UPDATED_DOCUMENTS).notes(UPDATED_NOTES);
        return examinationHistory;
    }

    @BeforeEach
    public void initTest() {
        examinationHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createExaminationHistory() throws Exception {
        int databaseSizeBeforeCreate = examinationHistoryRepository.findAll().size();
        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);
        restExaminationHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ExaminationHistory testExaminationHistory = examinationHistoryList.get(examinationHistoryList.size() - 1);
        assertThat(testExaminationHistory.getDocuments()).isEqualTo(DEFAULT_DOCUMENTS);
        assertThat(testExaminationHistory.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createExaminationHistoryWithExistingId() throws Exception {
        // Create the ExaminationHistory with an existing ID
        examinationHistory.setId(1L);
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        int databaseSizeBeforeCreate = examinationHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExaminationHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExaminationHistories() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documents").value(hasItem(DEFAULT_DOCUMENTS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getExaminationHistory() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get the examinationHistory
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, examinationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examinationHistory.getId().intValue()))
            .andExpect(jsonPath("$.documents").value(DEFAULT_DOCUMENTS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getExaminationHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        Long id = examinationHistory.getId();

        defaultExaminationHistoryShouldBeFound("id.equals=" + id);
        defaultExaminationHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultExaminationHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExaminationHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultExaminationHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExaminationHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents equals to DEFAULT_DOCUMENTS
        defaultExaminationHistoryShouldBeFound("documents.equals=" + DEFAULT_DOCUMENTS);

        // Get all the examinationHistoryList where documents equals to UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldNotBeFound("documents.equals=" + UPDATED_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents not equals to DEFAULT_DOCUMENTS
        defaultExaminationHistoryShouldNotBeFound("documents.notEquals=" + DEFAULT_DOCUMENTS);

        // Get all the examinationHistoryList where documents not equals to UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldBeFound("documents.notEquals=" + UPDATED_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsIsInShouldWork() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents in DEFAULT_DOCUMENTS or UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldBeFound("documents.in=" + DEFAULT_DOCUMENTS + "," + UPDATED_DOCUMENTS);

        // Get all the examinationHistoryList where documents equals to UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldNotBeFound("documents.in=" + UPDATED_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents is not null
        defaultExaminationHistoryShouldBeFound("documents.specified=true");

        // Get all the examinationHistoryList where documents is null
        defaultExaminationHistoryShouldNotBeFound("documents.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsContainsSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents contains DEFAULT_DOCUMENTS
        defaultExaminationHistoryShouldBeFound("documents.contains=" + DEFAULT_DOCUMENTS);

        // Get all the examinationHistoryList where documents contains UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldNotBeFound("documents.contains=" + UPDATED_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDocumentsNotContainsSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where documents does not contain DEFAULT_DOCUMENTS
        defaultExaminationHistoryShouldNotBeFound("documents.doesNotContain=" + DEFAULT_DOCUMENTS);

        // Get all the examinationHistoryList where documents does not contain UPDATED_DOCUMENTS
        defaultExaminationHistoryShouldBeFound("documents.doesNotContain=" + UPDATED_DOCUMENTS);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes equals to DEFAULT_NOTES
        defaultExaminationHistoryShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the examinationHistoryList where notes equals to UPDATED_NOTES
        defaultExaminationHistoryShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes not equals to DEFAULT_NOTES
        defaultExaminationHistoryShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the examinationHistoryList where notes not equals to UPDATED_NOTES
        defaultExaminationHistoryShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultExaminationHistoryShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the examinationHistoryList where notes equals to UPDATED_NOTES
        defaultExaminationHistoryShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes is not null
        defaultExaminationHistoryShouldBeFound("notes.specified=true");

        // Get all the examinationHistoryList where notes is null
        defaultExaminationHistoryShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesContainsSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes contains DEFAULT_NOTES
        defaultExaminationHistoryShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the examinationHistoryList where notes contains UPDATED_NOTES
        defaultExaminationHistoryShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        // Get all the examinationHistoryList where notes does not contain DEFAULT_NOTES
        defaultExaminationHistoryShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the examinationHistoryList where notes does not contain UPDATED_NOTES
        defaultExaminationHistoryShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        examinationHistory.setDoctor(doctor);
        examinationHistoryRepository.saveAndFlush(examinationHistory);
        Long doctorId = doctor.getId();

        // Get all the examinationHistoryList where doctor equals to doctorId
        defaultExaminationHistoryShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the examinationHistoryList where doctor equals to (doctorId + 1)
        defaultExaminationHistoryShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllExaminationHistoriesByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        examinationHistory.setPatient(patient);
        examinationHistoryRepository.saveAndFlush(examinationHistory);
        Long patientId = patient.getId();

        // Get all the examinationHistoryList where patient equals to patientId
        defaultExaminationHistoryShouldBeFound("patientId.equals=" + patientId);

        // Get all the examinationHistoryList where patient equals to (patientId + 1)
        defaultExaminationHistoryShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExaminationHistoryShouldBeFound(String filter) throws Exception {
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examinationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documents").value(hasItem(DEFAULT_DOCUMENTS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExaminationHistoryShouldNotBeFound(String filter) throws Exception {
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExaminationHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExaminationHistory() throws Exception {
        // Get the examinationHistory
        restExaminationHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExaminationHistory() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();

        // Update the examinationHistory
        ExaminationHistory updatedExaminationHistory = examinationHistoryRepository.findById(examinationHistory.getId()).get();
        // Disconnect from session so that the updates on updatedExaminationHistory are not directly saved in db
        em.detach(updatedExaminationHistory);
        updatedExaminationHistory.documents(UPDATED_DOCUMENTS).notes(UPDATED_NOTES);
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(updatedExaminationHistory);

        restExaminationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
        ExaminationHistory testExaminationHistory = examinationHistoryList.get(examinationHistoryList.size() - 1);
        assertThat(testExaminationHistory.getDocuments()).isEqualTo(UPDATED_DOCUMENTS);
        assertThat(testExaminationHistory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examinationHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExaminationHistoryWithPatch() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();

        // Update the examinationHistory using partial update
        ExaminationHistory partialUpdatedExaminationHistory = new ExaminationHistory();
        partialUpdatedExaminationHistory.setId(examinationHistory.getId());

        partialUpdatedExaminationHistory.documents(UPDATED_DOCUMENTS);

        restExaminationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExaminationHistory))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
        ExaminationHistory testExaminationHistory = examinationHistoryList.get(examinationHistoryList.size() - 1);
        assertThat(testExaminationHistory.getDocuments()).isEqualTo(UPDATED_DOCUMENTS);
        assertThat(testExaminationHistory.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateExaminationHistoryWithPatch() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();

        // Update the examinationHistory using partial update
        ExaminationHistory partialUpdatedExaminationHistory = new ExaminationHistory();
        partialUpdatedExaminationHistory.setId(examinationHistory.getId());

        partialUpdatedExaminationHistory.documents(UPDATED_DOCUMENTS).notes(UPDATED_NOTES);

        restExaminationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExaminationHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExaminationHistory))
            )
            .andExpect(status().isOk());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
        ExaminationHistory testExaminationHistory = examinationHistoryList.get(examinationHistoryList.size() - 1);
        assertThat(testExaminationHistory.getDocuments()).isEqualTo(UPDATED_DOCUMENTS);
        assertThat(testExaminationHistory.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examinationHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExaminationHistory() throws Exception {
        int databaseSizeBeforeUpdate = examinationHistoryRepository.findAll().size();
        examinationHistory.setId(count.incrementAndGet());

        // Create the ExaminationHistory
        ExaminationHistoryDTO examinationHistoryDTO = examinationHistoryMapper.toDto(examinationHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExaminationHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examinationHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExaminationHistory in the database
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExaminationHistory() throws Exception {
        // Initialize the database
        examinationHistoryRepository.saveAndFlush(examinationHistory);

        int databaseSizeBeforeDelete = examinationHistoryRepository.findAll().size();

        // Delete the examinationHistory
        restExaminationHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, examinationHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExaminationHistory> examinationHistoryList = examinationHistoryRepository.findAll();
        assertThat(examinationHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
