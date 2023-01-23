package eventservice.eventservice.controller;

import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.service.CountryCityService;
import eventservice.eventservice.web.controller.CountryCityController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CountryCityControllerTest {

    @Mock
    CountryCityService countryCityService;

    @InjectMocks
    CountryCityController countryCityController;

    CountryDto countryDto1;
    CountryDto countryDto2;
    CountryDto countryDto3;
    CountryDto countryDto4;
    CityDto cityDto1;
    CityDto cityDto2;
    CityDto cityDto3;
    CityDto cityDto4;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        countryDto1 = new CountryDto(1L, "Lithuania");
        countryDto2 = new CountryDto(2L, "Latvia");
        countryDto3 = new CountryDto(3L, "Poland");
        countryDto4 = new CountryDto(4L, "Sweden");
        cityDto1 = new CityDto("Kaunas");
        cityDto2= new CityDto("Vilnius");
        cityDto3 = new CityDto("Riga");
        cityDto4 = new CityDto("Venstspils");
    }

    @Test
    void getAllCountries_success() {
        Mockito.when(countryCityService.getAllCountries()).thenReturn(List.of(countryDto1,countryDto2,countryDto3,countryDto4));
        assertEquals(ResponseEntity.ok(List.of(countryDto1, countryDto2, countryDto3, countryDto4)),
                countryCityController.getAllCountries());
    }

    @Test
    void getAllCountries_emptyList() {
        Mockito.when(countryCityService.getAllCountries()).thenReturn(Collections.emptyList());
        assertEquals(ResponseEntity.ok(Collections.emptyList()),
                countryCityController.getAllCountries());
    }

    @Test
    void getCountryCities_success() {
        Mockito.when(countryCityService.getAllCitiesFromSpecificCountry(any()))
                .thenReturn(List.of(cityDto1, cityDto2));
        assertEquals(ResponseEntity.ok(List.of(cityDto1, cityDto2)), countryCityController.getCountryCities(1L));
    }

    @Test
    void getCountryCities_emptyList() {
        Mockito.when(countryCityService.getAllCitiesFromSpecificCountry(any()))
                .thenReturn(Collections.emptyList());
        assertEquals(ResponseEntity.ok(Collections.emptyList()), countryCityController.getCountryCities(1L));
    }
}