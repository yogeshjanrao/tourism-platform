package com.microservices.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name="users",
        indexes = {
                @Index(name = "idx_email", columnList = "email")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends Auditor {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact", nullable = true)
    private String contactNo;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified;

    @Column(name = "is_contact_verified", nullable = false)
    private Boolean isContactVerified;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private List<Role> roles;

}