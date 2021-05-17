package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.AppointmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { DoctorMapper.class, PatientMapper.class })
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "id")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "id")
    AppointmentDTO toDto(Appointment s);
}
