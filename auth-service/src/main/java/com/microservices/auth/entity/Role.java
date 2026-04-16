package com.microservices.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name="roles",
        indexes = {
                @Index(name = "idx_role", columnList = "role")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends Auditor {

    @Column(name = "role", nullable = false, unique = true)
    private String role;

}
