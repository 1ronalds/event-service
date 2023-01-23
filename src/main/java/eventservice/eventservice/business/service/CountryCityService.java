package eventservice.eventservice.business.service;

import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;

import java.util.List;

public interface CountryCityService {

    public List<CountryDto> getAllCountries();

    public List<CityDto> getAllCitiesFromSpecificCountry(Long countryId);
}
