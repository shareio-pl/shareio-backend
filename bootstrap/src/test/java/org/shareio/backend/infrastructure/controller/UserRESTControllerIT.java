package org.shareio.backend.infrastructure.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shareio.backend.core.model.vo.AccountType;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRESTControllerIT {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;
    private UserEntity generateUserEntity(UUID userId){
        return new UserEntity(null, userId, "jan.kowalski@poczta.pl", "J", "K",
                LocalDateTime.of(2000, 12, 31, 12, 0, 0),
                UUID.randomUUID(),
                new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
                        "Wólczańska", "215", "1", "91-001",
                        51.7467613, 19.4530878),
                new SecurityEntity(null,
                        "aa", AccountType.USER,
                        LocalDateTime.now(), LocalDateTime.now()));
    }
    @Test
    public void get_nonexistent_user_and_get_404() throws Exception {
        UUID userId = UUID.randomUUID();

        mvc.perform(MockMvcRequestBuilders
                        .get("/user/get/" + userId))
                .andExpect(status().isNotFound());

    }

    @Test
    public void get_invalid_user_and_get_401() throws Exception {
        UUID userId = UUID.randomUUID();
        userRepository.save(generateUserEntity(userId));
        mvc.perform(MockMvcRequestBuilders
                        .get("/user/get/" + userId))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void get_valid_user_and_get_200() throws Exception {
        UUID userId = UUID.randomUUID();
        UserEntity correctUserEntity = generateUserEntity(userId);
        correctUserEntity.setName("Jan");
        correctUserEntity.setSurname("Kowal");
        userRepository.save(correctUserEntity);
        mvc.perform(MockMvcRequestBuilders
                        .get("/user/get/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }



}

