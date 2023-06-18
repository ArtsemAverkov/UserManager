package ru.clevertec.UserManager.service.role;

import ru.clevertec.UserManager.entity.Role;

public interface RoleService {
     Role findRoleByName(String name);
}
