package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeniorityLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeniorityLevel.class);
        SeniorityLevel seniorityLevel1 = new SeniorityLevel();
        seniorityLevel1.setId(1L);
        SeniorityLevel seniorityLevel2 = new SeniorityLevel();
        seniorityLevel2.setId(seniorityLevel1.getId());
        assertThat(seniorityLevel1).isEqualTo(seniorityLevel2);
        seniorityLevel2.setId(2L);
        assertThat(seniorityLevel1).isNotEqualTo(seniorityLevel2);
        seniorityLevel1.setId(null);
        assertThat(seniorityLevel1).isNotEqualTo(seniorityLevel2);
    }
}
