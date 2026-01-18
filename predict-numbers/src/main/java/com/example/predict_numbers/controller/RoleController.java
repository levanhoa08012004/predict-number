package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.request.RoleRequest;
import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.dto.response.PermissionResponse;
import com.example.predict_numbers.dto.response.RoleResponse;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE:CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleRequest request) throws AppException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<RoleResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(this.roleService.createRole(request))
                        .message("Role created")
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@Valid @RequestBody RoleRequest role,@PathVariable Long id) throws AppException {
        return ResponseEntity.ok().body(ApiResponse.<RoleResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.roleService.updateRole(role,id))
                        .message("Role updated")
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE:DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) throws AppException {
        this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder()
                        .code(HttpStatus.OK.value())
                        .message("Role deleted")
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE:READ:ALL')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        return ResponseEntity.ok().body(ApiResponse.<List<RoleResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.roleService.getAllRoles())
                        .message("Get all role")
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE:READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok().body(ApiResponse.<RoleResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.roleService.getRoleById(id))
                        .message("get role by id")
                .build());
    }
}