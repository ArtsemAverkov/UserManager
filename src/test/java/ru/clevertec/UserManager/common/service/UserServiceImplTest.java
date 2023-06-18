package ru.clevertec.UserManager.common.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;
import ru.clevertec.UserManager.security.JwtTokenParser;
import ru.clevertec.UserManager.service.UserApiService;
import ru.clevertec.UserManager.common.utill.RequestId;
import ru.clevertec.UserManager.service.role.RoleService;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static ru.clevertec.UserManager.common.utill.UserBuild.*;

@DisplayName("User Service Test")
public class UserServiceImplTest {
    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverUser.class})
    public class ValidData {

        @InjectMocks
        private UserApiService userApiService;

        @Mock
        private JwtTokenParser jwtTokenParser;

        @Mock
        private UserRepository userRepository;

        @Mock
        private RoleService roleService;


        @Test
        void shouldFindByNameUserWhenUserValid(UserRequestDto userDto) {
            User buildUser = buildUser(userDto);
            when(userRepository.findByName(userDto.getUsername()))
                    .thenReturn(buildUser);
            assertEquals(buildUser, userApiService.findByName(userDto.getUsername()));
            verify(userRepository, times(1)).findByName(userDto.getUsername());
        }

        @Test
        void shouldCreateUserWhenUserIsValid(UserRequestDto userDto) {
            Role role = getRole();
            User userForMethodCreate = buildUserWithId(userDto);
            when(roleService.findRoleByName(userDto.getRole())).thenReturn(role);
            when(userRepository.save(any(User.class))).thenReturn(userForMethodCreate);
            assertEquals(RequestId.VALUE_1.getValue(), userApiService.create(userDto));
        }

        @Test
        void shouldExistActiveUserNameWhenUserNameIsActive(UserRequestDto userDto) {
            when(userRepository.existActiveUserName(userDto.getUsername())).thenReturn(1);
            assertThrows(IllegalArgumentException.class, () -> userApiService.create(userDto));
            verify(userRepository, times(1)).existActiveUserName(userDto.getUsername());
        }


        @Test
        void shouldDeleteUserWhenUserIsValid(UserRequestDto userDto) {
            User buildUser = buildUserWithId(userDto);
            when(userRepository.findByName(userDto.getUsername()))
                    .thenReturn(buildUser);
            assertTrue(userApiService.delete(userDto.getUsername()));
            verify(userRepository, times(1)).deleteById(RequestId.VALUE_1.getValue());
        }

        @Test
        void testAuthenticateUserWithValidToken(UserRequestDto userDto) {
            User user = buildUserWithId(userDto);
            String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.eyJleHAiOjE2MzE4NjYxNTl9";
            Jws<Claims> mockClaims = mock(Jws.class);
            when(mockClaims.getBody()).thenReturn(mock(Claims.class));
            when(mockClaims.getBody().getSubject()).thenReturn(user.getUsername());
            when(jwtTokenParser.validateToken(validToken)).thenReturn(mockClaims);
            when(userRepository.findByName(user.getUsername())).thenReturn(user);
            User result = userApiService.authenticateUserWithToken(validToken);
            assertNotNull(result);
            assertEquals(user.getUsername(), result.getUsername());
            verify(userRepository, times(1)).findByName(user.getUsername());
        }
    }

    @Nested
    @ExtendWith({MockitoExtension.class, ValidParameterResolverUser.class})
    public class InvalidData {

        @InjectMocks
        private UserApiService userApiService;

        @Mock
        private UserRepository userRepository;

        @Test
        void shouldFindByNameUserWhenUserInvalid(UserRequestDto userDto) {
            when(userRepository.findByName(userDto.getUsername()))
                            .thenReturn(null);
            assertThrows(IllegalArgumentException.class,
                    () -> userApiService.findByName(userDto.getUsername()));

        }

        @Test
        void shouldCreateUserWhenExistActiveUserNameInvalid(UserRequestDto userDto) {
            when(userRepository.existActiveUserName(userDto.getUsername())).thenReturn(1);
            assertThrows(IllegalArgumentException.class,
                    () -> userApiService.create(userDto));

        }
    }
}
