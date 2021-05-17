package com.medical.portal.repository;

import com.medical.portal.domain.Documents;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Documents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long>, JpaSpecificationExecutor<Documents> {}
