package com.medical.portal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medical.portal.IntegrationTest;
import com.medical.portal.domain.Doctor;
import com.medical.portal.domain.Documents;
import com.medical.portal.domain.Patient;
import com.medical.portal.domain.enumeration.DocumentType;
import com.medical.portal.repository.DocumentsRepository;
import com.medical.portal.service.criteria.DocumentsCriteria;
import com.medical.portal.service.dto.DocumentsDTO;
import com.medical.portal.service.mapper.DocumentsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocumentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentsResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_DOC_TYPE = DocumentType.EXAMINATION;
    private static final DocumentType UPDATED_DOC_TYPE = DocumentType.EXAMINATION;

    private static final byte[] DEFAULT_STREAM_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_STREAM_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_STREAM_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_STREAM_DATA_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_META = "AAAAAAAAAA";
    private static final String UPDATED_META = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private DocumentsMapper documentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentsMockMvc;

    private Documents documents;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documents createEntity(EntityManager em) {
        Documents documents = new Documents()
            .fileName(DEFAULT_FILE_NAME)
            .docType(DEFAULT_DOC_TYPE)
            .streamData(DEFAULT_STREAM_DATA)
            .streamDataContentType(DEFAULT_STREAM_DATA_CONTENT_TYPE)
            .meta(DEFAULT_META);
        return documents;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documents createUpdatedEntity(EntityManager em) {
        Documents documents = new Documents()
            .fileName(UPDATED_FILE_NAME)
            .docType(UPDATED_DOC_TYPE)
            .streamData(UPDATED_STREAM_DATA)
            .streamDataContentType(UPDATED_STREAM_DATA_CONTENT_TYPE)
            .meta(UPDATED_META);
        return documents;
    }

    @BeforeEach
    public void initTest() {
        documents = createEntity(em);
    }

    @Test
    @Transactional
    void createDocuments() throws Exception {
        int databaseSizeBeforeCreate = documentsRepository.findAll().size();
        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);
        restDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeCreate + 1);
        Documents testDocuments = documentsList.get(documentsList.size() - 1);
        assertThat(testDocuments.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocuments.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testDocuments.getStreamData()).isEqualTo(DEFAULT_STREAM_DATA);
        assertThat(testDocuments.getStreamDataContentType()).isEqualTo(DEFAULT_STREAM_DATA_CONTENT_TYPE);
        assertThat(testDocuments.getMeta()).isEqualTo(DEFAULT_META);
    }

    @Test
    @Transactional
    void createDocumentsWithExistingId() throws Exception {
        // Create the Documents with an existing ID
        documents.setId(1L);
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        int databaseSizeBeforeCreate = documentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentsRepository.findAll().size();
        // set the field null
        documents.setFileName(null);

        // Create the Documents, which fails.
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        restDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentsDTO)))
            .andExpect(status().isBadRequest());

        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentsRepository.findAll().size();
        // set the field null
        documents.setDocType(null);

        // Create the Documents, which fails.
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        restDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentsDTO)))
            .andExpect(status().isBadRequest());

        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].streamDataContentType").value(hasItem(DEFAULT_STREAM_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].streamData").value(hasItem(Base64Utils.encodeToString(DEFAULT_STREAM_DATA))))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META)));
    }

    @Test
    @Transactional
    void getDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get the documents
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL_ID, documents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documents.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.streamDataContentType").value(DEFAULT_STREAM_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.streamData").value(Base64Utils.encodeToString(DEFAULT_STREAM_DATA)))
            .andExpect(jsonPath("$.meta").value(DEFAULT_META));
    }

    @Test
    @Transactional
    void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        Long id = documents.getId();

        defaultDocumentsShouldBeFound("id.equals=" + id);
        defaultDocumentsShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentsShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName equals to DEFAULT_FILE_NAME
        defaultDocumentsShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the documentsList where fileName equals to UPDATED_FILE_NAME
        defaultDocumentsShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName not equals to DEFAULT_FILE_NAME
        defaultDocumentsShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the documentsList where fileName not equals to UPDATED_FILE_NAME
        defaultDocumentsShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultDocumentsShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the documentsList where fileName equals to UPDATED_FILE_NAME
        defaultDocumentsShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName is not null
        defaultDocumentsShouldBeFound("fileName.specified=true");

        // Get all the documentsList where fileName is null
        defaultDocumentsShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName contains DEFAULT_FILE_NAME
        defaultDocumentsShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the documentsList where fileName contains UPDATED_FILE_NAME
        defaultDocumentsShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName does not contain DEFAULT_FILE_NAME
        defaultDocumentsShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the documentsList where fileName does not contain UPDATED_FILE_NAME
        defaultDocumentsShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Disabled
    @Transactional
    void getAllDocumentsByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where docType equals to DEFAULT_DOC_TYPE
        defaultDocumentsShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the documentsList where docType equals to UPDATED_DOC_TYPE
        defaultDocumentsShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllDocumentsByDocTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where docType not equals to DEFAULT_DOC_TYPE
        defaultDocumentsShouldNotBeFound("docType.notEquals=" + DEFAULT_DOC_TYPE);

        // Get all the documentsList where docType not equals to UPDATED_DOC_TYPE
        defaultDocumentsShouldBeFound("docType.notEquals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Disabled
    @Transactional
    void getAllDocumentsByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultDocumentsShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the documentsList where docType equals to UPDATED_DOC_TYPE
        defaultDocumentsShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where docType is not null
        defaultDocumentsShouldBeFound("docType.specified=true");

        // Get all the documentsList where docType is null
        defaultDocumentsShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta equals to DEFAULT_META
        defaultDocumentsShouldBeFound("meta.equals=" + DEFAULT_META);

        // Get all the documentsList where meta equals to UPDATED_META
        defaultDocumentsShouldNotBeFound("meta.equals=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta not equals to DEFAULT_META
        defaultDocumentsShouldNotBeFound("meta.notEquals=" + DEFAULT_META);

        // Get all the documentsList where meta not equals to UPDATED_META
        defaultDocumentsShouldBeFound("meta.notEquals=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaIsInShouldWork() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta in DEFAULT_META or UPDATED_META
        defaultDocumentsShouldBeFound("meta.in=" + DEFAULT_META + "," + UPDATED_META);

        // Get all the documentsList where meta equals to UPDATED_META
        defaultDocumentsShouldNotBeFound("meta.in=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta is not null
        defaultDocumentsShouldBeFound("meta.specified=true");

        // Get all the documentsList where meta is null
        defaultDocumentsShouldNotBeFound("meta.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaContainsSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta contains DEFAULT_META
        defaultDocumentsShouldBeFound("meta.contains=" + DEFAULT_META);

        // Get all the documentsList where meta contains UPDATED_META
        defaultDocumentsShouldNotBeFound("meta.contains=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDocumentsByMetaNotContainsSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where meta does not contain DEFAULT_META
        defaultDocumentsShouldNotBeFound("meta.doesNotContain=" + DEFAULT_META);

        // Get all the documentsList where meta does not contain UPDATED_META
        defaultDocumentsShouldBeFound("meta.doesNotContain=" + UPDATED_META);
    }

    @Test
    @Transactional
    void getAllDocumentsByDoctorIsEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);
        Doctor doctor = DoctorResourceIT.createEntity(em);
        em.persist(doctor);
        em.flush();
        documents.setDoctor(doctor);
        documentsRepository.saveAndFlush(documents);
        Long doctorId = doctor.getId();

        // Get all the documentsList where doctor equals to doctorId
        defaultDocumentsShouldBeFound("doctorId.equals=" + doctorId);

        // Get all the documentsList where doctor equals to (doctorId + 1)
        defaultDocumentsShouldNotBeFound("doctorId.equals=" + (doctorId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        documents.setPatient(patient);
        documentsRepository.saveAndFlush(documents);
        Long patientId = patient.getId();

        // Get all the documentsList where patient equals to patientId
        defaultDocumentsShouldBeFound("patientId.equals=" + patientId);

        // Get all the documentsList where patient equals to (patientId + 1)
        defaultDocumentsShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentsShouldBeFound(String filter) throws Exception {
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].streamDataContentType").value(hasItem(DEFAULT_STREAM_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].streamData").value(hasItem(Base64Utils.encodeToString(DEFAULT_STREAM_DATA))))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META)));

        // Check, that the count call also returns 1
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentsShouldNotBeFound(String filter) throws Exception {
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocuments() throws Exception {
        // Get the documents
        restDocumentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();

        // Update the documents
        Documents updatedDocuments = documentsRepository.findById(documents.getId()).get();
        // Disconnect from session so that the updates on updatedDocuments are not directly saved in db
        em.detach(updatedDocuments);
        updatedDocuments
            .fileName(UPDATED_FILE_NAME)
            .docType(UPDATED_DOC_TYPE)
            .streamData(UPDATED_STREAM_DATA)
            .streamDataContentType(UPDATED_STREAM_DATA_CONTENT_TYPE)
            .meta(UPDATED_META);
        DocumentsDTO documentsDTO = documentsMapper.toDto(updatedDocuments);

        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
        Documents testDocuments = documentsList.get(documentsList.size() - 1);
        assertThat(testDocuments.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocuments.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testDocuments.getStreamData()).isEqualTo(UPDATED_STREAM_DATA);
        assertThat(testDocuments.getStreamDataContentType()).isEqualTo(UPDATED_STREAM_DATA_CONTENT_TYPE);
        assertThat(testDocuments.getMeta()).isEqualTo(UPDATED_META);
    }

    @Test
    @Transactional
    void putNonExistingDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentsWithPatch() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();

        // Update the documents using partial update
        Documents partialUpdatedDocuments = new Documents();
        partialUpdatedDocuments.setId(documents.getId());

        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocuments))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
        Documents testDocuments = documentsList.get(documentsList.size() - 1);
        assertThat(testDocuments.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocuments.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testDocuments.getStreamData()).isEqualTo(DEFAULT_STREAM_DATA);
        assertThat(testDocuments.getStreamDataContentType()).isEqualTo(DEFAULT_STREAM_DATA_CONTENT_TYPE);
        assertThat(testDocuments.getMeta()).isEqualTo(DEFAULT_META);
    }

    @Test
    @Transactional
    void fullUpdateDocumentsWithPatch() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();

        // Update the documents using partial update
        Documents partialUpdatedDocuments = new Documents();
        partialUpdatedDocuments.setId(documents.getId());

        partialUpdatedDocuments
            .fileName(UPDATED_FILE_NAME)
            .docType(UPDATED_DOC_TYPE)
            .streamData(UPDATED_STREAM_DATA)
            .streamDataContentType(UPDATED_STREAM_DATA_CONTENT_TYPE)
            .meta(UPDATED_META);

        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocuments))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
        Documents testDocuments = documentsList.get(documentsList.size() - 1);
        assertThat(testDocuments.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocuments.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testDocuments.getStreamData()).isEqualTo(UPDATED_STREAM_DATA);
        assertThat(testDocuments.getStreamDataContentType()).isEqualTo(UPDATED_STREAM_DATA_CONTENT_TYPE);
        assertThat(testDocuments.getMeta()).isEqualTo(UPDATED_META);
    }

    @Test
    @Transactional
    void patchNonExistingDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocuments() throws Exception {
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();
        documents.setId(count.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documents in the database
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        int databaseSizeBeforeDelete = documentsRepository.findAll().size();

        // Delete the documents
        restDocumentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, documents.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Documents> documentsList = documentsRepository.findAll();
        assertThat(documentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
