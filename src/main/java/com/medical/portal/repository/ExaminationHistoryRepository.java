package com.medical.portal.repository;

import com.medical.portal.domain.ExaminationHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExaminationHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExaminationHistoryRepository
    extends JpaRepository<ExaminationHistory, Long>, JpaSpecificationExecutor<ExaminationHistory> {}
