package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbId;
    private UUID reviewId;
    @OneToOne(cascade = CascadeType.ALL)
    private OfferEntity offer;
    private Integer value;
    private LocalDateTime date;
}