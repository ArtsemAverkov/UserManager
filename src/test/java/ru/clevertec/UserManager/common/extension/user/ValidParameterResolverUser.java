package ru.clevertec.UserManager.common.extension.user;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.utill.UserRole;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValidParameterResolverUser implements ParameterResolver {
    private final List<UserRequestProtos.UserRequestDto> userDtoList = Arrays.asList(
            UserRequestProtos.UserRequestDto.newBuilder()
                    .setUsername("admin")
                    .setPassword("admin123")
                    .setRole(UserRole.ADMIN.getName())
                    .build()
            );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserRequestProtos.UserRequestDto.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return userDtoList.get(new Random().nextInt(userDtoList.size()));
    }
}
