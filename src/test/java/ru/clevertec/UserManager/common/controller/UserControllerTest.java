package ru.clevertec.UserManager.common.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.UserManager.UserManagerApplication;
import ru.clevertec.UserManager.common.WireMockInitializer;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.common.utill.UserBuild;
import ru.clevertec.UserManager.controller.UserController;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.security.JwtTokenParser;
import ru.clevertec.UserManager.security.SecurityConfig;
import ru.clevertec.UserManager.service.user.UserService;
import ru.clevertec.UserManager.common.utill.RequestId;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.clevertec.UserManager.common.utill.UserBuild.getContent;

/**
 * Test class for the UserController.
 */
@ContextConfiguration(classes = UserManagerApplication.class)
@WebMvcTest({UserController.class, SecurityConfig.class, JwtTokenParser.class})
@ExtendWith(ValidParameterResolverUser.class)
@AutoConfigureMockMvc
@DisplayName("Testing User Controller")
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Tests the create method of the UserController.
     * @param userDto the user request DTO
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void create(UserRequestProtos.UserRequestDto userDto) throws Exception {
        when(userService.create(any(UserRequestProtos.UserRequestDto.class))).thenReturn(RequestId.VALUE_1.getValue());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(RequestId.VALUE_1.getValue())));
        verify(userService).create(any(UserRequestProtos.UserRequestDto.class));
    }

    /**
     * Tests the delete method of the UserController.
     * @param userDto the user request DTO
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void delete(UserRequestProtos.UserRequestDto userDto) throws Exception {
        User buildUser = UserBuild.buildUser(userDto);
        when(userService.delete(userDto.getUsername())).thenReturn(Boolean.valueOf("true"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{name}",buildUser.getUsername())
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
        verify(userService).delete(userDto.getUsername());
    }

    /**
     * Tests the authenticateUser method of the UserController.
     *
     * @param userDto the user request DTO
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testAuthenticateUser(UserRequestProtos.UserRequestDto userDto) throws Exception {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.eyJleHAiOjE2MzE4NjYxNTl9";
        User buildUser = UserBuild.buildUser(userDto);
        when(userService.authenticateUserWithToken(validToken)).thenReturn(buildUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/authenticate/{token}", validToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"username\": \""+buildUser.getUsername()+"\"}"));
        verify(userService, times(1)).authenticateUserWithToken(any(String.class));
    }

    /**
     * Creates and returns a UserDetails object with a predefined username, password, and role.
     * @return the created UserDetails object
     */
    public static UserDetails createUserDetails(){
        return org.springframework.security.core.userdetails.User.withUsername("username")
                .password("password")
                .roles("ADMIN")
                .build();
    }
}
