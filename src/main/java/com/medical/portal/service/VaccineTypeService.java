package com.medical.portal.service;

import com.medical.portal.service.dto.VaccineTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medical.portal.domain.VaccineType}.
 */
public interface VaccineTypeService {
    /**
     * Save a vaccineType.
     *
     * @param vaccineTypeDTO the entity to save.
     * @return the persisted entity.
     */
    VaccineTypeDTO save(VaccineTypeDTO vaccineTypeDTO);

    /**
     * Partially updates a vaccineType.
     *
     * @param vaccineTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VaccineTypeDTO> partialUpdate(VaccineTypeDTO vaccineTypeDTO);

    /**
     * Get all the vaccineTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VaccineTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vaccineType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VaccineTypeDTO> findOne(Long id);

    /**
     * Delete the "id" vaccineType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
