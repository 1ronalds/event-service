package eventservice.eventservice.service;

import eventservice.eventservice.business.connection.CountryCityServiceConnection;
import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.handlers.exceptions.DateIntervalNotSpecifiedException;
import eventservice.eventservice.business.handlers.exceptions.EventNotFoundException;
import eventservice.eventservice.business.handlers.exceptions.InvalidDataException;
import eventservice.eventservice.business.mapper.EventMapStruct;
import eventservice.eventservice.business.repository.EventRepository;
import eventservice.eventservice.business.repository.model.EventEntity;
import eventservice.eventservice.business.repository.model.EventTypeEntity;
import eventservice.eventservice.business.repository.model.RoleEntity;
import eventservice.eventservice.business.repository.model.UserEntity;
import eventservice.eventservice.business.service.UserService;
import eventservice.eventservice.business.service.impl.EventServiceImpl;
import eventservice.eventservice.model.EventDto;
import eventservice.eventservice.model.EventMinimalDto;
import eventservice.eventservice.model.EventTypeDto;
import eventservice.eventservice.model.RoleDto;
import eventservice.eventservice.model.UserDto;
import eventservice.eventservice.model.UserMinimalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.w3c.dom.events.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class EventServiceTest {
    @Mock
    EventMapStruct mapper;

    @Mock
    EventRepository repository;

    @Mock
    UserService userService;

    @Mock
    CountryCityServiceConnection countryCityServiceConnection;

    @Spy
    @InjectMocks
    EventServiceImpl service;

    EventDto eventDto;
    UserMinimalDto userMinimalDto;
    EventMinimalDto eventDto1;
    EventMinimalDto eventDto2;
    EventMinimalDto eventDto3;
    EventMinimalDto eventDto4;
    EventMinimalDto eventDto5;
    EventEntity eventEntity1;
    EventEntity eventEntity2;
    EventEntity eventEntity3;
    EventEntity eventEntity4;
    EventEntity eventEntity5;
    LinkedList<CountryDto> countryList = new LinkedList<>();
    LinkedList<CityDto> cityList = new LinkedList<>();
    UserDto userDto;
    EventDto eventDtoEdited;
    EventEntity eventEntity;
    EventEntity eventEntityEdited;
    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        RoleDto roleDto = new RoleDto(1L, "admin");
        userDto = new UserDto(1L, "AdminUser", "admin@admin.com", "password123", "Adam", "Leo", roleDto);
        userMinimalDto = new UserMinimalDto(1L, "User");
        RoleEntity roleEntity2 = new RoleEntity(1L, "admin");

        UserEntity userEntity2 = new UserEntity(1L, "AdminUser", "admin@admin.com", "password123", "Adam", "Leo", roleEntity2);
        EventTypeEntity publicTypeEntity = new EventTypeEntity(1L, "public");
        EventTypeEntity privateTypeEntity = new EventTypeEntity(2L, "private");
        EventTypeDto publicTypeDto = new EventTypeDto(1L, "public");

        eventDto = new EventDto(1L, "Bicycling contest", "A contest of bicycling free to watch and participate", "Latvia",
                "Riga", 300, LocalDateTime.parse("24-11-2022 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                1, userMinimalDto, publicTypeDto);
        eventDtoEdited = new EventDto(1L, "A Bicycling contest", "A contest of bicycling free to watch and participate", "Latvia",
                "Riga", 300, LocalDateTime.parse("24-11-2022 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                1, userMinimalDto, publicTypeDto);
        eventEntity = new EventEntity(1L, "Bicycling contest", "A contest of bicycling free to watch and participate", "Latvia",
                "Riga", 300, LocalDateTime.parse("24-11-2022 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                1, userEntity2, publicTypeEntity);

        eventDto1 = new EventMinimalDto(1L, "Bicycling contest");

        eventDto2 = new EventMinimalDto(2L, "Theatre");

        eventDto3 = new EventMinimalDto(3L, "Marathon");

        eventDto4 = new EventMinimalDto(4L, "TestEvent");

        eventDto5 = new EventMinimalDto(5L, "TestEvent");

        RoleEntity roleEntity = new RoleEntity(2L, "user");

        UserEntity userEntity = new UserEntity(1L, "User", "user@gmail.com", "password", "John", "Doe", roleEntity);


        eventEntity1 = new EventEntity(1L, "Bicycling contest", "A contest of bicycling free to watch and participate", "Latvia",
                "Riga", 300, LocalDateTime.parse("13-12-2023 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                1, userEntity, publicTypeEntity);

        eventEntity2 = new EventEntity(2L, "Theatre", "Everyone will be amazed watching this theatre","Latvia",
                "Venstspils", 300, LocalDateTime.parse("13-12-2023 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 1, userEntity, privateTypeEntity);

        eventEntity3 = new EventEntity(3L, "Marathon",
                "Running is good for your health, so join us in this 7km marathon", "Lithuania",
                "Vilnius", 300, LocalDateTime.parse("13-12-2023 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 1, userEntity, publicTypeEntity);

        eventEntity4 = new EventEntity(4L, "TestEvent", "TestEvent","Latvia",
                "Riga", 300, LocalDateTime.parse("13-12-2023 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 1, userEntity, publicTypeEntity);

        eventEntity5 = new EventEntity(5L, "TestEvent", "TestEvent","Latvia",
                "Ventspils", 300, LocalDateTime.parse("13-12-2023 12:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), 1, userEntity, publicTypeEntity);

        CountryDto country = new CountryDto(1L, "Latvia");
        CityDto city = new CityDto("Riga");
        countryList.add(country);
        cityList.add(city);
    }

    @Test
     void findAllPublicEvents_OnlyCountrySpecified_Found(){
        String country = "Latvia";
        Mockito.when(repository.findAllByCountryAndTypeType(country, "public")).thenReturn(List.of(eventEntity1, eventEntity4, eventEntity5));
        Mockito.when(mapper.entityToMinimalDto(eventEntity1)).thenReturn(eventDto1);
        Mockito.when(mapper.entityToMinimalDto(eventEntity4)).thenReturn(eventDto4);
        Mockito.when(mapper.entityToMinimalDto(eventEntity5)).thenReturn(eventDto5);

        List<EventMinimalDto> results = service.findAllPublicEvents(country, null, null, null);
        assertEquals(List.of(eventDto1, eventDto4, eventDto5), results);

    }

    @Test
    void findAllPublicEvents_OnlyCountrySpecified_NotFound(){
        String country = "Sweden";
        Mockito.when(repository.findAllByCountryAndTypeType(country, "public")).thenReturn(Collections.emptyList());
        Mockito.when(mapper.entityToMinimalDto(eventEntity1)).thenReturn(eventDto1);

        List<EventMinimalDto> results = service.findAllPublicEvents(country, null, null, null);
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    void findAllPublicEvents_CountryAndCitySpecified_Found(){
        String country = "Latvia";
        String city = "Riga";
        Mockito.when(repository.findAllByCountryAndTypeTypeAndCity(country, "public", city)).thenReturn(List.of(eventEntity1, eventEntity4));
        Mockito.when(mapper.entityToMinimalDto(eventEntity1)).thenReturn(eventDto1);
        Mockito.when(mapper.entityToMinimalDto(eventEntity4)).thenReturn(eventDto4);

        List<EventMinimalDto> results = service.findAllPublicEvents(country, city, null, null);
        assertEquals(List.of(eventDto1, eventDto4), results);


    }

    @Test
    void findAllPublicEvents_CountryAndCitySpecified_NotFound(){
        String country = "Sweden";
        String city = "Stockholm";
        Mockito.when(repository.findAllByCountryAndTypeTypeAndCity(country, "public", city))
                .thenReturn(Collections.emptyList());

        List<EventMinimalDto> results = service.findAllPublicEvents(country, city, null, null);
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    void findAllPublicEvents_CountryAndDateFromAndDateToSpecified_Found(){
        String country = "Latvia";
        LocalDate dateFrom = LocalDate.of(2020, 11, 12);
        LocalDate dateTo = LocalDate.of(2024, 11, 12);
        Mockito.when(repository.findAllByCountryAndTypeTypeAndDateTimeBetween(any(), any(), any(), any()))
                .thenReturn(List.of(eventEntity1, eventEntity4, eventEntity5));
        Mockito.when(mapper.entityToMinimalDto(eventEntity1)).thenReturn(eventDto1);
        Mockito.when(mapper.entityToMinimalDto(eventEntity4)).thenReturn(eventDto4);
        Mockito.when(mapper.entityToMinimalDto(eventEntity5)).thenReturn(eventDto5);

        List<EventMinimalDto> results = service.findAllPublicEvents(country, null, dateFrom, dateTo);
        assertEquals(List.of(eventDto1, eventDto4, eventDto5), results);
    }

    @Test
    void findAllPublicEvents_CountryAndDateFromAndDateToSpecified_NotFound(){
        String country = "Latvia";
        LocalDate dateFrom = LocalDate.of(2019, 11, 12);
        LocalDate dateTo = LocalDate.of(2020, 11, 12);
        Mockito.when(repository.findAllByCountryAndTypeTypeAndDateTimeBetween(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<EventMinimalDto> results = service.findAllPublicEvents(country, null, dateFrom, dateTo);
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromAndDateToSpecified_Found(){
        String country = "Latvia";
        LocalDate dateFrom = LocalDate.of(2019, 11, 12);
        LocalDate dateTo = LocalDate.of(2020, 11, 12);
        String city = "Riga";

        Mockito.when(repository.findAllByCountryAndTypeTypeAndCityAndDateTimeBetween(any(), any(), any(), any(), any()))
                .thenReturn(List.of(eventEntity1, eventEntity4));
        Mockito.when(mapper.entityToMinimalDto(eventEntity1)).thenReturn(eventDto1);
        Mockito.when(mapper.entityToMinimalDto(eventEntity4)).thenReturn(eventDto4);

        List<EventMinimalDto> results = service.findAllPublicEvents(country, city, dateFrom, dateTo);
        assertEquals(List.of(eventDto1, eventDto4), results);
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromAndDateToSpecified_NotFound(){
        String country = "Latvia";
        LocalDate dateFrom = LocalDate.of(2001, 11, 12);
        LocalDate dateTo = LocalDate.of(2002, 11, 12);
        String city = "Riga";

        Mockito.when(repository.findAllByCountryAndTypeTypeAndCityAndDateTimeBetween(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<EventMinimalDto> results = service.findAllPublicEvents(country, city, dateFrom, dateTo);
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateFromSpecified_Exception(){
        String country = "Latvia";
        LocalDate dateFrom = LocalDate.of(2001, 11, 12);
        String city = "Riga";
        assertThrows(DateIntervalNotSpecifiedException.class, () -> service.findAllPublicEvents(country, city, dateFrom, null));
    }

    @Test
    void findAllPublicEvents_CountryAndCityAndDateToSpecified_Exception(){
        String country = "Latvia";
        LocalDate dateTo = LocalDate.of(2002, 11, 12);
        String city = "Riga";
        assertThrows(DateIntervalNotSpecifiedException.class, () -> service.findAllPublicEvents(country, city, null, dateTo));
    }

    @Test
    void findEventInfo(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(eventEntity1));
        Mockito.when(mapper.entityToDto(any())).thenReturn(eventDto);
        assertEquals(eventDto, service.findEventInfo(1L));
    }

    @Test
    void findEventInfoNonexistentId(){
        Mockito.when(repository.findById(any())).thenThrow(EventNotFoundException.class);
        Mockito.when(mapper.entityToDto(any())).thenReturn(eventDto);
        assertEquals(eventDto, service.findEventInfo(1L));
    }

    @Test
    void saveEvent() {
        Mockito.when(userService.findUserDetails(any())).thenReturn(userDto);
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(countryList);
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(cityList);
        Mockito.when(mapper.dtoToEntity(any(), any())).thenReturn(eventEntity1);
        Mockito.when(mapper.entityToDto(any())).thenReturn(eventDto);
        assertEquals(eventDto, service.saveEvent("AdminUser", eventDto));
    }

    @Test
    void saveEventInvalidCountry() {
        Mockito.when(userService.findUserDetails(any())).thenReturn(userDto);
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(Collections.emptyList());
        //passes empty list which is the same as filtering country list and finding no matches
        assertThrows(InvalidDataException.class, () -> service.saveEvent("AdminUser", eventDto));
    }

    @Test
    void saveEventInvalidCity(){
        Mockito.when(userService.findUserDetails(any())).thenReturn(userDto);
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(Collections.emptyList());
        //passes empty list which is the same as filtering city list and finding no matches
        assertThrows(InvalidDataException.class, () -> service.saveEvent("AdminUser", eventDto));
    }

    @Test
    void editEvent(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));
        Mockito.when(userService.findUserDetails(any())).thenReturn(userDto);
        Mockito.when(mapper.entityToDto(any())).thenReturn(eventDtoEdited);
        Mockito.when(mapper.dtoToEntity(any(), any())).thenReturn(eventEntityEdited);
        Mockito.when(repository.save(any())).thenReturn(eventEntityEdited);

        assertEquals(eventDtoEdited, service.editEvent("AdminUser", 1L, eventDtoEdited));
    }

    @Test
    void editEventNonexistent(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> service.editEvent("Nonexistent", 1L, eventDtoEdited));
    }

    @Test
    void editEventIncorrectUsername(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));
        Mockito.when(userService.findUserDetails(any())).thenReturn(userDto);
        Mockito.when(mapper.entityToDto(any())).thenReturn(eventDtoEdited);
        Mockito.when(mapper.dtoToEntity(any(), any())).thenReturn(eventEntityEdited);
        Mockito.when(repository.save(any())).thenReturn(eventEntityEdited);

        assertThrows(InvalidDataException.class, () -> service.editEvent("IncorrectUsername", 1L, eventDtoEdited));
    }

    @Test
    void deleteEvent(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));

        service.deleteEvent("AdminUser", 1L);
        Mockito.verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEventNonexistent(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> service.deleteEvent("AdminUser", 1L));
    }

    @Test
    void deleteEventInvalidUsername(){
        Mockito.when(repository.findById(any())).thenReturn(Optional.ofNullable(eventEntity));

        assertThrows(InvalidDataException.class, () -> service.deleteEvent("IncorrectUser", 1L));
    }

}