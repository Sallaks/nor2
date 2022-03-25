package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SeniorityLevel;
import com.mycompany.myapp.repository.SeniorityLevelRepository;
import com.mycompany.myapp.service.dto.SeniorityLevelDTO;
import com.mycompany.myapp.service.mapper.SeniorityLevelMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SeniorityLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeniorityLevelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seniority-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeniorityLevelRepository seniorityLevelRepository;

    @Autowired
    private SeniorityLevelMapper seniorityLevelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeniorityLevelMockMvc;

    private SeniorityLevel seniorityLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeniorityLevel createEntity(EntityManager em) {
        SeniorityLevel seniorityLevel = new SeniorityLevel().name(DEFAULT_NAME);
        return seniorityLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeniorityLevel createUpdatedEntity(EntityManager em) {
        SeniorityLevel seniorityLevel = new SeniorityLevel().name(UPDATED_NAME);
        return seniorityLevel;
    }

    @BeforeEach
    public void initTest() {
        seniorityLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createSeniorityLevel() throws Exception {
        int databaseSizeBeforeCreate = seniorityLevelRepository.findAll().size();
        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);
        restSeniorityLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeCreate + 1);
        SeniorityLevel testSeniorityLevel = seniorityLevelList.get(seniorityLevelList.size() - 1);
        assertThat(testSeniorityLevel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSeniorityLevelWithExistingId() throws Exception {
        // Create the SeniorityLevel with an existing ID
        seniorityLevel.setId(1L);
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        int databaseSizeBeforeCreate = seniorityLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeniorityLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = seniorityLevelRepository.findAll().size();
        // set the field null
        seniorityLevel.setName(null);

        // Create the SeniorityLevel, which fails.
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        restSeniorityLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeniorityLevels() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        // Get all the seniorityLevelList
        restSeniorityLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seniorityLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSeniorityLevel() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        // Get the seniorityLevel
        restSeniorityLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, seniorityLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seniorityLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSeniorityLevel() throws Exception {
        // Get the seniorityLevel
        restSeniorityLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeniorityLevel() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();

        // Update the seniorityLevel
        SeniorityLevel updatedSeniorityLevel = seniorityLevelRepository.findById(seniorityLevel.getId()).get();
        // Disconnect from session so that the updates on updatedSeniorityLevel are not directly saved in db
        em.detach(updatedSeniorityLevel);
        updatedSeniorityLevel.name(UPDATED_NAME);
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(updatedSeniorityLevel);

        restSeniorityLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seniorityLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
        SeniorityLevel testSeniorityLevel = seniorityLevelList.get(seniorityLevelList.size() - 1);
        assertThat(testSeniorityLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seniorityLevelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeniorityLevelWithPatch() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();

        // Update the seniorityLevel using partial update
        SeniorityLevel partialUpdatedSeniorityLevel = new SeniorityLevel();
        partialUpdatedSeniorityLevel.setId(seniorityLevel.getId());

        partialUpdatedSeniorityLevel.name(UPDATED_NAME);

        restSeniorityLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeniorityLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeniorityLevel))
            )
            .andExpect(status().isOk());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
        SeniorityLevel testSeniorityLevel = seniorityLevelList.get(seniorityLevelList.size() - 1);
        assertThat(testSeniorityLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSeniorityLevelWithPatch() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();

        // Update the seniorityLevel using partial update
        SeniorityLevel partialUpdatedSeniorityLevel = new SeniorityLevel();
        partialUpdatedSeniorityLevel.setId(seniorityLevel.getId());

        partialUpdatedSeniorityLevel.name(UPDATED_NAME);

        restSeniorityLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeniorityLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeniorityLevel))
            )
            .andExpect(status().isOk());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
        SeniorityLevel testSeniorityLevel = seniorityLevelList.get(seniorityLevelList.size() - 1);
        assertThat(testSeniorityLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seniorityLevelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeniorityLevel() throws Exception {
        int databaseSizeBeforeUpdate = seniorityLevelRepository.findAll().size();
        seniorityLevel.setId(count.incrementAndGet());

        // Create the SeniorityLevel
        SeniorityLevelDTO seniorityLevelDTO = seniorityLevelMapper.toDto(seniorityLevel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeniorityLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seniorityLevelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeniorityLevel in the database
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeniorityLevel() throws Exception {
        // Initialize the database
        seniorityLevelRepository.saveAndFlush(seniorityLevel);

        int databaseSizeBeforeDelete = seniorityLevelRepository.findAll().size();

        // Delete the seniorityLevel
        restSeniorityLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, seniorityLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SeniorityLevel> seniorityLevelList = seniorityLevelRepository.findAll();
        assertThat(seniorityLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
