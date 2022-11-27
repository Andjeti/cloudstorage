package ru.netology.cloudstorage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.netology.cloudstorage.config.JwtTokenUtil;
import ru.netology.cloudstorage.dto.AuthRequest;
import ru.netology.cloudstorage.dto.CreateUserRequest;
import ru.netology.cloudstorage.dto.UserView;
import ru.netology.cloudstorage.dto.UserViewMapper;
import ru.netology.cloudstorage.model.CloudUser;
import ru.netology.cloudstorage.service.UserDetailsServiceImpl;
import javax.validation.Valid;

@RestController @RequestMapping(path = "/cloud")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    // зачем нужен класс userViewMapper, случайно это не специфика MongoDB?
    private final UserViewMapper userViewMapper;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthController(AuthenticationManager authenticationManager,
                   JwtTokenUtil jwtTokenUtil,
                   UserViewMapper userViewMapper,
                   UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userViewMapper = userViewMapper;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

//    FRONT приложение использует header auth-token в котором отправляет токен (ключ-строка)
//    для идентификации пользователя на BACKEND. Для получения токена нужно пройти авторизацию
//    на BACKEND и отправить на метод /login пару логин и пароль, в случае успешной проверки
//    в ответ BACKEND должен вернуть json объект с полем auth-token и значением токена.
    @PostMapping("login")
    public ResponseEntity<UserView> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            CloudUser cloudUser = (CloudUser)authenticate.getPrincipal();
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(cloudUser))
                    .body(userViewMapper.toUserView(cloudUser));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//    По заданию в дипломной нужно сделать разлогин
//    Для выхода из приложения нужно вызвать метод BACKEND /logout,
//    который удалит/деактивирует токен и последующие запросы с этим токеном
//    будут не авторизованы и возвращать код 401.
////    @PostMapping("logout")
//    public HttpStatus logout(@RequestHeader("auth-token") String token) {
//    authController.deleteTokenByUser(jwtTokenUtil.getUsername(token);
//    return HttpStatus.OK;
//    }
//
//    Map<String, string> tokenUser = new HashMap<>();
//    public void setTokenByUser (String username, String token){ tokenUser.put(username, token); }
//    public void deleteTokenByUser (String username){ tokenUser.remove(username); }


//    По заданию в дипломной не нужно регистрировать новых пользователей, скрыла
//    @PostMapping("register")
//    public UserView register(@RequestBody @Valid CreateUserRequest request) {
//        return userDetailsServiceImpl.create(request);
//    }
}
