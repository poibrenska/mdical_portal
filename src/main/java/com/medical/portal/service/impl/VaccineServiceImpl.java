package com.medical.portal.service.impl;

import com.medical.portal.domain.Vaccine;
import com.medical.portal.repository.VaccineRepository;
import com.medical.portal.service.VaccineService;
import com.medical.portal.service.dto.VaccineDTO;
import com.medical.portal.service.mapper.VaccineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vaccine}.
 */
@Service
@Transactional
public class VaccineServiceImpl implements VaccineService {

    private final Logger log = LoggerFactory.getLogger(VaccineServiceImpl.class);

    private final VaccineRepository vaccineRepository;

    private final VaccineMapper vaccineMapper;

    public VaccineServiceImpl(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
    }

    @Override
    public VaccineDTO save(VaccineDTO vaccineDTO) {
        log.debug("Request to save Vaccine : {}", vaccineDTO);
        Vaccine vaccine = vaccineMapper.toEntity(vaccineDTO);
        vaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.toDto(vaccine);
    }

    @Override
    public Optional<VaccineDTO> partialUpdate(VaccineDTO vaccineDTO) {
        log.debug("Request to partially update Vaccine : {}", vaccineDTO);

        return vaccineRepository
            .findById(vaccineDTO.getId())
            .map(
                existingVaccine -> {
                    vaccineMapper.partialUpdate(existingVaccine, vaccineDTO);
                    return existingVaccine;
                }
            )
            .map(vaccineRepository::save)
            .map(vaccineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VaccineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vaccines");
        return vaccineRepository.findAll(pageable).map(vaccineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VaccineDTO> findOne(Long id) {
        log.debug("Request to get Vaccine : {}", id);
        return vaccineRepository.findById(id).map(vaccineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vaccine : {}", id);
        vaccineRepository.deleteById(id);
    }
}
