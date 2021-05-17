package com.medical.portal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccineTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccineType.class);
        VaccineType vaccineType1 = new VaccineType();
        vaccineType1.setId(1L);
        VaccineType vaccineType2 = new VaccineType();
        vaccineType2.setId(vaccineType1.getId());
        assertThat(vaccineType1).isEqualTo(vaccineType2);
        vaccineType2.setId(2L);
        assertThat(vaccineType1).isNotEqualTo(vaccineType2);
        vaccineType1.setId(null);
        assertThat(vaccineType1).isNotEqualTo(vaccineType2);
    }
}
