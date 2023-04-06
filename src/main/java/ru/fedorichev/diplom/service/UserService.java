package ru.fedorichev.diplom.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fedorichev.diplom.entity.User;
import ru.fedorichev.diplom.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findOneByLogin(login);
    }
}
