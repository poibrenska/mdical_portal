package com.medical.portal.repository;

import com.medical.portal.domain.VaccineType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VaccineType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccineTypeRepository extends JpaRepository<VaccineType, Long>, JpaSpecificationExecutor<VaccineType> {}
