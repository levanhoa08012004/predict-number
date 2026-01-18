package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.dto.request.RoleRequest;
import com.example.predict_numbers.dto.response.RoleResponse;
import com.example.predict_numbers.entity.Permission;
import com.example.predict_numbers.entity.Role;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.exception.ResourceNotFoundException;
import com.example.predict_numbers.mapper.RoleMapper;
import com.example.predict_numbers.repository.RoleRepository;
import com.example.predict_numbers.service.PermissionService;
import com.example.predict_numbers.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;
    @Override
    public Role getByName(String name) {
        return this.roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }
    @Override
    public Role fetchRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role not found"));
    }
    @Override
    public RoleResponse getRoleById(Long id) {
        return this.roleMapper.toResponse(roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role not found")));
    }

    @Override
    public RoleResponse createRole(RoleRequest request) throws AppException {
        if (this.roleRepository.existsByName(request.getName())) {
            throw new ResourceNotFoundException("Role name already exists");
        }
        Role role = this.roleMapper.toEntity(request);
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionService.getByListPermissionId(request.getPermissionIds()));
            role.setPermissions(permissions);
        } else {
            role.setPermissions(new HashSet<>());
        }

        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(RoleRequest request, Long id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.getPermissions().clear();
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(this.permissionService.getByListPermissionId(request.getPermissionIds()));
            role.setPermissions(permissions);
        }
        return this.roleMapper.toResponse(this.roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return this.roleRepository.findAll().stream().map(roleMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Long id) throws AppException {
        Role role = this.roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("role not found"));
        this.roleRepository.delete(role);
    }
}
