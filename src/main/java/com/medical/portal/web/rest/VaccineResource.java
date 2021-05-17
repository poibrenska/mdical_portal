package com.medical.portal.web.rest;

import com.medical.portal.repository.VaccineRepository;
import com.medical.portal.service.VaccineQueryService;
import com.medical.portal.service.VaccineService;
import com.medical.portal.service.criteria.VaccineCriteria;
import com.medical.portal.service.dto.VaccineDTO;
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
 * REST controller for managing {@link com.medical.portal.domain.Vaccine}.
 */
@RestController
@RequestMapping("/api")
public class VaccineResource {

    private final Logger log = LoggerFactory.getLogger(VaccineResource.class);

    private static final String ENTITY_NAME = "medportalVaccine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccineService vaccineService;

    private final VaccineRepository vaccineRepository;

    private final VaccineQueryService vaccineQueryService;

    public VaccineResource(VaccineService vaccineService, VaccineRepository vaccineRepository, VaccineQueryService vaccineQueryService) {
        this.vaccineService = vaccineService;
        this.vaccineRepository = vaccineRepository;
        this.vaccineQueryService = vaccineQueryService;
    }

    /**
     * {@code POST  /vaccines} : Create a new vaccine.
     *
     * @param vaccineDTO the vaccineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccineDTO, or with status {@code 400 (Bad Request)} if the vaccine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccines")
    public ResponseEntity<VaccineDTO> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) throws URISyntaxException {
        log.debug("REST request to save Vaccine : {}", vaccineDTO);
        if (vaccineDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VaccineDTO result = vaccineService.save(vaccineDTO);
        return ResponseEntity
            .created(new URI("/api/vaccines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaccines/:id} : Updates an existing vaccine.
     *
     * @param id the id of the vaccineDTO to save.
     * @param vaccineDTO the vaccineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccines/{id}")
    public ResponseEntity<VaccineDTO> updateVaccine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccineDTO vaccineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vaccine : {}, {}", id, vaccineDTO);
        if (vaccineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VaccineDTO result = vaccineService.save(vaccineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccines/:id} : Partial updates given fields of an existing vaccine, field will ignore if it is null
     *
     * @param id the id of the vaccineDTO to save.
     * @param vaccineDTO the vaccineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VaccineDTO> partialUpdateVaccine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccineDTO vaccineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vaccine partially : {}, {}", id, vaccineDTO);
        if (vaccineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccineDTO> result = vaccineService.partialUpdate(vaccineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vaccineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccines} : get all the vaccines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccines in body.
     */
    @GetMapping("/vaccines")
    public ResponseEntity<List<VaccineDTO>> getAllVaccines(VaccineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vaccines by criteria: {}", criteria);
        Page<VaccineDTO> page = vaccineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccines/count} : count all the vaccines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vaccines/count")
    public ResponseEntity<Long> countVaccines(VaccineCriteria criteria) {
        log.debug("REST request to count Vaccines by criteria: {}", criteria);
        return ResponseEntity.ok().body(vaccineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vaccines/:id} : get the "id" vaccine.
     *
     * @param id the id of the vaccineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccines/{id}")
    public ResponseEntity<VaccineDTO> getVaccine(@PathVariable Long id) {
        log.debug("REST request to get Vaccine : {}", id);
        Optional<VaccineDTO> vaccineDTO = vaccineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccineDTO);
    }

    /**
     * {@code DELETE  /vaccines/:id} : delete the "id" vaccine.
     *
     * @param id the id of the vaccineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccines/{id}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable Long id) {
        log.debug("REST request to delete Vaccine : {}", id);
        vaccineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
