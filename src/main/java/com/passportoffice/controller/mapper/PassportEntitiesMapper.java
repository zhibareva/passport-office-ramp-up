package com.passportoffice.controller.mapper;

import com.passportoffice.dto.PassportDto;
import com.passportoffice.dto.response.PassportResponse;
import com.passportoffice.model.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PassportEntitiesMapper {
  @Mappings({
    @Mapping(target = "id", source = "passportDto.passportId"),
    @Mapping(target = "type", source = "passportDto.type"),
    @Mapping(target = "number", source = "passportDto.number"),
    @Mapping(target = "givenDate", source = "passportDto.givenDate"),
    @Mapping(target = "expirationDate", source = "passportDto.expirationDate"),
    @Mapping(target = "departmentCode", source = "passportDto.departmentCode"),
    @Mapping(target = "status", source = "passportDto.status")
  })
  PassportResponse toResponse(PassportDto passportDto);

  @Mappings({
    @Mapping(target = "passportId", source = "passport.passportId"),
    @Mapping(target = "type", source = "passport.type"),
    @Mapping(target = "number", source = "passport.number"),
    @Mapping(target = "givenDate", source = "passport.givenDate"),
    @Mapping(target = "expirationDate", source = "passport.expirationDate"),
    @Mapping(target = "departmentCode", source = "passport.departmentCode"),
    @Mapping(target = "status", source = "passport.status")
  })
  PassportDto toDto(Passport passport);
}
