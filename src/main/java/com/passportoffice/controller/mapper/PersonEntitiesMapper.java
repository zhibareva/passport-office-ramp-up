package com.passportoffice.controller.mapper;

import com.passportoffice.dto.response.PersonResponse;
import com.passportoffice.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PersonEntitiesMapper {
  @Mappings({
    @Mapping(target = "id", source = "person.id"),
    @Mapping(target = "firstName", source = "person.firstName"),
    @Mapping(target = "lastName", source = "person.lastName"),
    @Mapping(target = "dateOfBirth", source = "person.dateOfBirth"),
    @Mapping(target = "birthCountry", source = "person.birthCountry")
  })
  PersonResponse toResponse(Person person);
}
