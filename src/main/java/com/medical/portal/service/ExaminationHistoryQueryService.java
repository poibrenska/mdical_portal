package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.ExaminationHistory;
import com.medical.portal.repository.ExaminationHistoryRepository;
import com.medical.portal.service.criteria.ExaminationHistoryCriteria;
import com.medical.portal.service.dto.ExaminationHistoryDTO;
import com.medical.portal.service.mapper.ExaminationHistoryMapper;
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
 * Service for executing complex queries for {@link ExaminationHistory} entities in the database.
 * The main input is a {@link ExaminationHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExaminationHistoryDTO} or a {@link Page} of {@link ExaminationHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExaminationHistoryQueryService extends QueryService<ExaminationHistory> {

    private final Logger log = LoggerFactory.getLogger(ExaminationHistoryQueryService.class);

    private final ExaminationHistoryRepository examinationHistoryRepository;

    private final ExaminationHistoryMapper examinationHistoryMapper;

    public ExaminationHistoryQueryService(
        ExaminationHistoryRepository examinationHistoryRepository,
        ExaminationHistoryMapper examinationHistoryMapper
    ) {
        this.examinationHistoryRepository = examinationHistoryRepository;
        this.examinationHistoryMapper = examinationHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link ExaminationHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExaminationHistoryDTO> findByCriteria(ExaminationHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExaminationHistory> specification = createSpecification(criteria);
        return examinationHistoryMapper.toDto(examinationHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExaminationHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExaminationHistoryDTO> findByCriteria(ExaminationHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExaminationHistory> specification = createSpecification(criteria);
        return examinationHistoryRepository.findAll(specification, page).map(examinationHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExaminationHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExaminationHistory> specification = createSpecification(criteria);
        return examinationHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ExaminationHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExaminationHistory> createSpecification(ExaminationHistoryCriteria criteria) {
        Specification<ExaminationHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExaminationHistory_.id));
            }
            if (criteria.getDocuments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocuments(), ExaminationHistory_.documents));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), ExaminationHistory_.notes));
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDoctorId(),
                            root -> root.join(ExaminationHistory_.doctor, JoinType.LEFT).get(Doctor_.id)
                        )
                    );
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientId(),
                            root -> root.join(ExaminationHistory_.patient, JoinType.LEFT).get(Patient_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
