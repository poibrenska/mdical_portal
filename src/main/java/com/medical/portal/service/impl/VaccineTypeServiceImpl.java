package com.medical.portal.service.impl;

import com.medical.portal.domain.VaccineType;
import com.medical.portal.repository.VaccineTypeRepository;
import com.medical.portal.service.VaccineTypeService;
import com.medical.portal.service.dto.VaccineTypeDTO;
import com.medical.portal.service.mapper.VaccineTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VaccineType}.
 */
@Service
@Transactional
public class VaccineTypeServiceImpl implements VaccineTypeService {

    private final Logger log = LoggerFactory.getLogger(VaccineTypeServiceImpl.class);

    private final VaccineTypeRepository vaccineTypeRepository;

    private final VaccineTypeMapper vaccineTypeMapper;

    public VaccineTypeServiceImpl(VaccineTypeRepository vaccineTypeRepository, VaccineTypeMapper vaccineTypeMapper) {
        this.vaccineTypeRepository = vaccineTypeRepository;
        this.vaccineTypeMapper = vaccineTypeMapper;
    }

    @Override
    public VaccineTypeDTO save(VaccineTypeDTO vaccineTypeDTO) {
        log.debug("Request to save VaccineType : {}", vaccineTypeDTO);
        VaccineType vaccineType = vaccineTypeMapper.toEntity(vaccineTypeDTO);
        vaccineType = vaccineTypeRepository.save(vaccineType);
        return vaccineTypeMapper.toDto(vaccineType);
    }

    @Override
    public Optional<VaccineTypeDTO> partialUpdate(VaccineTypeDTO vaccineTypeDTO) {
        log.debug("Request to partially update VaccineType : {}", vaccineTypeDTO);

        return vaccineTypeRepository
            .findById(vaccineTypeDTO.getId())
            .map(
                existingVaccineType -> {
                    vaccineTypeMapper.partialUpdate(existingVaccineType, vaccineTypeDTO);
                    return existingVaccineType;
                }
            )
            .map(vaccineTypeRepository::save)
            .map(vaccineTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VaccineTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VaccineTypes");
        return vaccineTypeRepository.findAll(pageable).map(vaccineTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VaccineTypeDTO> findOne(Long id) {
        log.debug("Request to get VaccineType : {}", id);
        return vaccineTypeRepository.findById(id).map(vaccineTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VaccineType : {}", id);
        vaccineTypeRepository.deleteById(id);
    }
}
