package ru.clevertec.UserManager.common.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.UserManager.common.WireMockInitializer;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.Role;
import ru.clevertec.UserManager.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test class for the RoleRepository.
 */
@DataJpaTest
@Testcontainers
@ExtendWith(ValidParameterResolverUser.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Role Repository Test")
public class RolePostgresQlRepositoryTest extends TestContainerInitializer{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    /**
     * Tests the existence of an active user name in the RoleRepository.
     * @param userDto the user request DTO
     */
    @Test
    void shouldExistActiveUserName(UserRequestDto userDto){
        System.out.println("userDto = " + userDto);
        Role roleByName = roleRepository.findRoleByName(userDto.getRole());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        assertEquals(userDto.getRole(), roleByName.getName());
    }
}
