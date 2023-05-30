package ru.clevertec.UserManager.service;

import ru.clevertec.UserManager.dto.UserDto;
import ru.clevertec.UserManager.entity.User;

import java.util.List;

public interface UserService {
    User findByName(String name);
    Long create(UserDto user);
    boolean delete(String name);
    List<User> readAll();

}
