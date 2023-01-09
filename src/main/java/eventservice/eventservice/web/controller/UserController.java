package eventservice.eventservice.web.controller;

import eventservice.eventservice.business.service.UserService;
import eventservice.eventservice.model.RoleDto;
import eventservice.eventservice.model.UserDto;
import eventservice.eventservice.swagger.HTTPResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;
import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserService service;

    /**
     * Finds all details of specific user
     * @param username
     */
    @ApiOperation(value = "Finds all details of specific user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 404, message = HTTPResponseMessages.HTTP_404)
    })
    @GetMapping("/users/{username}")
    public ResponseEntity<UserDto> findUserDetails(@ApiParam(value = "username") @PathVariable String username){
        log.info("findUserDetails controller method called with parameter username: {}", username);
        return ResponseEntity.ok(service.findUserDetails(username));
    }

    /**
     * Create new user
     * @param user
     */
    @ApiOperation(value = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 400, message = HTTPResponseMessages.HTTP_400)
    })
    @PostMapping("/users")
    public ResponseEntity<UserDto> saveUser(@Valid @ApiParam(value = "UserDto") @RequestBody UserDto user){
        log.info("saveUser controller method called with request body: {}", user);
        return ResponseEntity.ok(service.saveUser(user));
    }

    /**
     * Edit user details
     * @param username
     * @param user
     */
    @ApiOperation(value = "Edit user details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 404, message = HTTPResponseMessages.HTTP_404),
            @ApiResponse(code = 400, message = HTTPResponseMessages.HTTP_400)
    })
    @PutMapping("/users/{username}")
    public ResponseEntity<UserDto> editUser(@ApiParam(value = "username") @PathVariable String username,
                                            @Valid @ApiParam(value="userDto") @RequestBody UserDto user){
        log.info("editUser controller method called with parameter username: {} and request body: {}", username, user);
        return ResponseEntity.ok(service.editUser(user, username));
    }

    /**
     * Delete user
     * @param username
     */
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTTPResponseMessages.HTTP_200),
            @ApiResponse(code = 500, message = HTTPResponseMessages.HTTP_500),
            @ApiResponse(code = 404, message = HTTPResponseMessages.HTTP_404)
    })
    @DeleteMapping("/users/{username}")
    public ResponseEntity<Void> deleteUser(@ApiParam(value="username") @PathVariable String username){
        log.info("deleteUser controller method called with parameter username: {}", username);
        service.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

}
