package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.in.ReserveOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferFullDaoInterface;
import org.shareio.backend.core.usecases.port.out.SaveOfferCommandInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ReserveOfferUseCaseService implements ReserveOfferUseCaseInterface {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    GetOfferFullDaoInterface getOfferFullDaoInterface;
    SaveOfferCommandInterface saveOfferCommandInterface;

    @Override
    public UUID reserveOffer(UUID offerId, UUID userId) {
        //TODO validate users reserved offer count
        //TODO validate offer status
        //TODO refactor this method entirely
        OfferEntity offerEntity = offerRepository.findByOfferId(offerId).get();
        offerEntity.setReservationDate(LocalDateTime.now());
        UserEntity recieverEntity = userRepository.findByUserId(userId).get();
        offerEntity.setReceiver(recieverEntity);
        offerEntity.setStatus(Status.RESERVED);
        saveOfferCommandInterface.saveOffer(null);
        return offerEntity.getOfferId();
    }
}
