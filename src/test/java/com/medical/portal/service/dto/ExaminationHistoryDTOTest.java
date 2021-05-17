package com.medical.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationHistoryDTO.class);
        ExaminationHistoryDTO examinationHistoryDTO1 = new ExaminationHistoryDTO();
        examinationHistoryDTO1.setId(1L);
        ExaminationHistoryDTO examinationHistoryDTO2 = new ExaminationHistoryDTO();
        assertThat(examinationHistoryDTO1).isNotEqualTo(examinationHistoryDTO2);
        examinationHistoryDTO2.setId(examinationHistoryDTO1.getId());
        assertThat(examinationHistoryDTO1).isEqualTo(examinationHistoryDTO2);
        examinationHistoryDTO2.setId(2L);
        assertThat(examinationHistoryDTO1).isNotEqualTo(examinationHistoryDTO2);
        examinationHistoryDTO1.setId(null);
        assertThat(examinationHistoryDTO1).isNotEqualTo(examinationHistoryDTO2);
    }
}
