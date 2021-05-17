package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.VaccineTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VaccineType} and its DTO {@link VaccineTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VaccineTypeMapper extends EntityMapper<VaccineTypeDTO, VaccineType> {}
