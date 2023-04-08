package ru.fedorichev.diplom.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fedorichev.diplom.entity.User;
import ru.fedorichev.diplom.repository.UserRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void initEach(){
        userRepository = BDDMockito.mock(UserRepository.class, BDDMockito.RETURNS_DEEP_STUBS);
        userService = new UserService(userRepository);
        user = User.builder()
                .id(1L)
                .login("test_user")
                .password("test_password")
                .build();
    }

    @DisplayName("JUnit test for getUserByLogin method")
    @Test
    public void testGetUserByLogin(){
        given(userRepository.findOneByLogin("test_user")).willReturn(Optional.of(user));

        User savedUser = userService.getByLogin(user.getLogin()).get();

        Assertions.assertNotNull(savedUser);
        System.out.println("Test getUserByLogin completed!");
    }


}
