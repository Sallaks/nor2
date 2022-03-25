package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeveloperMapperTest {

    private DeveloperMapper developerMapper;

    @BeforeEach
    public void setUp() {
        developerMapper = new DeveloperMapperImpl();
    }
}
