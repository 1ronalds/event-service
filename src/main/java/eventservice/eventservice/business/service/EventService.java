package eventservice.eventservice.business.service;

import eventservice.eventservice.model.EventDto;
import eventservice.eventservice.model.EventMinimalDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EventService {
    List<EventMinimalDto> findAllPublicEvents(String country, String city, LocalDate dateFrom, LocalDate dateTo);
    EventDto findEventInfo(Long eventId);
    EventDto saveEvent(String username, EventDto event);
    EventDto editEvent(String username, Long eventId, EventDto event);
    void deleteEvent(String username, Long eventId);
}
