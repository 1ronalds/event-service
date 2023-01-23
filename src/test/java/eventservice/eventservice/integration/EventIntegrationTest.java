package eventservice.eventservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eventservice.eventservice.business.connection.CountryCityServiceConnection;
import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.repository.EventRepository;
import eventservice.eventservice.business.repository.UserRepository;
import eventservice.eventservice.business.repository.model.EventEntity;
import eventservice.eventservice.business.repository.model.EventTypeEntity;
import eventservice.eventservice.business.repository.model.RoleEntity;
import eventservice.eventservice.business.repository.model.UserEntity;
import eventservice.eventservice.model.EventMinimalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EventIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CountryCityServiceConnection countryCityServiceConnection;
    EventMinimalDto eventDto1;
    EventMinimalDto eventDto2;
    EventMinimalDto eventDto3;
    EventMinimalDto eventDto4;
    EventMinimalDto eventDto5;
    EventMinimalDto eventDto6;

    UserEntity userEntity;
    EventEntity eventEntity;
    EventEntity eventEntity1;
    EventEntity eventEntity2;
    EventEntity eventEntity3;
    EventEntity eventEntity4;
    EventEntity eventEntity5;
    EventEntity eventEntity6;

    EventTypeEntity eventTypeEntity;
    RoleEntity roleEntity;

    @BeforeEach
    void init(){
        eventTypeEntity = new EventTypeEntity(1L, "public");
        roleEntity = new RoleEntity(1L, "admin");
        userEntity = new UserEntity(1L, "AdminUser", "admin@admin.com", "password123", "Adam", "Leo", roleEntity);
        eventEntity = new EventEntity(7L, "Random contest", "No description", "Latvia", "Liepāja", 10,
                LocalDateTime.of(2023, 1, 1, 12, 00), 0, userEntity, eventTypeEntity);
        eventDto1 = new EventMinimalDto(1L, "Bicycling contest");
        eventDto2 = new EventMinimalDto(2L, "Theatre");
        eventDto3 = new EventMinimalDto(3L, "Marathon");
        eventDto4 = new EventMinimalDto(4L, "Yoga");
        eventDto5 = new EventMinimalDto(5L, "Movie night");
        eventDto6 = new EventMinimalDto(6L, "Kebab eating contest");

        UserEntity fourthUser = new UserEntity(4L, "Damian123", "Damian123@gmail.com", "password", "Damian",
                "Blaskowicz", new RoleEntity(2L, "user"));
        UserEntity thirdUser = new UserEntity(3L, "CasualMovieEnjoyer", "thisMyEmail@gmail.com", "h@ck3rM@N", "Movie",
                "Enjoyer", new RoleEntity(2L, "user"));
        UserEntity secondUser = new UserEntity(2L, "BestClientEver", "bestClientEver@gmail.com", "h@ck3rM@N", "Mary",
                "Jackson", new RoleEntity(2L, "user"));

        eventEntity1 = new EventEntity(1L, "Bicycling contest",
                "A contest of bicycling free to watch and participate", "Latvia",
                "Riga", 300, LocalDateTime.of(LocalDate.of(2022, 12 ,8),
                LocalTime.of(12,0)), 2, secondUser, new EventTypeEntity(1L, "public"));

        eventEntity2 = new EventEntity(2L, "Theater", "Everyone will be amazed watching this theatre", "Latvia",
                "Venstspils", 50, LocalDateTime.of(LocalDate.of(2022, 12 ,4), LocalTime.of(15,30)),
                3, thirdUser, new EventTypeEntity(2L, "private"));

        eventEntity3 = new EventEntity(3L, "Marathon", "Running is good for your health, so join our 7km marathon", "Lithuania",
                "Vilnius", 1000, LocalDateTime.of(LocalDate.of(2022, 12 ,1), LocalTime.of(10,30)),
                2, fourthUser, new EventTypeEntity(2L, "private"));

        eventEntity4 = new EventEntity(4L, "Yoga", "Come, join us in a group yoga session.", "Latvia",
                "Venstspils", 200, LocalDateTime.of(LocalDate.of(2022, 11 ,1), LocalTime.of(10,30)),
                20, fourthUser, new EventTypeEntity(1L, "public"));

        eventEntity5 = new EventEntity(5L, "Movie night", "Join us in watching a christmas movie", "Latvia",
                "Riga", 500, LocalDateTime.of(LocalDate.of(2022, 12 ,1), LocalTime.of(10,30)),
                0, fourthUser, new EventTypeEntity(1L, "public"));

        eventEntity6 = new EventEntity(6L, "Kebab eating contest", "Prove to everyone once and for all that you are the best kebab eater in Lithuania!", "Lithuania",
                "Kaunas", 500, LocalDateTime.of(LocalDate.of(2022, 12 ,1), LocalTime.of(10,30)),
                0, fourthUser, new EventTypeEntity(2L, "private"));

    }

    @Test
    void findAllPublicEvents_OnlyCountrySpecified_Found() throws Exception {
        Mockito.when(eventRepository.findAllByCountryAndTypeType("Latvia", "public"))
                .thenReturn(List.of(eventEntity1, eventEntity4, eventEntity5));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(eventDto1, eventDto4, eventDto5));

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_OnlyCountrySpecified_NotFound() throws Exception {
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Sweden")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndCitySpecified_Found() throws Exception {
        Mockito.when(eventRepository.findAllByCountryAndTypeTypeAndCity("Latvia", "public", "Venstspils"))
                .thenReturn(List.of(eventEntity4));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(eventDto4));

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("city", "Venstspils")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndCitySpecified_NotFound() throws Exception{
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Lithuania")
                        .param("city", "Kaunas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndDateFromAndDateToSpecified_Found() throws Exception{
        Mockito.when(eventRepository.findAllByCountryAndTypeTypeAndDateTimeBetween("Latvia", "public",
                        LocalDateTime.of(2020, 11, 12, 0, 0),
                        LocalDateTime.of(2023, 11, 12, 0, 0)))
                .thenReturn(List.of(eventEntity1, eventEntity4, eventEntity5));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(eventDto1, eventDto4, eventDto5));

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("date_from", "12-11-2020")
                        .param("date_to", "12-11-2023")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndDateFromAndDateToSpecified_NotFound() throws Exception{
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("date_from", "12-11-2029")
                        .param("date_to", "12-11-2029")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromAndDateToSpecified_Found() throws Exception{
        Mockito.when(eventRepository.findAllByCountryAndTypeTypeAndCityAndDateTimeBetween("Latvia", "public", "Venstspils",
                        LocalDateTime.of(2020, 11, 12, 0, 0),
                        LocalDateTime.of(2023, 11, 12, 0, 0)))
                .thenReturn(List.of(eventEntity4));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(eventDto4));

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("city", "Venstspils")
                        .param("date_from", "12-11-2020")
                        .param("date_to", "12-11-2023")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromAndDateToSpecified_NotFound() throws Exception{
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("city", "Venstspils")
                        .param("date_from", "12-11-2029")
                        .param("date_to", "12-11-2029")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());

    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromSpecified_Exception() throws Exception{
        mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("city", "Venstspils")
                        .param("date_from", "12-11-2020")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateToSpecified_Exception() throws Exception{
        mockMvc.perform(get("/v1/events/event")
                        .param("country", "Latvia")
                        .param("city", "Venstspils")
                        .param("date_to", "12-11-2020")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findEventInfo() throws Exception {
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));

        mockMvc.perform(get("/v1/events/event/7"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Random contest"));
    }

    @Test
    void findEventInfoNonexistent() throws Exception {
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.empty());
        mockMvc.perform(get("/v1/events/event/7"))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    void saveEvent() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.save(any())).thenReturn(eventEntity);
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(eventEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(new CountryDto(1L, "Latvia")));
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(List.of(new CityDto("Liepāja")));


        mockMvc.perform(post("/v1/events/user/AdminUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void saveEventInvalidCountry() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.save(any())).thenReturn(eventEntity);
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(eventEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(Collections.emptyList());


        mockMvc.perform(post("/v1/events/user/AdminUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEventInvalidCity() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.save(any())).thenReturn(eventEntity);
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(eventEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(new CountryDto(1L, "Latvia")));
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(Collections.emptyList());


        mockMvc.perform(post("/v1/events/user/AdminUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveEventInvalidData() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        EventEntity invalidEntity = new EventEntity(7L, null, "No description", "Latvia", "Liepāja", 10,
                LocalDateTime.of(2023, 1, 1, 12, 00), 0, userEntity, eventTypeEntity);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(invalidEntity);

        mockMvc.perform(post("/v1/events/user/AdminUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void editEvent() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.save(any())).thenReturn(eventEntity);
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(eventEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(new CountryDto(1L, "Latvia")));
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(List.of(new CityDto("Liepāja")));


        mockMvc.perform(put("/v1/events/user/AdminUser/event/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editEventIncorrectCountry() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        EventEntity differentEntity = new EventEntity(7L, "No title", "No description", "Latvia", "Rēzekne", 10,
                LocalDateTime.of(2023, 1, 1, 12, 0), 0, userEntity, eventTypeEntity);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(differentEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(Collections.emptyList());

        mockMvc.perform(put("/v1/events/user/AdminUser/event/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void editEventIncorrectCity() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        EventEntity differentEntity = new EventEntity(7L, "No title", "No description", "Latvia", "Rēzekne", 10,
                LocalDateTime.of(2023, 1, 1, 12, 0), 0, userEntity, eventTypeEntity);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(differentEntity);
        // Unnecessary data gets replaced with system generated data
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(new CountryDto(1L, "Latvia")));
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(put("/v1/events/user/AdminUser/event/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void editEventInvalidData() throws Exception {
        Mockito.when(userRepository.findById((anyLong()))).thenReturn(Optional.of(userEntity));
        EventEntity invalidEntity = new EventEntity(7L, null, "No description", "Latvia", "Liepāja", 10,
                LocalDateTime.of(2023, 1, 1, 12, 00), 0, userEntity, eventTypeEntity);
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.of(eventEntity));
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(df);
        String eventJson = mapper.writeValueAsString(invalidEntity);

        mockMvc.perform(put("/v1/events/user/AdminUser/event/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteEvent() throws Exception {
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));

        mockMvc.perform(delete("/v1/events/user/AdminUser/event/7"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEventNotFound() throws Exception {
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/v1/events/user/AdminUser/event/7"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEventInvalidUsername() throws Exception {
        Mockito.when(eventRepository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));

        mockMvc.perform(delete("/v1/events/user/randomUser/event/7"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
