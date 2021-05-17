package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.Hospital;
import com.medical.portal.repository.HospitalRepository;
import com.medical.portal.service.criteria.HospitalCriteria;
import com.medical.portal.service.dto.HospitalDTO;
import com.medical.portal.service.mapper.HospitalMapper;
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
 * Service for executing complex queries for {@link Hospital} entities in the database.
 * The main input is a {@link HospitalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HospitalDTO} or a {@link Page} of {@link HospitalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HospitalQueryService extends QueryService<Hospital> {

    private final Logger log = LoggerFactory.getLogger(HospitalQueryService.class);

    private final HospitalRepository hospitalRepository;

    private final HospitalMapper hospitalMapper;

    public HospitalQueryService(HospitalRepository hospitalRepository, HospitalMapper hospitalMapper) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
    }

    /**
     * Return a {@link List} of {@link HospitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HospitalDTO> findByCriteria(HospitalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalMapper.toDto(hospitalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HospitalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HospitalDTO> findByCriteria(HospitalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.findAll(specification, page).map(hospitalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HospitalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hospital> specification = createSpecification(criteria);
        return hospitalRepository.count(specification);
    }

    /**
     * Function to convert {@link HospitalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Hospital> createSpecification(HospitalCriteria criteria) {
        Specification<Hospital> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Hospital_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Hospital_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Hospital_.address));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Hospital_.city));
            }
            if (criteria.getAdministrationPhones() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdministrationPhones(), Hospital_.administrationPhones));
            }
            if (criteria.getNoi() != null) {
                specification = specification.and(buildSpecification(criteria.getNoi(), Hospital_.noi));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDirector(), Hospital_.director));
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDoctorId(), root -> root.join(Hospital_.doctors, JoinType.LEFT).get(Doctor_.id))
                    );
            }
        }
        return specification;
    }
}
