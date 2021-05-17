package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.Patient;
import com.medical.portal.repository.PatientRepository;
import com.medical.portal.service.criteria.PatientCriteria;
import com.medical.portal.service.dto.PatientDTO;
import com.medical.portal.service.mapper.PatientMapper;
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
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientDTO} or a {@link Page} of {@link PatientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private final Logger log = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    public PatientQueryService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    /**
     * Return a {@link List} of {@link PatientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientDTO> findByCriteria(PatientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientMapper.toDto(patientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PatientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findByCriteria(PatientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page).map(patientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Patient_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Patient_.lastName));
            }
            if (criteria.getAddressText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressText(), Patient_.addressText));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Patient_.birthDate));
            }
            if (criteria.getEgn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEgn(), Patient_.egn));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Patient_.phone));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Patient_.active));
            }
            if (criteria.getGp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGp(), Patient_.gp));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Patient_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getDocumentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocumentsId(),
                            root -> root.join(Patient_.documents, JoinType.LEFT).get(Documents_.id)
                        )
                    );
            }
            if (criteria.getAppointmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppointmentId(),
                            root -> root.join(Patient_.appointments, JoinType.LEFT).get(Appointment_.id)
                        )
                    );
            }
            if (criteria.getExaminationHistoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getExaminationHistoryId(),
                            root -> root.join(Patient_.examinationHistories, JoinType.LEFT).get(ExaminationHistory_.id)
                        )
                    );
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDoctorId(), root -> root.join(Patient_.doctors, JoinType.LEFT).get(Doctor_.id))
                    );
            }
            if (criteria.getVaccineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVaccineId(), root -> root.join(Patient_.vaccines, JoinType.LEFT).get(Vaccine_.id))
                    );
            }
        }
        return specification;
    }
}
