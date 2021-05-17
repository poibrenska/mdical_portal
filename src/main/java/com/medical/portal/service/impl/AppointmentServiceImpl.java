package com.medical.portal.service.impl;

import com.medical.portal.domain.Appointment;
import com.medical.portal.repository.AppointmentRepository;
import com.medical.portal.service.AppointmentService;
import com.medical.portal.service.dto.AppointmentDTO;
import com.medical.portal.service.mapper.AppointmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appointment}.
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.debug("Request to save Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public Optional<AppointmentDTO> partialUpdate(AppointmentDTO appointmentDTO) {
        log.debug("Request to partially update Appointment : {}", appointmentDTO);

        return appointmentRepository
            .findById(appointmentDTO.getId())
            .map(
                existingAppointment -> {
                    appointmentMapper.partialUpdate(existingAppointment, appointmentDTO);
                    return existingAppointment;
                }
            )
            .map(appointmentRepository::save)
            .map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentDTO> findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }
}
