package ru.clevertec.UserManager.service.user;

import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.User;


public interface UserService {
    User findByName(String name);
    Long create(UserRequestProtos.UserRequestDto userRequestDto);
    boolean delete(String name);
    User authenticateUserWithToken (String token);

}
