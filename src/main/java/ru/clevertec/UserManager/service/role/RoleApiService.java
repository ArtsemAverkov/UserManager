package ru.clevertec.UserManager.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.repository.RoleRepository;

import java.util.Objects;

/**
 * Service class for managing roles.
 */
@Service
@RequiredArgsConstructor
public class RoleApiService implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * Finds a role by its name.
     * @param name the name of the role
     * @return the found role
     * @throws IllegalArgumentException if the role with the specified name is not found
     */
    @Override
    public Role findRoleByName(String name) {
        Role role = roleRepository.findRoleByName(name);
        if (Objects.isNull(role)) {
            throw new IllegalArgumentException("Роль с именем " + name + " не найдена");
        }
        return role;
    }
}
