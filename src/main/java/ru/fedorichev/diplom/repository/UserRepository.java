package ru.fedorichev.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fedorichev.diplom.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long id);
}
