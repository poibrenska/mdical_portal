package com.medical.portal.web.rest;

import com.medical.portal.repository.VaccineTypeRepository;
import com.medical.portal.service.VaccineTypeQueryService;
import com.medical.portal.service.VaccineTypeService;
import com.medical.portal.service.criteria.VaccineTypeCriteria;
import com.medical.portal.service.dto.VaccineTypeDTO;
import com.medical.portal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.medical.portal.domain.VaccineType}.
 */
@RestController
@RequestMapping("/api")
public class VaccineTypeResource {

    private final Logger log = LoggerFactory.getLogger(VaccineTypeResource.class);

    private static final String ENTITY_NAME = "medportalVaccineType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccineTypeService vaccineTypeService;

    private final VaccineTypeRepository vaccineTypeRepository;

    private final VaccineTypeQueryService vaccineTypeQueryService;

    public VaccineTypeResource(
        VaccineTypeService vaccineTypeService,
        VaccineTypeRepository vaccineTypeRepository,
        VaccineTypeQueryService vaccineTypeQueryService
    ) {
        this.vaccineTypeService = vaccineTypeService;
        this.vaccineTypeRepository = vaccineTypeRepository;
        this.vaccineTypeQueryService = vaccineTypeQueryService;
    }

    /**
     * {@code POST  /vaccine-types} : Create a new vaccineType.
     *
     * @param vaccineTypeDTO the vaccineTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccineTypeDTO, or with status {@code 400 (Bad Request)} if the vaccineType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccine-types")
    public ResponseEntity<VaccineTypeDTO> createVaccineType(@Valid @RequestBody VaccineTypeDTO vaccineTypeDTO) throws URISyntaxException {
        log.debug("REST request to save VaccineType : {}", vaccineTypeDTO);
        if (vaccineTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccineType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaccineTypeDTO result = vaccineTypeService.save(vaccineTypeDTO);
        return ResponseEntity
            .created(new URI("/api/vaccine-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaccine-types/:id} : Updates an existing vaccineType.
     *
     * @param id the id of the vaccineTypeDTO to save.
     * @param vaccineTypeDTO the vaccineTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccineTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccine-types/{id}")
    public ResponseEntity<VaccineTypeDTO> updateVaccineType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccineTypeDTO vaccineTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VaccineType : {}, {}", id, vaccineTypeDTO);
        if (vaccineTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VaccineTypeDTO result = vaccineTypeService.save(vaccineTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccineTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccine-types/:id} : Partial updates given fields of an existing vaccineType, field will ignore if it is null
     *
     * @param id the id of the vaccineTypeDTO to save.
     * @param vaccineTypeDTO the vaccineTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineTypeDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccineTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccineTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccine-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VaccineTypeDTO> partialUpdateVaccineType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccineTypeDTO vaccineTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VaccineType partially : {}, {}", id, vaccineTypeDTO);
        if (vaccineTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccineTypeDTO> result = vaccineTypeService.partialUpdate(vaccineTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccineTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccine-types} : get all the vaccineTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccineTypes in body.
     */
    @GetMapping("/vaccine-types")
    public ResponseEntity<List<VaccineTypeDTO>> getAllVaccineTypes(VaccineTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VaccineTypes by criteria: {}", criteria);
        Page<VaccineTypeDTO> page = vaccineTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccine-types/count} : count all the vaccineTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vaccine-types/count")
    public ResponseEntity<Long> countVaccineTypes(VaccineTypeCriteria criteria) {
        log.debug("REST request to count VaccineTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(vaccineTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vaccine-types/:id} : get the "id" vaccineType.
     *
     * @param id the id of the vaccineTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccineTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccine-types/{id}")
    public ResponseEntity<VaccineTypeDTO> getVaccineType(@PathVariable Long id) {
        log.debug("REST request to get VaccineType : {}", id);
        Optional<VaccineTypeDTO> vaccineTypeDTO = vaccineTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccineTypeDTO);
    }

    /**
     * {@code DELETE  /vaccine-types/:id} : delete the "id" vaccineType.
     *
     * @param id the id of the vaccineTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccine-types/{id}")
    public ResponseEntity<Void> deleteVaccineType(@PathVariable Long id) {
        log.debug("REST request to delete VaccineType : {}", id);
        vaccineTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
