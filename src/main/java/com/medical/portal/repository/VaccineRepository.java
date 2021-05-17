package com.medical.portal.repository;

import com.medical.portal.domain.Vaccine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vaccine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long>, JpaSpecificationExecutor<Vaccine> {}
