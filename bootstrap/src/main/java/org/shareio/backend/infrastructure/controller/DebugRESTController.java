package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.vo.AccountType;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/debug")
public class DebugRESTController {
    /*
    ENDPOINTS:
    localhost:8082/debug/createUser
    localhost:8082/debug/createOffers/{userId}
    localhost:8082/debug/getOfferIds
    localhost:8082/debug/getOffersByName
    */
    UserRepository userRepository;
    OfferRepository offerRepository;
    GetOffersByNameUseCaseInterface offersByNameUseCaseInterface;

    private final Map<String, UUID> imageUUIDs = new HashMap<>() {{
        put("user", UUID.fromString("6829fcfa-2773-498e-a339-f87e9b14a5ee"));
        put("offer1", UUID.fromString("8988b046-7994-4d63-bbaa-3cf4a78b1d1b"));
        put("offer2", UUID.fromString("24e2ccf3-70fe-406a-aa9a-4efa6202c6f7"));
        put("offer3", UUID.fromString("838d9dd8-9880-472f-8e34-0fb3b57db359"));
    }};

    @RequestMapping(value = "/createUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateUser() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UUID userId = UUID.randomUUID();
        UserEntity userEntity;
        Map<String, Object> response = new HashMap<>();
        try {
            userEntity = new UserEntity(null, userId, "jan.kowalski@poczta.pl", "Jan", "Kowalski",
                    LocalDateTime.of(2000, 12, 31, 12, 0, 0),
                    imageUUIDs.get("user"),
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

    @RequestMapping(value = "/createOffers/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateOffer(@PathVariable(value = "userId") UUID userId) {
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
                    LocalDateTime.now(), Status.CREATED, null, null,
                    "Dorodne krzyczące dziecko", Condition.LIGHTLY_USED, Category.HOBBY,
                    "W pełni zdrowe (no może lekko otyłe) krzyczące dziecko. Nie moje, ale chcę się go pozbyć",
                    imageUUIDs.get("offer1"));
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(1), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            51.7467613, 19.4530878),
                    LocalDateTime.now(), Status.CREATED, null, null,
                    "Ładny szop", Condition.ALMOST_NEW, Category.INFRASTRUKTURA_KOLEJOWA,
                    "Oddam bardzo ładnego szopa. Prawie nie gryzie i chyba nie ma wścieklizny. Za darmo, bo to uczciwa cena.",
                    imageUUIDs.get("offer2"));
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(2), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Kołodziejska", "18", "3", "91-001",
                            51.7467613, 19.4530878),
                    LocalDateTime.now(), Status.CREATED, null, null,
                    "Mieszkanie", Condition.BROKEN, Category.INNE,
                    "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                    imageUUIDs.get("offer3"));
            offerRepository.save(offerEntity);

        } catch (Exception e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }

        response.put("ids", offerIds);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/getOfferIds", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        } catch (Exception e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }

    @RequestMapping(value = "/getOffersByName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugGetOffersByName(@RequestParam String name) throws MultipleValidationException {
        Map<String, Object> response = new HashMap<>();
        response.put("offerIds", offersByNameUseCaseInterface.getOfferResponseDtoListByName(name));
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
}
