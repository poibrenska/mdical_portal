package com.medical.portal.web.rest;

import com.medical.portal.repository.ExaminationHistoryRepository;
import com.medical.portal.service.ExaminationHistoryQueryService;
import com.medical.portal.service.ExaminationHistoryService;
import com.medical.portal.service.criteria.ExaminationHistoryCriteria;
import com.medical.portal.service.dto.ExaminationHistoryDTO;
import com.medical.portal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medical.portal.domain.ExaminationHistory}.
 */
@RestController
@RequestMapping("/api")
public class ExaminationHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ExaminationHistoryResource.class);

    private static final String ENTITY_NAME = "medportalExaminationHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExaminationHistoryService examinationHistoryService;

    private final ExaminationHistoryRepository examinationHistoryRepository;

    private final ExaminationHistoryQueryService examinationHistoryQueryService;

    public ExaminationHistoryResource(
        ExaminationHistoryService examinationHistoryService,
        ExaminationHistoryRepository examinationHistoryRepository,
        ExaminationHistoryQueryService examinationHistoryQueryService
    ) {
        this.examinationHistoryService = examinationHistoryService;
        this.examinationHistoryRepository = examinationHistoryRepository;
        this.examinationHistoryQueryService = examinationHistoryQueryService;
    }

    /**
     * {@code POST  /examination-histories} : Create a new examinationHistory.
     *
     * @param examinationHistoryDTO the examinationHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examinationHistoryDTO, or with status {@code 400 (Bad Request)} if the examinationHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/examination-histories")
    public ResponseEntity<ExaminationHistoryDTO> createExaminationHistory(@RequestBody ExaminationHistoryDTO examinationHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExaminationHistory : {}", examinationHistoryDTO);
        if (examinationHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new examinationHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExaminationHistoryDTO result = examinationHistoryService.save(examinationHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/examination-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /examination-histories/:id} : Updates an existing examinationHistory.
     *
     * @param id the id of the examinationHistoryDTO to save.
     * @param examinationHistoryDTO the examinationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the examinationHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examinationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/examination-histories/{id}")
    public ResponseEntity<ExaminationHistoryDTO> updateExaminationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExaminationHistoryDTO examinationHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExaminationHistory : {}, {}", id, examinationHistoryDTO);
        if (examinationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExaminationHistoryDTO result = examinationHistoryService.save(examinationHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examinationHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /examination-histories/:id} : Partial updates given fields of an existing examinationHistory, field will ignore if it is null
     *
     * @param id the id of the examinationHistoryDTO to save.
     * @param examinationHistoryDTO the examinationHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the examinationHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examinationHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examinationHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/examination-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExaminationHistoryDTO> partialUpdateExaminationHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExaminationHistoryDTO examinationHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExaminationHistory partially : {}, {}", id, examinationHistoryDTO);
        if (examinationHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExaminationHistoryDTO> result = examinationHistoryService.partialUpdate(examinationHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, examinationHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /examination-histories} : get all the examinationHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examinationHistories in body.
     */
    @GetMapping("/examination-histories")
    public ResponseEntity<List<ExaminationHistoryDTO>> getAllExaminationHistories(ExaminationHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExaminationHistories by criteria: {}", criteria);
        Page<ExaminationHistoryDTO> page = examinationHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examination-histories/count} : count all the examinationHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/examination-histories/count")
    public ResponseEntity<Long> countExaminationHistories(ExaminationHistoryCriteria criteria) {
        log.debug("REST request to count ExaminationHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(examinationHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /examination-histories/:id} : get the "id" examinationHistory.
     *
     * @param id the id of the examinationHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examinationHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/examination-histories/{id}")
    public ResponseEntity<ExaminationHistoryDTO> getExaminationHistory(@PathVariable Long id) {
        log.debug("REST request to get ExaminationHistory : {}", id);
        Optional<ExaminationHistoryDTO> examinationHistoryDTO = examinationHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examinationHistoryDTO);
    }

    /**
     * {@code DELETE  /examination-histories/:id} : delete the "id" examinationHistory.
     *
     * @param id the id of the examinationHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/examination-histories/{id}")
    public ResponseEntity<Void> deleteExaminationHistory(@PathVariable Long id) {
        log.debug("REST request to delete ExaminationHistory : {}", id);
        examinationHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
