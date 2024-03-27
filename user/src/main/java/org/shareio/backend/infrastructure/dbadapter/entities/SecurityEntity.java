package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "security")
@AllArgsConstructor
@NoArgsConstructor
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long securityId;
    private String pwHash;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
}
