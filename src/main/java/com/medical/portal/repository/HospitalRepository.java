package com.medical.portal.repository;

import com.medical.portal.domain.Hospital;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hospital entity.
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>, JpaSpecificationExecutor<Hospital> {
    @Query(
        value = "select distinct hospital from Hospital hospital left join fetch hospital.doctors",
        countQuery = "select count(distinct hospital) from Hospital hospital"
    )
    Page<Hospital> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct hospital from Hospital hospital left join fetch hospital.doctors")
    List<Hospital> findAllWithEagerRelationships();

    @Query("select hospital from Hospital hospital left join fetch hospital.doctors where hospital.id =:id")
    Optional<Hospital> findOneWithEagerRelationships(@Param("id") Long id);
}
