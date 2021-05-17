package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.PatientDTO;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    PatientDTO toDto(Patient s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoId(Patient patient);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<PatientDTO> toDtoIdSet(Set<Patient> patient);

    default Patient fromId(Long id) {
        if (id == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(id);
        return patient;
    }

    default Set<String> fromSet(Set<Authority> authoritySet){
        if(authoritySet==null)
            return Collections.emptySet();
        return authoritySet.stream().map(Authority::getName).collect(Collectors.toSet());
    }
}
