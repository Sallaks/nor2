package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Developer;
import com.mycompany.myapp.service.dto.DeveloperDTO;
import com.mycompany.myapp.service.dto.DeveloperDTOWithName;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Developer} and its DTO {@link DeveloperDTO}.
 */
@Mapper(componentModel = "spring", uses = { SkillMapper.class })
public interface DeveloperMapper extends EntityMapper<DeveloperDTO, Developer> {
    @Mapping(target = "skills", source = "skills", qualifiedByName = "nameSet")
    DeveloperDTO toDto(Developer s);

    @Mapping(target = "removeSkill", ignore = true)
    Developer toEntity(DeveloperDTO developerDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeveloperDTOWithName toDtoWithName(Developer d);

}
