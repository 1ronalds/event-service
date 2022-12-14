package eventservice.eventservice.business.mapper;

import eventservice.eventservice.business.repository.model.EventEntity;
import eventservice.eventservice.model.EventDto;
import eventservice.eventservice.model.EventMinimalDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapStructImpl.class, EventTypeMapStructImpl.class})
public interface EventMapStruct {
    EventDto entityToDto(EventEntity eventEntity);
    EventEntity dtoToEntity(EventDto eventDto);
    EventMinimalDto entityToMinimalDto(EventEntity eventEntity);


}
