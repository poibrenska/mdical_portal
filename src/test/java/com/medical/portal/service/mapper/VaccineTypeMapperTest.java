package com.medical.portal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccineTypeMapperTest {

    private VaccineTypeMapper vaccineTypeMapper;

    @BeforeEach
    public void setUp() {
        vaccineTypeMapper = new VaccineTypeMapperImpl();
    }
}
