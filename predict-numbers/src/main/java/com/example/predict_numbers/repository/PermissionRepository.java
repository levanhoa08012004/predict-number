package com.example.predict_numbers.repository;

import com.example.predict_numbers.entity.Permission;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {


    boolean existsByName(String name);

    List<Permission> findByIdIn(List<Long> id);
}
