package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.VaccineType;
import com.medical.portal.repository.VaccineTypeRepository;
import com.medical.portal.service.criteria.VaccineTypeCriteria;
import com.medical.portal.service.dto.VaccineTypeDTO;
import com.medical.portal.service.mapper.VaccineTypeMapper;
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
 * Service for executing complex queries for {@link VaccineType} entities in the database.
 * The main input is a {@link VaccineTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VaccineTypeDTO} or a {@link Page} of {@link VaccineTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VaccineTypeQueryService extends QueryService<VaccineType> {

    private final Logger log = LoggerFactory.getLogger(VaccineTypeQueryService.class);

    private final VaccineTypeRepository vaccineTypeRepository;

    private final VaccineTypeMapper vaccineTypeMapper;

    public VaccineTypeQueryService(VaccineTypeRepository vaccineTypeRepository, VaccineTypeMapper vaccineTypeMapper) {
        this.vaccineTypeRepository = vaccineTypeRepository;
        this.vaccineTypeMapper = vaccineTypeMapper;
    }

    /**
     * Return a {@link List} of {@link VaccineTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VaccineTypeDTO> findByCriteria(VaccineTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VaccineType> specification = createSpecification(criteria);
        return vaccineTypeMapper.toDto(vaccineTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VaccineTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccineTypeDTO> findByCriteria(VaccineTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VaccineType> specification = createSpecification(criteria);
        return vaccineTypeRepository.findAll(specification, page).map(vaccineTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VaccineTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VaccineType> specification = createSpecification(criteria);
        return vaccineTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link VaccineTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VaccineType> createSpecification(VaccineTypeCriteria criteria) {
        Specification<VaccineType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VaccineType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), VaccineType_.name));
            }
            if (criteria.getDoses() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDoses(), VaccineType_.doses));
            }
            if (criteria.getDurationBetweenDosesTime() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDurationBetweenDosesTime(), VaccineType_.durationBetweenDosesTime)
                    );
            }
            if (criteria.getDurationBetweenDosesUnit() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDurationBetweenDosesUnit(), VaccineType_.durationBetweenDosesUnit));
            }
            if (criteria.getManufacturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacturer(), VaccineType_.manufacturer));
            }
        }
        return specification;
    }
}
