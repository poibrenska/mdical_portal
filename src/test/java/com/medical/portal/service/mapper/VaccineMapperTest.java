package com.medical.portal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccineMapperTest {

    private VaccineMapper vaccineMapper;

    @BeforeEach
    public void setUp() {
        vaccineMapper = new VaccineMapperImpl();
    }
}
