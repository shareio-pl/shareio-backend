package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class UserRESTController {

    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    UserRepository userRepository;

    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = "application/json")
    public List<String> firstPage() {
        List<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        return strings;
    }

    @RequestMapping(value = "/debug/createUser", method = RequestMethod.GET, produces = "text/plain")
    public ResponseEntity<String> debugCreateTestUser(@RequestParam UUID id) {
        UserEntity userEntity;
        try {
            userEntity = new UserEntity(null, id, "A", "B", LocalDateTime.now(),
                    new AddressEntity(null, null, "A", "B", "C", "12", "21", "99999", 10.1, 22.1),
                    new SecurityEntity(null, "aa", LocalDateTime.now(), LocalDateTime.now()));

        } catch (Exception e) {
            return new ResponseEntity<>("Could not create debug user: " + e, HttpStatusCode.valueOf(500));
        }
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
            return new ResponseEntity<>("Could not save debug user: " + e, HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>("Created debug user with id: " + id, HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserSnapshot> getUserById(@RequestParam UUID id) {
        return new ResponseEntity<>(getUserProfileUseCaseInterface.getUserSnapshot(id), HttpStatusCode.valueOf(200));
    }
}

