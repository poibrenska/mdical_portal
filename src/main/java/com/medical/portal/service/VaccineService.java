package com.medical.portal.service;

import com.medical.portal.service.dto.VaccineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medical.portal.domain.Vaccine}.
 */
public interface VaccineService {
    /**
     * Save a vaccine.
     *
     * @param vaccineDTO the entity to save.
     * @return the persisted entity.
     */
    VaccineDTO save(VaccineDTO vaccineDTO);

    /**
     * Partially updates a vaccine.
     *
     * @param vaccineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VaccineDTO> partialUpdate(VaccineDTO vaccineDTO);

    /**
     * Get all the vaccines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VaccineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vaccine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VaccineDTO> findOne(Long id);

    /**
     * Delete the "id" vaccine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
