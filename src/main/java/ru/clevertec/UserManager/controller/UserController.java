package ru.clevertec.UserManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.service.UserService;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid UserRequestDto userDto){
         return userService.create(userDto);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable @Valid String name){
        return  userService.delete(name);
    }


    @GetMapping(value = "/authenticate/{token}")
    public User authenticateUser(@PathVariable String token) {
        return userService.authenticateUserWithToken(token);
    }

}
