package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;
    private UUID userId;
    private String email;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private UUID photoId;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressEntity address;
    @OneToOne(cascade = CascadeType.ALL)
    private SecurityEntity security;
}
