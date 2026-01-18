package com.example.predict_numbers.service;

import com.example.predict_numbers.dto.request.RoleRequest;
import com.example.predict_numbers.dto.response.RoleResponse;
import com.example.predict_numbers.entity.Role;

import java.util.List;

public interface RoleService {
    Role getByName(String name);
    Role fetchRoleById(Long id);
    RoleResponse getRoleById(Long id);
    RoleResponse createRole(RoleRequest request);
    RoleResponse updateRole(RoleRequest request, Long id);
    List<RoleResponse> getAllRoles();
    void deleteRole(Long id);
}
