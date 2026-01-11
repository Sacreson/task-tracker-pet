package com.sacreson.tasktracker.api.service;

import com.sacreson.tasktracker.api.dto.LoginRequestDto;
import com.sacreson.tasktracker.api.dto.LoginResponseDto;
import com.sacreson.tasktracker.api.dto.RegisterUserDto;
import com.sacreson.tasktracker.api.security.JwtUtil;
import com.sacreson.tasktracker.store.entities.UserEntity;
import com.sacreson.tasktracker.store.enums.Role;
import com.sacreson.tasktracker.store.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserEntity registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        UserEntity user = UserEntity.builder()
                .username(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect");
        }
        String generatedToker = jwtUtil.generatedToker(userEntity.getUsername());

        return new LoginResponseDto(generatedToker);
    }


}
