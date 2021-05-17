package com.medical.portal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentsMapperTest {

    private DocumentsMapper documentsMapper;

    @BeforeEach
    public void setUp() {
        documentsMapper = new DocumentsMapperImpl();
    }
}
