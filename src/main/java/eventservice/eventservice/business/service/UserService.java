package eventservice.eventservice.business.service;

import eventservice.eventservice.model.UserDto;

public interface UserService {

    UserDto findUserDetails(String username);
    UserDto saveUser(UserDto user);
    UserDto editUser(UserDto user, String username);
    void deleteUser(String username);
}
