package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.dto.request.PermissionRequest;
import com.example.predict_numbers.dto.response.PermissionResponse;
import com.example.predict_numbers.entity.Permission;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.exception.ResourceNotFoundException;
import com.example.predict_numbers.mapper.PermissionMapper;
import com.example.predict_numbers.repository.PermissionRepository;
import com.example.predict_numbers.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) throws AppException {
        if (this.permissionRepository.existsByName(request.getName())) {
            throw new ResourceNotFoundException("Permission already exists");
        }
        Permission permission = this.permissionMapper.toEntity(request);
        return this.permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse updatePermission(PermissionRequest request, Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        permission.setName(request.getName());

        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionResponse> getAllPermission() {
        return this.permissionRepository.findAll().stream().map(permissionMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePermission(long id) throws ResourceNotFoundException {
        Permission permission =
                this.permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
        this.permissionRepository.delete(permission);
    }

    @Override
    public List<Permission> getByListPermissionId(List<Long> id) {
        return this.permissionRepository.findByIdIn(id);
    }
}
