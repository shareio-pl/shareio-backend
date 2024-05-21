package org.shareio.backend.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;

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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private UserEntity owner;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressEntity address;
    private LocalDateTime creationDate;
    private Status status;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private UserEntity receiver;
    private LocalDateTime reservationDate;

    private String title;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String description;
    private UUID photoId;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private ReviewEntity review;
}
