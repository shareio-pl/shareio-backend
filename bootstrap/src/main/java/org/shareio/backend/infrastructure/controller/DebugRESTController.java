package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class DebugRESTController {
    UserRepository userRepository;
    OfferRepository offerRepository;

    @RequestMapping(value = "/debug/createUser", method = RequestMethod.GET, produces = "text/plain")
    public ResponseEntity<String> debugCreateUser() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity;
        try {
            userEntity = new UserEntity(null, userId, "jan.kowalski@poczta.pl", "Jan", "Kowalski",
                    LocalDateTime.of(2000, 12, 31, 12, 0, 0),
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            51.7467613, 19.4530878),
                    new SecurityEntity(null,
                            "$2a$12$6PNuiENyG0f/NQIPyqvc3.eUbPUdsmDoxSnoSTp4DCnoQctc3TPCC",
                            LocalDateTime.now(), LocalDateTime.now()));
        } catch (Exception e) {
            return new ResponseEntity<>("Could not create debug user: " + e, HttpStatusCode.valueOf(500));
        }
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not save debug user: " + e, HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>("Created debug user with id: " + userId + " and password: 1234", HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/debug/createOffer", method = RequestMethod.GET, produces = "text/plain")
    public ResponseEntity<String> debugCreateOffer(@RequestParam UUID userId) {
        UserEntity userEntity;
        try {
            userEntity = userRepository.findByUserId(userId).orElseThrow();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Could not find user with given id: " + e, HttpStatusCode.valueOf(404));
        }

        UUID offerId = UUID.randomUUID();
        OfferEntity offerEntity;
        try {
            offerEntity = new OfferEntity(null, offerId, userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            51.7467613, 19.4530878),
                    LocalDateTime.now(), null, null,
                    "Ładny szop", "Oddam bardzo ładnego szopa. Prawie nie gryzie i chyba nie ma wścieklizny. Za darmo, bo to uczciwa cena.",
                    null);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not create debug offer: " + e, HttpStatusCode.valueOf(500));
        }
        try {
            offerRepository.save(offerEntity);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not save debug offer: " + e, HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>("Created debug offer with id: " + offerId, HttpStatusCode.valueOf(200));
    }


}
