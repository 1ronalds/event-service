package eventservice.eventservice.service;

import eventservice.eventservice.business.connection.CountryCityServiceConnection;
import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.service.impl.CountryCityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class CountryCityServiceTest {
    @Mock
    CountryCityServiceConnection countryCityServiceConnection;

    @InjectMocks
    CountryCityServiceImpl countryCityService;

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
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(countryDto1, countryDto2, countryDto3));
        assertEquals(List.of(countryDto1, countryDto2, countryDto3), countryCityService.getAllCountries());
    }

    @Test
    void getAllCountries_emptyList() {
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), countryCityService.getAllCountries());
    }

    @Test
    void getAllCitiesFromSpecificCountry_success() {
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(List.of(cityDto1, cityDto2));
        assertEquals(List.of(cityDto1, cityDto2), countryCityService.getAllCitiesFromSpecificCountry(1L));
    }

    @Test
    void getAllCitiesFromSpecificCountry_emptyList() {
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), countryCityService.getAllCitiesFromSpecificCountry(1L));
    }
}