package org.shareio.backend.infrastructure.controller;

//import org.junit.runner.RunWith;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class OfferRESTControllerIT {


//    @Autowired
//    OfferRepository offerRepository;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Test
//    public void testGetOfferGet404() throws Exception {
//        UUID offerId = UUID.randomUUID();
//
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/offer/get/" + offerId))
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    public void testGetUserGet200() throws Exception {
//        UUID offerId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        UUID addressId = UUID.randomUUID();
//        offerRepository.save(new OfferEntity(
//                null,
//                offerId,
//                new UserEntity(null, userId, "jan.kowalski@poczta.pl", "Jan", "Kowal",
//                        LocalDateTime.of(2000, 12, 31, 12, 0, 0),
//                        UUID.randomUUID(),
//                        new AddressEntity(null, UUID.randomUUID(), "Polska", "Łódzkie", "Łódź",
//                                "Wólczańska", "215", "1", "91-001",
//                                51.7467613, 19.4530878),
//                        new SecurityEntity(null,
//                                "aa", AccountType.USER,
//                                LocalDateTime.now(), LocalDateTime.now())),
//                new AddressEntity(
//                        null,
//                        addressId,
//                        "Poland",
//                        "Lodzkie",
//                        "Lodz",
//                        "Limanowskiego",
//                        "29",
//                        "29",
//                        "95-010",
//                        25.22,
//                        12.54
//                        ),
//                LocalDateTime.now(),
//                Status.CREATED,
//                null,
//                null,
//                "testOffer",
//                Condition.ALMOST_NEW,
//                Category.OTHER,
//                "testing purposes",
//                UUID.randomUUID()
//        ));
//        mvc.perform(MockMvcRequestBuilders
//                        .get("/offer/get/" + offerId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void testGetConditionsGet200() throws Exception {
//     mvc.perform(MockMvcRequestBuilders
//             .get("/offer/getConditions")
//            .accept(MediaType.APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk());
//
//    }
//
////    @Test
////    public void testGetOfferGet401() throws Exception {
////        UUID offerId = UUID.randomUUID();
////        offerRepository.save(new OfferEntity(
////                null,
////                offerId,
////                null,
////                null,
////                LocalDateTime.now(),
////                Status.CREATED,
////                null,
////                null,
////                "testOffer",
////                Condition.ALMOST_NEW,
////                "testing purposes",
////                UUID.randomUUID()
////        ));
////        mvc.perform(MockMvcRequestBuilders
////                        .get("/offer/get/" + offerId))
////                .andExpect(status().isBadRequest());
////
////    }
}

