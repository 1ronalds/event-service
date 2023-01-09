package eventservice.eventservice.business.connection;

import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "countrycityservice", url = "localhost:8081/api/country-city-service")
public interface CountryCityServiceConnection {

    @GetMapping("/v1/countries")
    public List<CountryDto> getCountries();

    @GetMapping("/v1/cities/{country-id}")
    public List<CityDto> getCities(@PathVariable("country-id") Long countryId);

}
