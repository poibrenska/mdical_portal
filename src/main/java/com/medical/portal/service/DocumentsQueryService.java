package com.medical.portal.service;

import com.medical.portal.domain.*; // for static metamodels
import com.medical.portal.domain.Documents;
import com.medical.portal.repository.DocumentsRepository;
import com.medical.portal.service.criteria.DocumentsCriteria;
import com.medical.portal.service.dto.DocumentsDTO;
import com.medical.portal.service.mapper.DocumentsMapper;
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
 * Service for executing complex queries for {@link Documents} entities in the database.
 * The main input is a {@link DocumentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentsDTO} or a {@link Page} of {@link DocumentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentsQueryService extends QueryService<Documents> {

    private final Logger log = LoggerFactory.getLogger(DocumentsQueryService.class);

    private final DocumentsRepository documentsRepository;

    private final DocumentsMapper documentsMapper;

    public DocumentsQueryService(DocumentsRepository documentsRepository, DocumentsMapper documentsMapper) {
        this.documentsRepository = documentsRepository;
        this.documentsMapper = documentsMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentsDTO> findByCriteria(DocumentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Documents> specification = createSpecification(criteria);
        return documentsMapper.toDto(documentsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocumentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentsDTO> findByCriteria(DocumentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Documents> specification = createSpecification(criteria);
        return documentsRepository.findAll(specification, page).map(documentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Documents> specification = createSpecification(criteria);
        return documentsRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Documents> createSpecification(DocumentsCriteria criteria) {
        Specification<Documents> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Documents_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), Documents_.fileName));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), Documents_.docType));
            }
            if (criteria.getMeta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMeta(), Documents_.meta));
            }
            if (criteria.getDoctorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDoctorId(), root -> root.join(Documents_.doctor, JoinType.LEFT).get(Doctor_.id))
                    );
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPatientId(), root -> root.join(Documents_.patient, JoinType.LEFT).get(Patient_.id))
                    );
            }
        }
        return specification;
    }
}
