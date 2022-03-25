package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.projections.AmountDevByAge;
import com.mycompany.myapp.projections.AmountDevsBySkill;
import com.mycompany.myapp.repository.DeveloperRepository;
import com.mycompany.myapp.service.DeveloperService;
import com.mycompany.myapp.service.dto.DeveloperDTO;
import com.mycompany.myapp.service.dto.DeveloperDTOWithName;
import com.mycompany.myapp.service.dto.DevsBySkillDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Developer}.
 */
@RestController
@RequestMapping("/api")
public class DeveloperResource {

    private final Logger log = LoggerFactory.getLogger(DeveloperResource.class);

    private static final String ENTITY_NAME = "developer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeveloperService developerService;

    private final DeveloperRepository developerRepository;

    public DeveloperResource(DeveloperService developerService, DeveloperRepository developerRepository) {
        this.developerService = developerService;
        this.developerRepository = developerRepository;
    }

    /**
     * {@code POST  /developers} : Create a new developer.
     *
     * @param developerDTO the developerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new developerDTO, or with status {@code 400 (Bad Request)}
     *         if the developer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/developers")
    public ResponseEntity<DeveloperDTO> createDeveloper(@RequestBody DeveloperDTO developerDTO)
            throws URISyntaxException {
        log.debug("REST request to save Developer : {}", developerDTO);
        if (developerDTO.getId() != null) {
            throw new BadRequestAlertException("A new developer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeveloperDTO result = developerService.save(developerDTO);
        return ResponseEntity
                .created(new URI("/api/developers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                        result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /developers/:id} : Updates an existing developer.
     *
     * @param id           the id of the developerDTO to save.
     * @param developerDTO the developerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated developerDTO,
     *         or with status {@code 400 (Bad Request)} if the developerDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         developerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/developers/{id}")
    public ResponseEntity<DeveloperDTO> updateDeveloper(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to update Developer : {}, {}", id, developerDTO);
        if (developerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, developerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!developerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeveloperDTO result = developerService.save(developerDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        developerDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /developers/:id} : Partial updates given fields of an existing
     * developer, field will ignore if it is null
     *
     * @param id           the id of the developerDTO to save.
     * @param developerDTO the developerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated developerDTO,
     *         or with status {@code 400 (Bad Request)} if the developerDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the developerDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         developerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/developers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeveloperDTO> partialUpdateDeveloper(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody DeveloperDTO developerDTO) throws URISyntaxException {
        log.debug("REST request to partial update Developer partially : {}, {}", id, developerDTO);
        if (developerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, developerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!developerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeveloperDTO> result = developerService.partialUpdate(developerDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        developerDTO.getId().toString()));
    }

    /**
     * {@code GET  /developers} : get all the developers.
     *
     * @param eagerload flag to eager load entities from relationships (This is
     *                  applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of developers in body.
     */
    @GetMapping("/developers")
    public List<DeveloperDTO> getAllDevelopers(
            @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Developers");
        return developerService.findAll();
    }

    /**
     * {@code GET  /developers/:id} : get the "id" developer.
     *
     * @param id the id of the developerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the developerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/developers/{id}")
    public ResponseEntity<DeveloperDTO> getDeveloper(@PathVariable Long id) {
        log.debug("REST request to get Developer : {}", id);
        Optional<DeveloperDTO> developerDTO = developerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(developerDTO);
    }

    @GetMapping("/developers/count/{age}")
    public ResponseEntity<Integer> countDevsByAge(@PathVariable Integer age) {
        log.debug("REST request to get amount developers by age : {}", age);
        return new ResponseEntity<>(developerService.countDevsByAge(age), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/developers/skills/count/{skillId}")
    public ResponseEntity<Integer> countDevsBySkillWithSkillId(@PathVariable Long skillId) {
        log.debug("Request to get amount Developers by Skill id: {}", skillId);
        return new ResponseEntity<>(developerService.countDevsBySkillWithSkillId(skillId), HttpStatus.OK);
    }

    @GetMapping("/developers/skill/avgAge/{skillId}")
    public ResponseEntity<Float> avgAgeDevsBySkill(@PathVariable Long skillId) {
        log.debug("Request to get average age of Developers by Skill id: {}", skillId);
        return new ResponseEntity<>(developerService.avgAgeDevsBySkill(skillId), HttpStatus.OK);

    }

    @GetMapping("/developers/names")
    public List<DeveloperDTOWithName> getAllDevelopersWithName() {
        log.debug("REST request to get all Developers");
        return developerService.findDeveloperListWithName();
    }

    @GetMapping("/developers/amount/age")
    public ResponseEntity<List<AmountDevByAge>> getNumberOfDevsByAge() {
        return new ResponseEntity<>(developerService.getNumberOfDevsByAge(), HttpStatus.OK);
    }

    @GetMapping("/developers/devs")
    public ResponseEntity<List<AmountDevsBySkill>> devsWithSkills() {
        return new ResponseEntity<>(developerService.getDevsBySkill(), HttpStatus.OK);
    }

    /**
     * {@code DELETE  /developers/:id} : delete the "id" developer.
     *
     * @param id the id of the developerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/developers/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long id) {
        log.debug("REST request to delete Developer : {}", id);
        developerService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
