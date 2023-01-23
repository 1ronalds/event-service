package eventservice.eventservice.business.service.impl;

import eventservice.eventservice.business.connection.CountryCityServiceConnection;
import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.handlers.exceptions.CountryNotFoundException;
import eventservice.eventservice.business.service.CountryCityService;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryCityServiceImpl implements CountryCityService {

    @Autowired
    CountryCityServiceConnection countryCityServiceConnection;


    @Override
    public List<CountryDto> getAllCountries() {
        return countryCityServiceConnection.getCountries();
    }

    @Override
    public List<CityDto> getAllCitiesFromSpecificCountry(Long countryId) {
        return countryCityServiceConnection.getCities(countryId);
    }
}
