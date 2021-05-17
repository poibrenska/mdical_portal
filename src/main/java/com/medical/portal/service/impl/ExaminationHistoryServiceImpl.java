package com.medical.portal.service.impl;

import com.medical.portal.domain.ExaminationHistory;
import com.medical.portal.repository.ExaminationHistoryRepository;
import com.medical.portal.service.ExaminationHistoryService;
import com.medical.portal.service.dto.ExaminationHistoryDTO;
import com.medical.portal.service.mapper.ExaminationHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExaminationHistory}.
 */
@Service
@Transactional
public class ExaminationHistoryServiceImpl implements ExaminationHistoryService {

    private final Logger log = LoggerFactory.getLogger(ExaminationHistoryServiceImpl.class);

    private final ExaminationHistoryRepository examinationHistoryRepository;

    private final ExaminationHistoryMapper examinationHistoryMapper;

    public ExaminationHistoryServiceImpl(
        ExaminationHistoryRepository examinationHistoryRepository,
        ExaminationHistoryMapper examinationHistoryMapper
    ) {
        this.examinationHistoryRepository = examinationHistoryRepository;
        this.examinationHistoryMapper = examinationHistoryMapper;
    }

    @Override
    public ExaminationHistoryDTO save(ExaminationHistoryDTO examinationHistoryDTO) {
        log.debug("Request to save ExaminationHistory : {}", examinationHistoryDTO);
        ExaminationHistory examinationHistory = examinationHistoryMapper.toEntity(examinationHistoryDTO);
        examinationHistory = examinationHistoryRepository.save(examinationHistory);
        return examinationHistoryMapper.toDto(examinationHistory);
    }

    @Override
    public Optional<ExaminationHistoryDTO> partialUpdate(ExaminationHistoryDTO examinationHistoryDTO) {
        log.debug("Request to partially update ExaminationHistory : {}", examinationHistoryDTO);

        return examinationHistoryRepository
            .findById(examinationHistoryDTO.getId())
            .map(
                existingExaminationHistory -> {
                    examinationHistoryMapper.partialUpdate(existingExaminationHistory, examinationHistoryDTO);
                    return existingExaminationHistory;
                }
            )
            .map(examinationHistoryRepository::save)
            .map(examinationHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExaminationHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExaminationHistories");
        return examinationHistoryRepository.findAll(pageable).map(examinationHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExaminationHistoryDTO> findOne(Long id) {
        log.debug("Request to get ExaminationHistory : {}", id);
        return examinationHistoryRepository.findById(id).map(examinationHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExaminationHistory : {}", id);
        examinationHistoryRepository.deleteById(id);
    }
}
