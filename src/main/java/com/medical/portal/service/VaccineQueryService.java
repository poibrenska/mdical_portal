package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.Vaccine;
import com.medical.portal.repository.VaccineRepository;
import com.medical.portal.service.criteria.VaccineCriteria;
import com.medical.portal.service.dto.VaccineDTO;
import com.medical.portal.service.mapper.VaccineMapper;
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
 * Service for executing complex queries for {@link Vaccine} entities in the database.
 * The main input is a {@link VaccineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VaccineDTO} or a {@link Page} of {@link VaccineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VaccineQueryService extends QueryService<Vaccine> {

    private final Logger log = LoggerFactory.getLogger(VaccineQueryService.class);

    private final VaccineRepository vaccineRepository;

    private final VaccineMapper vaccineMapper;

    public VaccineQueryService(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
    }

    /**
     * Return a {@link List} of {@link VaccineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VaccineDTO> findByCriteria(VaccineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vaccine> specification = createSpecification(criteria);
        return vaccineMapper.toDto(vaccineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VaccineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccineDTO> findByCriteria(VaccineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vaccine> specification = createSpecification(criteria);
        return vaccineRepository.findAll(specification, page).map(vaccineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VaccineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vaccine> specification = createSpecification(criteria);
        return vaccineRepository.count(specification);
    }

    /**
     * Function to convert {@link VaccineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vaccine> createSpecification(VaccineCriteria criteria) {
        Specification<Vaccine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vaccine_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Vaccine_.type));
            }
            if (criteria.getDose() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDose(), Vaccine_.dose));
            }
            if (criteria.getNextDoseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNextDoseDate(), Vaccine_.nextDoseDate));
            }
            if (criteria.getDosesLeft() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDosesLeft(), Vaccine_.dosesLeft));
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPatientId(), root -> root.join(Vaccine_.patient, JoinType.LEFT).get(Patient_.id))
                    );
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDoctorId(), root -> root.join(Vaccine_.doctor, JoinType.LEFT).get(Doctor_.id))
                    );
            }
        }
        return specification;
    }
}
