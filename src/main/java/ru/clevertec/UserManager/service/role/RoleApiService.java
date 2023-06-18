package ru.clevertec.UserManager.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.repository.RoleRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleApiService implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findRoleByName(String name) {
        Role role = roleRepository.findRoleByName(name);
        if (Objects.isNull(role)) {
            throw new IllegalArgumentException("Роль с именем " + name + " не найдена");
        }
        return role;
    }
}
