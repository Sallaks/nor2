package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Skill;
import com.mycompany.myapp.service.dto.SkillDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring", uses = { SeniorityLevelMapper.class })
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "seniorityLevel", source = "seniorityLevel", qualifiedByName = "name")
    SkillDTO toDto(Skill s);

    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<SkillDTO> toDtoNameSet(Set<Skill> skill);
}
