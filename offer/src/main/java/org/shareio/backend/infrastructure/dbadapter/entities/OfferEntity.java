package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shareio.backend.core.model.vo.Condition;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "offers")
@AllArgsConstructor
@NoArgsConstructor
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;
    private UUID offerId;
    @ManyToOne(cascade = CascadeType.ALL)
    private UserEntity owner;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressEntity address;
    private LocalDateTime creationDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private UserEntity receiver;
    private LocalDateTime reservationDate;

    private String title;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private String description;
    private UUID photoId;
}
