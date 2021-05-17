package com.medical.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccineDTO.class);
        VaccineDTO vaccineDTO1 = new VaccineDTO();
        vaccineDTO1.setId(1L);
        VaccineDTO vaccineDTO2 = new VaccineDTO();
        assertThat(vaccineDTO1).isNotEqualTo(vaccineDTO2);
        vaccineDTO2.setId(vaccineDTO1.getId());
        assertThat(vaccineDTO1).isEqualTo(vaccineDTO2);
        vaccineDTO2.setId(2L);
        assertThat(vaccineDTO1).isNotEqualTo(vaccineDTO2);
        vaccineDTO1.setId(null);
        assertThat(vaccineDTO1).isNotEqualTo(vaccineDTO2);
    }
}
