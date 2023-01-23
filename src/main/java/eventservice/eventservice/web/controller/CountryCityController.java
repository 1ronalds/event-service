package eventservice.eventservice.web.controller;

import eventservice.eventservice.business.connection.model.CityDto;
import eventservice.eventservice.business.connection.model.CountryDto;
import eventservice.eventservice.business.service.CountryCityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class CountryCityController {

    @Autowired
    CountryCityService countryCityService;

    /**
     * Gets a list of all the countries in the world
     * @return list of countries
     */
    @ApiOperation(value = "Returns the list of all of the countries in the world")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request is successful"),
    })
    @GetMapping(value = "/countries/all")
    public ResponseEntity<List<CountryDto>> getAllCountries(){
        return ResponseEntity.ok(countryCityService.getAllCountries());
    }

    /**
     * Gets a list of cities in a specific country
     * @param countryId - id of the country
     * @return list of cities
     */
    @ApiOperation(value = "Returns the list of all the cities in a specific country")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request is successful"),
    })
    @GetMapping(value = "/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCountryCities(@ApiParam(value = "Id of the country") @PathVariable(name = "countryId") Long countryId){
        return ResponseEntity.ok(countryCityService.getAllCitiesFromSpecificCountry(countryId));
    }
}
