package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.DoctorDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doctor} and its DTO {@link DoctorDTO}.
 */
@Mapper(componentModel = "spring", uses = { PatientMapper.class })
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {
    @Mapping(target = "patients", source = "patients", qualifiedByName = "idSet")
    DoctorDTO toDto(Doctor s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DoctorDTO toDtoId(Doctor doctor);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<DoctorDTO> toDtoIdSet(Set<Doctor> doctor);

    @Mapping(target = "removePatient", ignore = true)
    Doctor toEntity(DoctorDTO doctorDTO);
}
