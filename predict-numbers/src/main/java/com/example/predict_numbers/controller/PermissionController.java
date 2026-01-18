package com.example.predict_numbers.controller;

import com.example.predict_numbers.dto.request.PermissionRequest;
import com.example.predict_numbers.dto.response.ApiResponse;
import com.example.predict_numbers.dto.response.PermissionResponse;
import com.example.predict_numbers.exception.AppException;
import com.example.predict_numbers.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PermissionController {
    private final PermissionService permissionService;

    @PreAuthorize("hasAuthority('PERMISSION:CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@Valid @RequestBody PermissionRequest permission) throws AppException {
        log.info("Creating permission: {}", permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<PermissionResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(this.permissionService.createPermission(permission))
                        .message("Successfully created")
                .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION:UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(@Valid @RequestBody PermissionRequest permission,@PathVariable Long id) throws AppException {
        return ResponseEntity.ok().body(ApiResponse.<PermissionResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.permissionService.updatePermission(permission, id))
                        .message("Successfully updated")
                .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION:DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id) throws AppException {
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder()
                        .code(HttpStatus.OK.value())
                        .message("Successfully deleted")
                .build());
    }

    @PreAuthorize("hasAuthority('PERMISSION:READ:ALL')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermission() {
        return ResponseEntity.ok().body(ApiResponse.<List<PermissionResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(this.permissionService.getAllPermission())
                        .message("Successfully get all permission")
                .build());
    }
}
