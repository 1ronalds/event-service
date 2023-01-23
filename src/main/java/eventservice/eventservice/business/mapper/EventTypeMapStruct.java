package eventservice.eventservice.business.mapper;

import eventservice.eventservice.business.repository.model.EventTypeEntity;
import eventservice.eventservice.model.EventTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventTypeMapStruct {

    EventTypeDto entityToDto(EventTypeEntity entity);
    EventTypeEntity dtoToEntity(EventTypeDto entity);
}
