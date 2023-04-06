package ru.fedorichev.diplom.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fedorichev.diplom.component.JwtRequest;
import ru.fedorichev.diplom.component.Login;
import ru.fedorichev.diplom.entity.User;
import ru.fedorichev.diplom.security.authentication.JwtAuthentication;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public Login login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            user.setAuthToken(accessToken);
            return new Login(accessToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
