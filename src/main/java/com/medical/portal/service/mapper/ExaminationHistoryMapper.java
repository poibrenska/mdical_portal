package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.ExaminationHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExaminationHistory} and its DTO {@link ExaminationHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { DoctorMapper.class, PatientMapper.class })
public interface ExaminationHistoryMapper extends EntityMapper<ExaminationHistoryDTO, ExaminationHistory> {
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "id")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "id")
    ExaminationHistoryDTO toDto(ExaminationHistory s);
}
