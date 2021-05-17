package com.medical.portal.service;

import com.medical.portal.service.dto.HospitalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medical.portal.domain.Hospital}.
 */
public interface HospitalService {
    /**
     * Save a hospital.
     *
     * @param hospitalDTO the entity to save.
     * @return the persisted entity.
     */
    HospitalDTO save(HospitalDTO hospitalDTO);

    /**
     * Partially updates a hospital.
     *
     * @param hospitalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HospitalDTO> partialUpdate(HospitalDTO hospitalDTO);

    /**
     * Get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HospitalDTO> findAll(Pageable pageable);

    /**
     * Get all the hospitals with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HospitalDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" hospital.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HospitalDTO> findOne(Long id);

    /**
     * Delete the "id" hospital.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
