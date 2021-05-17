package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.HospitalDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hospital} and its DTO {@link HospitalDTO}.
 */
@Mapper(componentModel = "spring", uses = { DoctorMapper.class })
public interface HospitalMapper extends EntityMapper<HospitalDTO, Hospital> {
    @Mapping(target = "doctors", source = "doctors", qualifiedByName = "idSet")
    HospitalDTO toDto(Hospital s);

    @Mapping(target = "removeDoctor", ignore = true)
    Hospital toEntity(HospitalDTO hospitalDTO);
}
