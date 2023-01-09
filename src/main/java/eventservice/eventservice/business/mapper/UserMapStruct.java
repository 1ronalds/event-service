package eventservice.eventservice.business.mapper;

import eventservice.eventservice.business.repository.model.UserEntity;
import eventservice.eventservice.model.UserDto;
import eventservice.eventservice.model.UserMinimalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//@Mapper(componentModel = "spring", uses = RoleMapStructImpl.class)
@Mapper(componentModel = "spring")
public interface UserMapStruct {

    @Mapping(source="role", target="roleEntity")
    UserEntity dtoToEntity(UserDto userDto);
    @Mapping(source="roleEntity", target="role")
    UserDto entityToDto(UserEntity userEntity);

}
