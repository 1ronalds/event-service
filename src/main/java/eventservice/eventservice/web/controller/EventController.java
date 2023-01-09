package eventservice.eventservice.web.controller;

import eventservice.eventservice.business.service.EventService;
import eventservice.eventservice.model.EventDto;
import eventservice.eventservice.model.EventMinimalDto;
import eventservice.eventservice.swagger.HTTPResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class EventController {
    private final EventService eventService;
    /**
     * Finds all public events based on certain criteria through parameter input
     * @param country - the country, where the event is taking place
     * @param city  - the city, where the event is taking place
     * @param dateFrom - the start of the date interval, which is used to find events
     *                   that are taking place during a certain time period
     * @param dateTo  - the end of the date interval, which is used to find events that
     *                  are taking place during a certain time period
     * */
    @ApiOperation(value = "Finds all public events by country and city, date interval if specified")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request is successful"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters not valid")
    })
    @GetMapping(value = "/events/event")
    public ResponseEntity<List<EventMinimalDto>> findAllPublicEvents(@ApiParam(value = "country, where the event will take place", required = true)
                                                                  @RequestParam(name = "country") String country,
                                                                     @ApiParam(value = "city, where the event will take place")
                                                                  @RequestParam(name = "city", required = false) String city,
                                                                     @ApiParam(value = "The date from which events will take place")
                                                                  @RequestParam(name = "date_from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
                                                                     @ApiParam(value = "The date to which events will take place")
                                                                  @RequestParam(name = "date_to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateTo){
        log.info("findAllPublicEvents controller method called with parameters " +
                "country: {}, city: {}, date_from: {}, date_to: {} ", country, city, dateFrom, dateTo);
        return ResponseEntity.ok(eventService.findAllPublicEvents(country, city, dateFrom, dateTo));
    }

    /**
     * Returns full information of event by id
     * @param eventId
     */
    @ApiOperation(value = "Return full information of event by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_404)
    })
    @GetMapping("/events/event/{event-id}")
    public ResponseEntity<EventDto> findEventInfo(@PathVariable("event-id") Long eventId) {
        log.info("findEventInfo controller method is called with eventId: {}", eventId);
        return ResponseEntity.ok(eventService.findEventInfo(eventId));
    }

    /**
     * Saves new event and returns its full information to user
     * @param userName
     * @param event
     */
    @ApiOperation(value = "Saves new event and returns its full information to user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTTPResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500)
    })
    @PostMapping("/events/user/{user-name}")
    public ResponseEntity<EventDto> saveEvent(@PathVariable("user-name") String userName, @Valid @RequestBody EventDto event) {
        log.info("saveEvent controller method is called with user name: {} and event DTO: {}", userName, event.toString());
        return ResponseEntity.ok(eventService.saveEvent(userName, event));
    }

    /**
     * Edits user information
     * @param username
     * @param eventId
     * @param event
     */
    @ApiOperation(value = "Edits user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTTPResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 404, message = HTTPResponseMessages.HTTP_404),
    })
    @PutMapping("/events/user/{user-name}/event/{event-id}")
    public ResponseEntity<EventDto> editEvent(@PathVariable("user-name") String username,
                                              @PathVariable("event-id") Long eventId,
                                              @Valid @RequestBody EventDto event) {
        log.info("editEvent controller method called with username: {}, eventId: {}, eventDTO: {}", username, eventId, event.toString());
        return ResponseEntity.ok(eventService.editEvent(username, eventId, event));
    }

    /**
     * Deletes event
     * @param userName
     * @param eventId
     */
    @ApiOperation(value = "Deletes event")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTTPResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
    })
    @DeleteMapping("/events/user/{user-name}/event/{event-id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("user-name") String userName,
                                            @PathVariable("event-id") Long eventId){
        eventService.deleteEvent(userName, eventId);
        log.info("deleteEvent is called with userName: {} and eventId: {}", userName, eventId);
        return ResponseEntity.noContent().build();
    }
}
