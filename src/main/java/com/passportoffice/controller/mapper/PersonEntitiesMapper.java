package com.passportoffice.controller.mapper;

import com.passportoffice.dto.PersonDto;
import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.model.Person;
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

  @Mappings({
    @Mapping(target = "id", source = "person.id"),
    @Mapping(target = "firstName", source = "person.firstName"),
    @Mapping(target = "lastName", source = "person.lastName"),
    @Mapping(target = "dateOfBirth", source = "person.dateOfBirth"),
    @Mapping(target = "birthCountry", source = "person.birthCountry")
  })
  PersonDto toDto(Person person);
}
