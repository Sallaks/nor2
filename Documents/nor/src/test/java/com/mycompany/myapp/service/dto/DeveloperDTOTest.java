package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeveloperDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeveloperDTO.class);
        DeveloperDTO developerDTO1 = new DeveloperDTO();
        developerDTO1.setId(1L);
        DeveloperDTO developerDTO2 = new DeveloperDTO();
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
        developerDTO2.setId(developerDTO1.getId());
        assertThat(developerDTO1).isEqualTo(developerDTO2);
        developerDTO2.setId(2L);
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
        developerDTO1.setId(null);
        assertThat(developerDTO1).isNotEqualTo(developerDTO2);
    }
}
