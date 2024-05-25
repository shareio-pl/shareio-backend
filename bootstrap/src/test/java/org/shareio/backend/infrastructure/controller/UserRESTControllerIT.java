//package org.shareio.backend.infrastructure.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.shareio.backend.core.model.vo.AccountType;
//import org.shareio.backend.core.usecases.port.dto.AddressSaveDto;
//import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
//import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
//import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
//import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
//import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//public class UserRESTControllerIT {
//
//    ObjectMapper objectMapper;
//    ObjectWriter objectWriter;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    private MockMvc mvc;
//
//    private void configureObjectMapper() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
//
//    }
//
//    private UserEntity generateUserEntity(UUID userId) {
//        return new UserEntity(null, userId, "jan.kowalski@poczta.pl", "J", "K",
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
//    public void get_nonexistent_user_and_get_404() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/user/get/" + userId))
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void get_invalid_user_and_get_401() throws Exception {
//        UUID userId = UUID.randomUUID();
//        userRepository.save(generateUserEntity(userId));
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/user/get/" + userId))
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    public void get_valid_user_and_get_200() throws Exception {
//        UUID userId = UUID.randomUUID();
//        UserEntity correctUserEntity = generateUserEntity(userId);
//        correctUserEntity.setName("Jan");
//        correctUserEntity.setSurname("Kowal");
//        userRepository.save(correctUserEntity);
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/user/get/" + userId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void add_user_with_existing_email_and_get_401() throws Exception {
//        configureObjectMapper();
//        UUID userId = UUID.randomUUID();
//        UserEntity correctUserEntity = generateUserEntity(userId);
//        correctUserEntity.setName("Jan");
//        correctUserEntity.setSurname("Kowal");
//        userRepository.save(correctUserEntity);
//
//        UserSaveDto userAddDto = new UserSaveDto(
//                "Jan",
//                "Kowal",
//                "bbb",
//                "jan.kowalski@poczta.pl",
//                LocalDate.now(),
//                new AddressSaveDto(
//                        "Polska",
//                        "Łódzkie",
//                        "Łódź",
//                        "95-000",
//                        "Lutomierska",
//                        "12",
//                        "2"
//                )
//        );
//
//        String requestJson = objectWriter.writeValueAsString(userAddDto);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .post("/user/add")
//                        .contentType(APPLICATION_JSON_VALUE)
//                        .content(requestJson)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void add_user_with_correct_data_and_get_200() throws Exception {
//        configureObjectMapper();
//        UUID userId = UUID.randomUUID();
//        UserEntity correctUserEntity = generateUserEntity(userId);
//        correctUserEntity.setName("Jan");
//        correctUserEntity.setSurname("Kowal");
//        userRepository.save(correctUserEntity);
//
//        UserSaveDto userAddDto = new UserSaveDto(
//                "Jan",
//                "Kowal",
//                "bbb",
//                "jan.kowalski@poczta.pl",
//                LocalDate.now(),
//                new AddressSaveDto(
//                        "Polska",
//                        "Łódzkie",
//                        "Łódź",
//                        "95-000",
//                        "Lutomierska",
//                        "12",
//                        "2"
//                )
//        );
//
//        String requestJson = objectWriter.writeValueAsString(userAddDto);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .post("/user/add")
//                        .contentType(APPLICATION_JSON_VALUE)
//                        .content(requestJson)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//
//}
//
