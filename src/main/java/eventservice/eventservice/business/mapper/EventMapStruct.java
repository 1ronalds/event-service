package eventservice.eventservice.business.mapper;

import eventservice.eventservice.business.repository.UserRepository;
import eventservice.eventservice.business.repository.model.EventEntity;
import eventservice.eventservice.business.repository.model.UserEntity;
import eventservice.eventservice.model.EventDto;
import eventservice.eventservice.model.EventMinimalDto;
import eventservice.eventservice.model.UserMinimalDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = {UserMapStructImpl.class, EventTypeMapStructImpl.class})
public interface EventMapStruct {
    EventDto entityToDto(EventEntity eventEntity);

    @Mapping(source = "organiser", target = "organiser", qualifiedByName = "organiserMapper")
    EventEntity dtoToEntity(EventDto eventDto, @Context UserRepository userRepository);

    EventMinimalDto entityToMinimalDto(EventEntity eventEntity);

    @Named("organiserMapper")
    static UserEntity organiserMapper(UserMinimalDto userMinimalDto, @Context UserRepository userrepository){
        return userrepository.findById(userMinimalDto.getId()).get();
    }

}
