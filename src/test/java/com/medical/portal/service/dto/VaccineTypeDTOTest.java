package com.medical.portal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaccineTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VaccineTypeDTO.class);
        VaccineTypeDTO vaccineTypeDTO1 = new VaccineTypeDTO();
        vaccineTypeDTO1.setId(1L);
        VaccineTypeDTO vaccineTypeDTO2 = new VaccineTypeDTO();
        assertThat(vaccineTypeDTO1).isNotEqualTo(vaccineTypeDTO2);
        vaccineTypeDTO2.setId(vaccineTypeDTO1.getId());
        assertThat(vaccineTypeDTO1).isEqualTo(vaccineTypeDTO2);
        vaccineTypeDTO2.setId(2L);
        assertThat(vaccineTypeDTO1).isNotEqualTo(vaccineTypeDTO2);
        vaccineTypeDTO1.setId(null);
        assertThat(vaccineTypeDTO1).isNotEqualTo(vaccineTypeDTO2);
    }
}
