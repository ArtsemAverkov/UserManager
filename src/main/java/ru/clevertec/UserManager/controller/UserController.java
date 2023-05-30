package ru.clevertec.UserManager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "{name}")
    @ResponseStatus(HttpStatus.OK)
    public User read(@PathVariable @Valid String name){
        return userService.findByName(name);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> readAll(){
        return userService.readAll();
    }


}
