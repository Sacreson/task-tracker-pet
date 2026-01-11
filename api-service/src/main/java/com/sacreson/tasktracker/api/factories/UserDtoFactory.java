package com.sacreson.tasktracker.api.factories;

import com.sacreson.tasktracker.api.dto.UserDto;
import com.sacreson.tasktracker.store.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoFactory {

    public UserDto makeUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }

}
