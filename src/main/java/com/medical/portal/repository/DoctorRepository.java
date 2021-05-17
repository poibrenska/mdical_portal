package com.medical.portal.repository;

import com.medical.portal.domain.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Doctor entity.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    @Query(
        value = "select distinct doctor from Doctor doctor left join fetch doctor.patients",
        countQuery = "select count(distinct doctor) from Doctor doctor"
    )
    Page<Doctor> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct doctor from Doctor doctor left join fetch doctor.patients")
    List<Doctor> findAllWithEagerRelationships();

    @Query("select doctor from Doctor doctor left join fetch doctor.patients where doctor.id =:id")
    Optional<Doctor> findOneWithEagerRelationships(@Param("id") Long id);
}
