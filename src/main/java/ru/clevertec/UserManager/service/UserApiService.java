package ru.clevertec.UserManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.UserManager.dto.UserDto;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;
import ru.clevertec.UserManager.entity.UserRole;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApiService implements UserService{

    private final UserRepository userRepository;
    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);

//                .orElseThrow(() ->
//                new IllegalArgumentException("User not found for id"+ id));
    }

    @Override
    public Long create(UserDto user) {
        User buildUser = buildCreateUser(user);
        return userRepository.save(buildUser).getId();
    }

    @Override
    public boolean delete(String name) {
        User byName = findByName(name);
        userRepository.deleteById(byName.getId());
        return true;
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    private User buildCreateUser(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(new Role(UserRole.SUBSCRIBER.getName()))
                .build();
    }

    private User buildUpdateUser(UserDto userDto,  Role roles){
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(roles)
                .build();
    }
}
