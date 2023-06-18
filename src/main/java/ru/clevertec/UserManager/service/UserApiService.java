package ru.clevertec.UserManager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;
import ru.clevertec.UserManager.security.JwtTokenParser;
import ru.clevertec.UserManager.service.role.RoleService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserApiService implements UserService{

    private final UserRepository userRepository;
    private final JwtTokenParser jwtTokenParser;
    private final RoleService roleApiService;


    @Override
    public User authenticateUserWithToken(String token) {
        Jws<Claims> claims = jwtTokenParser.validateToken(token);
        if (Objects.isNull(claims)) {
            throw new IllegalArgumentException("Token with value '" + token + "' not found");
        }
        String username = claims.getBody().getSubject();
        return userRepository.findByName(username);
    }

    @Override
    public User findByName(String name) {
        User user = userRepository.findByName(name);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("User with name '" + name + "' not found");
        }
        return user;
    }

    @Override
    public Long create(UserRequestDto userRequestDto) {
        int activeUserName = userRepository.existActiveUserName(userRequestDto.getUsername());
        if (activeUserName>0) {
            throw new IllegalArgumentException("User with name '" + userRequestDto.getUsername() + "' already exists");
        }
        Role roleByName = roleApiService.findRoleByName(userRequestDto.getRole());
        User buildUser = buildCreateUser(userRequestDto);
            buildUser.setRole(roleByName);
            return userRepository.save(buildUser).getId();
    }

    @Override
    public boolean delete(String name) {
        User byName = findByName(name);
        userRepository.deleteById(byName.getId());
        return true;
    }

    private User buildCreateUser(UserRequestDto userRequestDto){
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .build();
    }
}
