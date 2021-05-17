package com.medical.portal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationHistory.class);
        ExaminationHistory examinationHistory1 = new ExaminationHistory();
        examinationHistory1.setId(1L);
        ExaminationHistory examinationHistory2 = new ExaminationHistory();
        examinationHistory2.setId(examinationHistory1.getId());
        assertThat(examinationHistory1).isEqualTo(examinationHistory2);
        examinationHistory2.setId(2L);
        assertThat(examinationHistory1).isNotEqualTo(examinationHistory2);
        examinationHistory1.setId(null);
        assertThat(examinationHistory1).isNotEqualTo(examinationHistory2);
    }
}
