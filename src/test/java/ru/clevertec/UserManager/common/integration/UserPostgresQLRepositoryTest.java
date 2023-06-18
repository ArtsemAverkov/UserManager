package ru.clevertec.UserManager.common.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.UserManager.common.extension.user.ValidParameterResolverUser;
import ru.clevertec.UserManager.common.utill.UserBuild;
import ru.clevertec.UserManager.dto.UserRequestDto;
import ru.clevertec.UserManager.entity.User;
import ru.clevertec.UserManager.repository.UserRepository;


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

    @Test
    void shouldExistActiveUserName(UserRequestDto userDto){
        int activeUserName = userRepository.existActiveUserName(userDto.getUsername());
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
       Assertions.assertEquals(1, activeUserName);
    }

    @Test
    void shouldFindByUserName(UserRequestDto userDto){
        User userRepositoryByName = userRepository.findByName(userDto.getUsername());
        User buildUser = UserBuild.buildUserWithId(userDto);
        testEntityManager.flush();
        testEntityManager.getEntityManager().getTransaction().commit();
        Assertions.assertEquals(userRepositoryByName, buildUser );
    }
}
