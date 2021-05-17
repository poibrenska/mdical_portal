package com.medical.portal.service.mapper;

import com.medical.portal.domain.*;
import com.medical.portal.service.dto.VaccineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vaccine} and its DTO {@link VaccineDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VaccineMapper extends EntityMapper<VaccineDTO, Vaccine> {}
