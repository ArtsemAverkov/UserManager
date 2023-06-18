package ru.clevertec.UserManager.common.utill;

import org.jetbrains.annotations.NotNull;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;

public class UserBuild {

    public static User buildUser(UserRequestDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(new Role(userDto.getRole()))
                .build();
    }

    public static User buildUserWithId(UserRequestDto userDto){
        return User.builder()
                .id(RequestId.VALUE_1.getValue())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(new Role(userDto.getRole()))
                .build();
    }

    @NotNull
    public static String getContent(UserRequestDto userDto) {
        return "{\n" +
                "  \"username\": \"" + userDto.getUsername() + "\",\n" +
                "  \"password\": \"" + userDto.getPassword() + "\",\n" +
                "  \"role\": \"" + userDto.getRole() + "\"\n" +
                "}";
    }

    @NotNull
    public static Role getRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setUsers(null);
        return role;
    }
}
