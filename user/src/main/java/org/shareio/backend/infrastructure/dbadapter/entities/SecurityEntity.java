package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shareio.backend.core.model.vo.AccountType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "security")
@AllArgsConstructor
@NoArgsConstructor
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;
    private String pwHash;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
}
