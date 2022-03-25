package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeniorityLevelMapperTest {

    private SeniorityLevelMapper seniorityLevelMapper;

    @BeforeEach
    public void setUp() {
        seniorityLevelMapper = new SeniorityLevelMapperImpl();
    }
}
