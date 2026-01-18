package com.example.predict_numbers.mapper;

import com.example.predict_numbers.dto.request.RoleRequest;
import com.example.predict_numbers.dto.response.RoleResponse;
import com.example.predict_numbers.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest dto);

    @Mapping(target = "permissions", source = "permissions")
    RoleResponse toResponse(Role entity);
}