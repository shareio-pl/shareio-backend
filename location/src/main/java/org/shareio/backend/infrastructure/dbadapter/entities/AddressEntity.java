package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;
    private UUID addressId;
    private String country;
    private String region;
    private String city;
    private String houseNumber;
    private String flatNumber;
    private String postCode;
    private Double latitude;
    private Double longitude;
}
