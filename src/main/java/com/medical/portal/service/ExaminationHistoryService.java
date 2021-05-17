package com.medical.portal.service;

import com.medical.portal.service.dto.ExaminationHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medical.portal.domain.ExaminationHistory}.
 */
public interface ExaminationHistoryService {
    /**
     * Save a examinationHistory.
     *
     * @param examinationHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    ExaminationHistoryDTO save(ExaminationHistoryDTO examinationHistoryDTO);

    /**
     * Partially updates a examinationHistory.
     *
     * @param examinationHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExaminationHistoryDTO> partialUpdate(ExaminationHistoryDTO examinationHistoryDTO);

    /**
     * Get all the examinationHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExaminationHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" examinationHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExaminationHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" examinationHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
