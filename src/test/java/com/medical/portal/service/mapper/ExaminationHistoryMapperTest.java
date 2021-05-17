package com.medical.portal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExaminationHistoryMapperTest {

    private ExaminationHistoryMapper examinationHistoryMapper;

    @BeforeEach
    public void setUp() {
        examinationHistoryMapper = new ExaminationHistoryMapperImpl();
    }
}
