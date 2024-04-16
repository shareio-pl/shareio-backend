package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.vo.AccountType;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/debug")
public class DebugRESTController {
    /*
    ENDPOINTS:
    localhost:8081/debug/createUser
    localhost:8081/debug/createOffers?userId=
    */
    UserRepository userRepository;
    OfferRepository offerRepository;

    @RequestMapping(value = "/createUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateUser() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UUID userId = UUID.randomUUID();
        UserEntity userEntity;
        Map<String, Object> response = new HashMap<>();
        try {
            userEntity = new UserEntity(null, userId, "jan.kowalski@poczta.pl", "Jan", "Kowalski",
                    LocalDateTime.of(2000, 12, 31, 12, 0, 0),
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            51.7467613, 19.4530878),
                    new SecurityEntity(null,
                            bCryptPasswordEncoder.encode("aa"), AccountType.USER,
                            LocalDateTime.now(), LocalDateTime.now()));
            userRepository.save(userEntity);
        } catch (Exception e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
        response.put("id", userId.toString());
        response.put("password", "aa");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/createOffers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateOffer(@RequestParam UUID userId) {
        UserEntity userEntity;
        Map<String, Object> response = new HashMap<>();
        try {
            userEntity = userRepository.findByUserId(userId).orElseThrow();
        } catch (NoSuchElementException e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }

        List<UUID> offerIds = new ArrayList<>(3);
        offerIds.add(UUID.randomUUID());
        offerIds.add(UUID.randomUUID());
        offerIds.add(UUID.randomUUID());

        OfferEntity offerEntity;
        try {
            offerEntity = new OfferEntity(null, offerIds.getFirst(), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Drewnowska", "58", "1", "91-002",
                            51.7792315, 19.4428693),
                    LocalDateTime.now(), null, null,
                    "Dorodne krzyczące dziecko", "W pełni zdrowe (no może lekko otyłe) krzyczące dziecko. Nie moje, ale chcę się go pozbyć",
                    null);
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(1), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            51.7467613, 19.4530878),
                    LocalDateTime.now(), null, null,
                    "Ładny szop", "Oddam bardzo ładnego szopa. Prawie nie gryzie i chyba nie ma wścieklizny. Za darmo, bo to uczciwa cena.",
                    null);
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(2), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Kołodziejska", "18", "3", "91-001",
                            51.7467613, 19.4530878),
                    LocalDateTime.now(), null, null,
                    "Mieszkanie w centrum", "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                    null);
            offerRepository.save(offerEntity);

        } catch (Exception e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        response.put("ids", offerIds);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }


}
