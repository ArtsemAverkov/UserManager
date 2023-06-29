package ru.clevertec.UserManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.service.user.UserService;

/**
 * Controller class that handles user-related operations.
 */
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     * @param userDto the UserRequestDto containing the user details
     * @return the ID of the created user
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserRequestProtos.UserRequestDto userDto){
         return userService.create(userDto);
    }

    /**
     * Deletes a user by their name.
     * @param name the name of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable @Valid String name){
        return  userService.delete(name);
    }

    /**
     * Authenticates a user using a token.
     * @param token the authentication token
     * @return the authenticated User object
     */
    @GetMapping(value = "/authenticate/{token}")
    public User authenticateUser(@PathVariable String token) {
        return userService.authenticateUserWithToken(token);
    }
}
