package ru.clevertec.UserManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.UserManager.entity.User;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their name.
     * @param name the name of the user
     * @return the User object with the specified name
     */
    @Query(value = "select * from users where username =:name", nativeQuery = true)
    User findByName(String name);

    /**
     * Checks if an active user with the given name exists.
     * @param name the name of the user
     * @return the number of active users with the specified name
     */
    @Query(value = "select count(username) from users where username =:name", nativeQuery = true)
    int existActiveUserName (String name);
}
