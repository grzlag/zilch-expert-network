package com.zilch.zen.core.users;

import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.users.kafka.model.CreateUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.username());
        user.setEmail(userDTO.email());
        return user;
    }
}
