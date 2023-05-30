package ru.clevertec.UserManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.UserManager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from users where username =:name", nativeQuery = true)
    User findByName(String name);

}
