package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.*;
import org.shareio.backend.infrastructure.dbadapter.repositories.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/debug")
public class DebugRESTController {

    /*
    ENDPOINTS:
    localhost:8082/debug/getOfferIds
    localhost:8082/debug/getOffersByName
    */

    AddressRepository addressRepository;
    OfferRepository offerRepository;
    SecurityRepository securityRepository;
    UserRepository userRepository;


    GetOffersByNameUseCaseInterface offersByNameUseCaseInterface;


    @GetMapping(value = "/getOfferIds",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugGetOfferIds() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UUID> offerIds = new ArrayList<>();
            for (OfferEntity offer : offerRepository.findAll()) {
                offerIds.add(offer.getOfferId());
            }
            response.put("offerIds", offerIds);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        } catch (NoSuchElementException e) {
            response.put(Const.SERVER_ERR, e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        } catch (Exception e) {
            response.put(Const.SERVER_ERR, e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping(value = "/createReview/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateReview(@PathVariable(value = "offerId") UUID offerId) {
        Map<String, Object> response = new HashMap<>();
        UUID reviewId = UUID.randomUUID();
        OfferEntity offerEntity;
        try {
            offerEntity = offerRepository.findByOfferId(offerId).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            response.put(Const.SERVER_ERR, e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }
        ReviewEntity reviewEntity = new ReviewEntity(null, reviewId,
                2.0, LocalDateTime.now());
        offerEntity.setReview(reviewEntity);
        offerRepository.save(offerEntity);
        response.put("id", reviewId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/nuke", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugPurifyTheUnclean() {
        Map<String, Object> response = new HashMap<>();
        try {
            userRepository.truncateTable();
            addressRepository.truncateTable();
            securityRepository.truncateTable();
        } catch (Exception e) {
            response.put("error", e.toString());
        }

        response.put("effect", "\uD83D\uDCA3\uD83D\uDCA2\uD83E\uDD0C");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }
}
