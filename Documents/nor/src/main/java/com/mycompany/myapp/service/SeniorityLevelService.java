package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SeniorityLevel;
import com.mycompany.myapp.repository.SeniorityLevelRepository;
import com.mycompany.myapp.service.dto.SeniorityLevelDTO;
import com.mycompany.myapp.service.mapper.SeniorityLevelMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SeniorityLevel}.
 */
@Service
@Transactional
public class SeniorityLevelService {

    private final Logger log = LoggerFactory.getLogger(SeniorityLevelService.class);

    private final SeniorityLevelRepository seniorityLevelRepository;

    private final SeniorityLevelMapper seniorityLevelMapper;

    public SeniorityLevelService(SeniorityLevelRepository seniorityLevelRepository, SeniorityLevelMapper seniorityLevelMapper) {
        this.seniorityLevelRepository = seniorityLevelRepository;
        this.seniorityLevelMapper = seniorityLevelMapper;
    }

    /**
     * Save a seniorityLevel.
     *
     * @param seniorityLevelDTO the entity to save.
     * @return the persisted entity.
     */
    public SeniorityLevelDTO save(SeniorityLevelDTO seniorityLevelDTO) {
        log.debug("Request to save SeniorityLevel : {}", seniorityLevelDTO);
        SeniorityLevel seniorityLevel = seniorityLevelMapper.toEntity(seniorityLevelDTO);
        seniorityLevel = seniorityLevelRepository.save(seniorityLevel);
        return seniorityLevelMapper.toDto(seniorityLevel);
    }

    /**
     * Partially update a seniorityLevel.
     *
     * @param seniorityLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SeniorityLevelDTO> partialUpdate(SeniorityLevelDTO seniorityLevelDTO) {
        log.debug("Request to partially update SeniorityLevel : {}", seniorityLevelDTO);

        return seniorityLevelRepository
            .findById(seniorityLevelDTO.getId())
            .map(existingSeniorityLevel -> {
                seniorityLevelMapper.partialUpdate(existingSeniorityLevel, seniorityLevelDTO);

                return existingSeniorityLevel;
            })
            .map(seniorityLevelRepository::save)
            .map(seniorityLevelMapper::toDto);
    }

    /**
     * Get all the seniorityLevels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SeniorityLevelDTO> findAll() {
        log.debug("Request to get all SeniorityLevels");
        return seniorityLevelRepository
            .findAll()
            .stream()
            .map(seniorityLevelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one seniorityLevel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SeniorityLevelDTO> findOne(Long id) {
        log.debug("Request to get SeniorityLevel : {}", id);
        return seniorityLevelRepository.findById(id).map(seniorityLevelMapper::toDto);
    }

    /**
     * Delete the seniorityLevel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SeniorityLevel : {}", id);
        seniorityLevelRepository.deleteById(id);
    }
}
