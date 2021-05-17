package com.medical.portal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.medical.portal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Documents.class);
        Documents documents1 = new Documents();
        documents1.setId(1L);
        Documents documents2 = new Documents();
        documents2.setId(documents1.getId());
        assertThat(documents1).isEqualTo(documents2);
        documents2.setId(2L);
        assertThat(documents1).isNotEqualTo(documents2);
        documents1.setId(null);
        assertThat(documents1).isNotEqualTo(documents2);
    }
}
