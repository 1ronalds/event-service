package eventservice.eventservice.integration;

import com.fasterxml.jackson.databind.json.JsonMapper;
import eventservice.eventservice.business.connection.CountryCityServiceConnection;
import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class CountryCityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryCityServiceConnection countryCityServiceConnection;


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
    void getAllCountries_success() throws Exception {
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(List.of(countryDto1, countryDto2, countryDto3, countryDto4));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(countryDto1, countryDto2, countryDto3, countryDto4));
        MvcResult result = mockMvc.perform(get("/v1/countries/all").accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn();
        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void getAllCountries_emptyList() throws Exception {
        Mockito.when(countryCityServiceConnection.getCountries()).thenReturn(Collections.emptyList());
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());
        MvcResult result = mockMvc.perform(get("/v1/countries/all").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void getAllCitiesFromSpecificCountry_success() throws Exception {
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(List.of(cityDto1, cityDto2));
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(List.of(cityDto1, cityDto2));
        MvcResult result = mockMvc.perform(get("/v1/cities/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }

    @Test
    void getAllCitiesFromSpecificCountry_emptyList() throws Exception {
        Mockito.when(countryCityServiceConnection.getCities(any())).thenReturn(Collections.emptyList());
        JsonMapper jm = JsonMapper.builder().build();
        String eventJsonExpectedResult = jm.writeValueAsString(Collections.emptyList());
        MvcResult result = mockMvc.perform(get("/v1/cities/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(eventJsonExpectedResult, result.getResponse().getContentAsString());
    }
}
