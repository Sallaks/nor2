package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SeniorityLevelRepository;
import com.mycompany.myapp.service.SeniorityLevelService;
import com.mycompany.myapp.service.dto.SeniorityLevelDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SeniorityLevel}.
 */
@RestController
@RequestMapping("/api")
public class SeniorityLevelResource {

    private final Logger log = LoggerFactory.getLogger(SeniorityLevelResource.class);

    private static final String ENTITY_NAME = "seniorityLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeniorityLevelService seniorityLevelService;

    private final SeniorityLevelRepository seniorityLevelRepository;

    public SeniorityLevelResource(SeniorityLevelService seniorityLevelService, SeniorityLevelRepository seniorityLevelRepository) {
        this.seniorityLevelService = seniorityLevelService;
        this.seniorityLevelRepository = seniorityLevelRepository;
    }

    /**
     * {@code POST  /seniority-levels} : Create a new seniorityLevel.
     *
     * @param seniorityLevelDTO the seniorityLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seniorityLevelDTO, or with status {@code 400 (Bad Request)} if the seniorityLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seniority-levels")
    public ResponseEntity<SeniorityLevelDTO> createSeniorityLevel(@Valid @RequestBody SeniorityLevelDTO seniorityLevelDTO)
        throws URISyntaxException {
        log.debug("REST request to save SeniorityLevel : {}", seniorityLevelDTO);
        if (seniorityLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new seniorityLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeniorityLevelDTO result = seniorityLevelService.save(seniorityLevelDTO);
        return ResponseEntity
            .created(new URI("/api/seniority-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seniority-levels/:id} : Updates an existing seniorityLevel.
     *
     * @param id the id of the seniorityLevelDTO to save.
     * @param seniorityLevelDTO the seniorityLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seniorityLevelDTO,
     * or with status {@code 400 (Bad Request)} if the seniorityLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seniorityLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seniority-levels/{id}")
    public ResponseEntity<SeniorityLevelDTO> updateSeniorityLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeniorityLevelDTO seniorityLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SeniorityLevel : {}, {}", id, seniorityLevelDTO);
        if (seniorityLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seniorityLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seniorityLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SeniorityLevelDTO result = seniorityLevelService.save(seniorityLevelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seniorityLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seniority-levels/:id} : Partial updates given fields of an existing seniorityLevel, field will ignore if it is null
     *
     * @param id the id of the seniorityLevelDTO to save.
     * @param seniorityLevelDTO the seniorityLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seniorityLevelDTO,
     * or with status {@code 400 (Bad Request)} if the seniorityLevelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the seniorityLevelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the seniorityLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seniority-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeniorityLevelDTO> partialUpdateSeniorityLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeniorityLevelDTO seniorityLevelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SeniorityLevel partially : {}, {}", id, seniorityLevelDTO);
        if (seniorityLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seniorityLevelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seniorityLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeniorityLevelDTO> result = seniorityLevelService.partialUpdate(seniorityLevelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seniorityLevelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /seniority-levels} : get all the seniorityLevels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seniorityLevels in body.
     */
    @GetMapping("/seniority-levels")
    public List<SeniorityLevelDTO> getAllSeniorityLevels() {
        log.debug("REST request to get all SeniorityLevels");
        return seniorityLevelService.findAll();
    }

    /**
     * {@code GET  /seniority-levels/:id} : get the "id" seniorityLevel.
     *
     * @param id the id of the seniorityLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seniorityLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seniority-levels/{id}")
    public ResponseEntity<SeniorityLevelDTO> getSeniorityLevel(@PathVariable Long id) {
        log.debug("REST request to get SeniorityLevel : {}", id);
        Optional<SeniorityLevelDTO> seniorityLevelDTO = seniorityLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seniorityLevelDTO);
    }

    /**
     * {@code DELETE  /seniority-levels/:id} : delete the "id" seniorityLevel.
     *
     * @param id the id of the seniorityLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seniority-levels/{id}")
    public ResponseEntity<Void> deleteSeniorityLevel(@PathVariable Long id) {
        log.debug("REST request to delete SeniorityLevel : {}", id);
        seniorityLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
