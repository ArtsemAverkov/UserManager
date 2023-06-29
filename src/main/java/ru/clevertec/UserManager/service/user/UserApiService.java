package ru.clevertec.UserManager.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;
import ru.clevertec.UserManager.security.JwtTokenParser;
import ru.clevertec.UserManager.service.role.RoleService;

import java.util.Objects;

/**
 * Service class for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserApiService implements UserService{

    private final UserRepository userRepository;
    private final JwtTokenParser jwtTokenParser;
    private final RoleService roleApiService;

    /**
     * Authenticates a user with the provided token.
     * @param token the token to authenticate the user
     * @return the authenticated user
     * @throws IllegalArgumentException if the token is invalid or not found
     */
    @Override
    public User authenticateUserWithToken(String token) {
        Jws<Claims> claims = jwtTokenParser.validateToken(token);
        if (Objects.isNull(claims)) {
            throw new IllegalArgumentException("Token with value '" + token + "' not found");
        }
        String username = claims.getBody().getSubject();
        return userRepository.findByName(username);
    }

    /**
     * Finds a user by their name.
     * @param name the name of the user
     * @return the found user
     * @throws IllegalArgumentException if the user with the specified name is not found
     */
    @Override
    public User findByName(String name) {
        User user = userRepository.findByName(name);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("User with name '" + name + "' not found");
        }
        return user;
    }

    /**
     * Creates a new user with the provided user request data.
     * @param userRequestDto the user request data
     * @return the ID of the created user
     * @throws IllegalArgumentException if the specified role is invalid or already exists
     */
    @Override
    public Long create(UserRequestProtos.UserRequestDto userRequestDto) {
        if (Objects.equals(userRequestDto.getRole(), "ADMIN")){
            throw new IllegalArgumentException("Role with name '" + userRequestDto.getRole() + "' cannot be used");
        }
        int activeUserName = userRepository.existActiveUserName(userRequestDto.getUsername());
        if (activeUserName>0) {
            throw new IllegalArgumentException("User with name '" + userRequestDto.getUsername() + "' already exists");
        }
        Role roleByName = roleApiService.findRoleByName(userRequestDto.getRole());
        User buildUser = buildCreateUser(userRequestDto);
            buildUser.setRole(roleByName);
            return userRepository.save(buildUser).getId();
    }

    /**
     * Deletes a user with the specified name.
     * @param name the name of the user to delete
     * @return true if the user is deleted successfully, false otherwise
     */
    @Override
    @PreAuthorize("isAuthenticated()")
    public boolean delete(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            User byName = findByName(name);
            if (byName.getUsername().equals(authentication.getName())) {
                userRepository.deleteById(byName.getId());
                return true;
            } else {
                throw new AccessDeniedException(
                        "You cannot delete a user because you are not one");
            }
        } else {
            throw new AccessDeniedException(
                    "You are not an authorized user, please login or register");
        }
    }

    /**
     * Builds a new user based on the provided user request data.
     * @param userRequestDto the user request data
     * @return the built user
     */
    private User buildCreateUser(UserRequestProtos.UserRequestDto userRequestDto){
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .build();
    }
}
