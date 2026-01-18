package com.example.predict_numbers.mapper;

import com.example.predict_numbers.dto.request.PermissionRequest;
import com.example.predict_numbers.dto.response.PermissionResponse;
import com.example.predict_numbers.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PermissionMapper {

    Permission toEntity(PermissionRequest dto);

    PermissionResponse toResponse(Permission entity);
}
