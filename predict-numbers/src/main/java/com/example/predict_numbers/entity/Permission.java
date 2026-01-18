package com.example.predict_numbers.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity{

    @Column(nullable = false, unique = true, length = 100)
    String name;


    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Role> roles;

}
