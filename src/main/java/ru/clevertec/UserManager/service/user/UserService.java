package ru.clevertec.UserManager.service.user;

import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.User;


public interface UserService {
    User findByName(String name);
    Long create(UserRequestDto userRequestDto);
    boolean delete(String name);
    User authenticateUserWithToken (String token);

}
