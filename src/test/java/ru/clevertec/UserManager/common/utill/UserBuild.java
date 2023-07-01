package ru.clevertec.UserManager.common.utill;

import org.jetbrains.annotations.NotNull;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.entity.User;

public class UserBuild {

    public static User buildUser(UserRequestProtos.UserRequestDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(new Role(userDto.getRole()))
                .build();
    }

    public static User buildUserWithId(UserRequestProtos.UserRequestDto userDto){
        return User.builder()
                .id(RequestId.VALUE_1.getValue())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(new Role(userDto.getRole()))
                .build();
    }

    @NotNull
    public static String getContent(UserRequestProtos.UserRequestDto userDto) {
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
        role.setName("JOURNALIST");
        role.setUsers(null);
        return role;
    }
}
