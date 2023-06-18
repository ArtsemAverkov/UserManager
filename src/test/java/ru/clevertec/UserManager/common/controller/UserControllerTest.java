package ru.clevertec.UserManager.common.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.clevertec.UserManager.UserManagerApplication;
import ru.clevertec.UserManager.common.WireMockInitializer;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.common.utill.UserBuild;
import ru.clevertec.UserManager.controller.UserController;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.security.SecurityConfig;
import ru.clevertec.UserManager.service.UserService;
import ru.clevertec.UserManager.common.utill.RequestId;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.clevertec.UserManager.common.utill.UserBuild.getContent;

@ContextConfiguration(classes = UserManagerApplication.class)
@WebMvcTest({UserController.class, SecurityConfig.class})
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

    @Test
    public void create(UserRequestDto userDto) throws Exception {
        when(userService.create(any(UserRequestDto.class))).thenReturn(RequestId.VALUE_1.getValue());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(RequestId.VALUE_1.getValue())));
        verify(userService).create(any(UserRequestDto.class));
    }

    @Test
    public void delete(UserRequestDto userDto) throws Exception {
        User buildUser = UserBuild.buildUser(userDto);
        when(userService.delete(userDto.getUsername())).thenReturn(Boolean.valueOf("true"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{name}",buildUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
        verify(userService).delete(userDto.getUsername());
    }

    @Test
    public void testAuthenticateUser(UserRequestDto userDto) throws Exception {
        String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.eyJleHAiOjE2MzE4NjYxNTl9";
        User buildUser = UserBuild.buildUser(userDto);
        when(userService.authenticateUserWithToken(validToken)).thenReturn(buildUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/authenticate/{token}", validToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"username\": \""+buildUser.getUsername()+"\"}"));
        verify(userService, times(1)).authenticateUserWithToken(any(String.class));
    }
}
