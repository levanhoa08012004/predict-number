package com.example.predict_numbers.service;

import com.example.predict_numbers.dto.request.PermissionRequest;
import com.example.predict_numbers.dto.response.PermissionResponse;
import com.example.predict_numbers.dto.response.RoleResponse;
import com.example.predict_numbers.entity.Permission;

import java.util.List;

public interface PermissionService {

    PermissionResponse createPermission(PermissionRequest request);
    PermissionResponse updatePermission(PermissionRequest request, Long id);
    List<PermissionResponse> getAllPermission();
    void deletePermission(long id);
    List<Permission> getByListPermissionId(List<Long> id);
}
