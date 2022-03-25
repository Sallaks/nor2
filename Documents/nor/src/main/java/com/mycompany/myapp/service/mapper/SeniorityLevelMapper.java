package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SeniorityLevel;
import com.mycompany.myapp.service.dto.SeniorityLevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SeniorityLevel} and its DTO {@link SeniorityLevelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SeniorityLevelMapper extends EntityMapper<SeniorityLevelDTO, SeniorityLevel> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SeniorityLevelDTO toDtoName(SeniorityLevel seniorityLevel);
}
