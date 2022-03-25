package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Developer;
import com.mycompany.myapp.domain.Skill;
import com.mycompany.myapp.projections.AmountDevByAge;
import com.mycompany.myapp.projections.AmountDevsBySkill;
import com.mycompany.myapp.repository.DeveloperRepository;
import com.mycompany.myapp.repository.SkillRepository;
import com.mycompany.myapp.service.dto.DeveloperDTO;
import com.mycompany.myapp.service.dto.DeveloperDTOWithName;
import com.mycompany.myapp.service.dto.DevsBySkillDTO;
import com.mycompany.myapp.service.mapper.DeveloperMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import liquibase.pro.packaged.lo;

/**
 * Service Implementation for managing {@link Developer}.
 */
@Service
@Transactional
public class DeveloperService {

    private final Logger log = LoggerFactory.getLogger(DeveloperService.class);

    private final DeveloperRepository developerRepository;

    private final DeveloperMapper developerMapper;

    private final SkillRepository skillRepository;

    public DeveloperService(DeveloperRepository developerRepository, DeveloperMapper developerMapper,
            SkillRepository skillRepository) {
        this.developerRepository = developerRepository;
        this.developerMapper = developerMapper;
        this.skillRepository = skillRepository;
    }

    /**
     * Save a developer.
     *
     * @param developerDTO the entity to save.
     * @return the persisted entity.
     */
    public DeveloperDTO save(DeveloperDTO developerDTO) {
        log.debug("Request to save Developer : {}", developerDTO);
        Developer developer = developerMapper.toEntity(developerDTO);
        developer = developerRepository.save(developer);
        return developerMapper.toDto(developer);
    }

    /**
     * Partially update a developer.
     *
     * @param developerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeveloperDTO> partialUpdate(DeveloperDTO developerDTO) {
        log.debug("Request to partially update Developer : {}", developerDTO);

        return developerRepository
                .findById(developerDTO.getId())
                .map(existingDeveloper -> {
                    developerMapper.partialUpdate(existingDeveloper, developerDTO);

                    return existingDeveloper;
                })
                .map(developerRepository::save)
                .map(developerMapper::toDto);
    }

    /**
     * Get all the developers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DeveloperDTO> findAll() {
        log.debug("Request to get all Developers");
        return developerRepository
                .findAllWithEagerRelationships()
                .stream()
                .map(developerMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the developers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DeveloperDTO> findAllWithEagerRelationships(Pageable pageable) {
        return developerRepository.findAllWithEagerRelationships(pageable).map(developerMapper::toDto);
    }

    /**
     * Get one developer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeveloperDTO> findOne(Long id) {
        log.debug("Request to get Developer : {}", id);
        return developerRepository.findOneWithEagerRelationships(id).map(developerMapper::toDto);
    }

    /**
     * Delete the developer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Developer : {}", id);
        developerRepository.deleteById(id);
    }

    public Integer countDevsByAge(Integer age) {
        if (age != null) {
            return developerRepository.countDevsByAge(age);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Age %d doesn't exists", age));
        }
    }

    public Integer countDevsBySkillWithSkillId(Long skillId) {
        Optional<Skill> result = skillRepository.findById(skillId);
        if (result.isPresent()) {
            return developerRepository.countDevsBySkill(skillId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Skill %d doesn't exists", skillId));
        }

    }

    public Float avgAgeDevsBySkill(Long skillId) {
        Optional<Skill> result = skillRepository.findById(skillId);
        if (result.isPresent()) {
            return developerRepository.avgAgeDevsBySkill(skillId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Skill %d doesn't exists", skillId));
        }
    }

    public List<DeveloperDTOWithName> findDeveloperListWithName() {
        return developerRepository.findAll()
                .stream()
                .map(developerMapper::toDtoWithName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<AmountDevByAge> getNumberOfDevsByAge() {

        return developerRepository.getNumberOfDevsByAge();
    }

    /**
     * HOLA
     */
    // public void getAvgDevsBySkill(){}

    public List<AmountDevsBySkill> getDevsBySkill() {
        return developerRepository.getCountDevsBySkill();
    }
}
