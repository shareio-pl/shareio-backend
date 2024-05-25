//TODO: create these tests when delete functions will work
//package org.shareio.backend.infrastructure.controller;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.shareio.backend.core.model.vo.AccountType;
//import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
//import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
//import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
//import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class DeleteRESTControllerIT {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private MockMvc mvc;
//
//    private UserEntity generateUserEntity(UUID userId) {
//        return new UserEntity(null, userId, "jan.kowalski@poczta.pl", "Jan", "Kowal",
//                LocalDate.of(2000, 12, 31),
//                UUID.randomUUID(),
//                new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
//                        "Wólczańska", "215", "1", "91-001",
//                        51.7467613, 19.4530878),
//                new SecurityEntity(null,
//                        "aa", AccountType.USER,
//                        LocalDateTime.now(), LocalDateTime.now()));
//    }
//
//    @Test
//    public void remove_nonexistent_user_and_get_404() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        mvc.perform(MockMvcRequestBuilders
//                        .delete("/user/delete/" + userId))
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void remove_existent_user_and_get_204() throws Exception {
//        UUID userId = UUID.randomUUID();
//        userRepository.save(generateUserEntity(userId));
//        mvc.perform(MockMvcRequestBuilders
//                        .delete("/user/delete/" + userId))
//                .andExpect(status().isOk());
//
//    }
//
//}
