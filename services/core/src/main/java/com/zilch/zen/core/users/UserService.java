package com.zilch.zen.core.users;

import com.zilch.zen.core.users.entity.UserRepository;
import com.zilch.zen.core.users.kafka.model.CreateUserDTO;
import com.zilch.zen.core.users.entity.User;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User saveUser(CreateUserDTO userDTO) {
        User newUser = userMapper.toEntity(userDTO);
        return userRepository.save(newUser);
    }
}
