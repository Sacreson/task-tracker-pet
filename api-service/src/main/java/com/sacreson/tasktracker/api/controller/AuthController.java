package com.sacreson.tasktracker.api.controller;

import com.sacreson.tasktracker.api.dto.LoginRequestDto;
import com.sacreson.tasktracker.api.dto.LoginResponseDto;
import com.sacreson.tasktracker.api.dto.RegisterUserDto;
import com.sacreson.tasktracker.api.dto.UserDto;
import com.sacreson.tasktracker.api.factories.UserDtoFactory;
import com.sacreson.tasktracker.api.service.UserService;
import com.sacreson.tasktracker.api.store.entities.UserEntity;
import com.sacreson.tasktracker.api.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserDtoFactory userDtoFactory;

    @PostMapping(value = Constants.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserEntity userEntity = userService.registerUser(registerUserDto);

        return userDtoFactory.makeUserDto(userEntity);
    }

    @PostMapping(value = Constants.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }
}
