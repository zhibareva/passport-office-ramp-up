package com.passportoffice.utils.mapper;

import com.passportoffice.dto.PersonDto;
import com.passportoffice.dto.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PersonEntitiesMapper {
    @Mappings({
            @Mapping(target = "id", source = "personDto.id"),
            @Mapping(target = "firstName", source = "personDto.firstName"),
            @Mapping(target = "lastName", source = "personDto.lastName"),
            @Mapping(target = "dateOfBirth", source = "personDto.dateOfBirth"),
            @Mapping(target = "birthCountry", source = "personDto.birthCountry")
    })
    PersonResponse toResponse(PersonDto personDto);
}
