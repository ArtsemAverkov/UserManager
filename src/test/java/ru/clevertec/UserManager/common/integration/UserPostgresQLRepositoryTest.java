package ru.clevertec.UserManager.common.integration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.UserManager.common.WireMockInitializer;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.common.utill.UserBuild;
import ru.clevertec.UserManager.dto.UserRequestProtos;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;

/**
 * Integration test class for the UserRepository.
 */
@DataJpaTest
@Testcontainers
@ExtendWith(ValidParameterResolverUser.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("User Repository Test")
public class UserPostgresQLRepositoryTest extends TestContainerInitializer{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  TestEntityManager testEntityManager;

    @BeforeAll
    static void setup() {
        WireMockInitializer.setup();
    }

    @AfterAll
    static void teardown() {
        WireMockInitializer.teardown();
    }

    /**
     * Tests the existence of an active user name in the UserRepository.
     * @param userDto the user request DTO
     */
    @Test
    void shouldExistActiveUserName(UserRequestProtos.UserRequestDto userDto){
        int activeUserName = userRepository.existActiveUserName(userDto.getUsername());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
       Assertions.assertEquals(1, activeUserName);
    }

    /**
     * Tests finding a user by username in the UserRepository.
     * @param userDto the user request DTO
     */
    @Test
    void shouldFindByUserName(UserRequestProtos.UserRequestDto userDto){
        User userRepositoryByName = userRepository.findByName(userDto.getUsername());
        System.out.println("userRepositoryByName = " + userRepositoryByName);
        User buildUser = UserBuild.buildUserWithId(userDto);
        System.out.println("buildUser = " + buildUser);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertEquals(userRepositoryByName, buildUser );
    }
}
