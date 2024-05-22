package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.vo.AccountType;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.*;
import org.shareio.backend.infrastructure.dbadapter.repositories.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    AddressRepository addressRepository;
    OfferRepository offerRepository;
    SecurityRepository securityRepository;
    UserRepository userRepository;


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
                    LocalDate.of(2000, 12, 31),
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
                    imageUUIDs.get("offer1"), null);
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(1), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Wólczańska", "215", "1", "91-001",
                            50.7467613, 19.4530878),
                    LocalDateTime.now(), Status.CREATED, null, null,
                    "Ładny szop", Condition.ALMOST_NEW, Category.RAILWAY,
                    "Oddam bardzo ładnego szopa. Prawie nie gryzie i chyba nie ma wścieklizny. Za darmo, bo to uczciwa cena.",
                    imageUUIDs.get("offer2"), null);
            offerRepository.save(offerEntity);

            offerEntity = new OfferEntity(null, offerIds.get(2), userEntity,
                    new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                            "Kołodziejska", "18", "3", "91-001",
                            51.7467613, 18.4530878),
                    LocalDateTime.now(), Status.CREATED, null, null,
                    "Mieszkanie", Condition.BROKEN, Category.OTHER,
                    "Klimatyczne mieszkanie w centrum Łodzi. Blisko manufaktury. W tradycyjnej Łódzkiej kamienicy.",
                    imageUUIDs.get("offer3"), null);
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

    @RequestMapping(value = "/createReview/{offerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugCreateReview(@PathVariable(value = "offerId") UUID offerId) {
        Map<String, Object> response = new HashMap<>();
        UUID reviewId = UUID.randomUUID();
        OfferEntity offerEntity;
        try {
            offerEntity  = offerRepository.findByOfferId(offerId).get();
        } catch (NoSuchElementException e) {
            response.put("error", e.toString());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
        }
        ReviewEntity reviewEntity = new ReviewEntity(null,reviewId,
                20.0F, LocalDateTime.now());
        offerEntity.setReview(reviewEntity);
        offerRepository.save(offerEntity);
        response.put("id", reviewId);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/nuke", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> debugPurifyTheUnclean() {
        Map<String, Object> response = new HashMap<>();
        try {
            userRepository.truncateTable();
            addressRepository.truncateTable();
            securityRepository.truncateTable();
        }
        catch (Exception e) {
            response.put("error", e.toString());
        }

        response.put("effect", "\uD83D\uDCA3\uD83D\uDCA2\uD83E\uDD0C");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

    }
}
