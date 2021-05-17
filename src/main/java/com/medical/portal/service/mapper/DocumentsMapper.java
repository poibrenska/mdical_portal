package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.DocumentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documents} and its DTO {@link DocumentsDTO}.
 */
@Mapper(componentModel = "spring", uses = { DoctorMapper.class, PatientMapper.class })
public interface DocumentsMapper extends EntityMapper<DocumentsDTO, Documents> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "id")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "id")
    DocumentsDTO toDto(Documents s);
}
