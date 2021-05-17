package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.Doctor;
import com.medical.portal.repository.DoctorRepository;
import com.medical.portal.service.criteria.DoctorCriteria;
import com.medical.portal.service.dto.DoctorDTO;
import com.medical.portal.service.mapper.DoctorMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Doctor} entities in the database.
 * The main input is a {@link DoctorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DoctorDTO} or a {@link Page} of {@link DoctorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorQueryService extends QueryService<Doctor> {

    private final Logger log = LoggerFactory.getLogger(DoctorQueryService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    public DoctorQueryService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    /**
     * Return a {@link List} of {@link DoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DoctorDTO> findByCriteria(DoctorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorMapper.toDto(doctorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DoctorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findByCriteria(DoctorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.findAll(specification, page).map(doctorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Doctor> specification = createSpecification(criteria);
        return doctorRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Doctor> createSpecification(DoctorCriteria criteria) {
        Specification<Doctor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Doctor_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Doctor_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Doctor_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Doctor_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Doctor_.phone));
            }
            if (criteria.getSpecialization() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialization(), Doctor_.specialization));
            }
            if (criteria.getAdditionalInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdditionalInfo(), Doctor_.additionalInfo));
            }
            if (criteria.getMeta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMeta(), Doctor_.meta));
            }
            if (criteria.getDocumentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocumentsId(),
                            root -> root.join(Doctor_.documents, JoinType.LEFT).get(Documents_.id)
                        )
                    );
            }
            if (criteria.getAppointmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppointmentId(),
                            root -> root.join(Doctor_.appointments, JoinType.LEFT).get(Appointment_.id)
                        )
                    );
            }
            if (criteria.getExaminationHistoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExaminationHistoryId(),
                            root -> root.join(Doctor_.examinationHistories, JoinType.LEFT).get(ExaminationHistory_.id)
                        )
                    );
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPatientId(), root -> root.join(Doctor_.patients, JoinType.LEFT).get(Patient_.id))
                    );
            }
            if (criteria.getHospitalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getHospitalId(), root -> root.join(Doctor_.hospitals, JoinType.LEFT).get(Hospital_.id))
                    );
            }
            if (criteria.getVaccineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVaccineId(), root -> root.join(Doctor_.vaccines, JoinType.LEFT).get(Vaccine_.id))
                    );
            }
        }
        return specification;
    }
}
