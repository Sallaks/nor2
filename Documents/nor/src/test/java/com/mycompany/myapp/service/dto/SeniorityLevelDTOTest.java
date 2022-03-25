package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeniorityLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeniorityLevelDTO.class);
        SeniorityLevelDTO seniorityLevelDTO1 = new SeniorityLevelDTO();
        seniorityLevelDTO1.setId(1L);
        SeniorityLevelDTO seniorityLevelDTO2 = new SeniorityLevelDTO();
        assertThat(seniorityLevelDTO1).isNotEqualTo(seniorityLevelDTO2);
        seniorityLevelDTO2.setId(seniorityLevelDTO1.getId());
        assertThat(seniorityLevelDTO1).isEqualTo(seniorityLevelDTO2);
        seniorityLevelDTO2.setId(2L);
        assertThat(seniorityLevelDTO1).isNotEqualTo(seniorityLevelDTO2);
        seniorityLevelDTO1.setId(null);
        assertThat(seniorityLevelDTO1).isNotEqualTo(seniorityLevelDTO2);
    }
}
