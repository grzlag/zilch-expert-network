package com.zilch.zen.core.users;

import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.users.kafka.model.CreateUserDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;


class UserMapperTest {

    UserMapper userMapper = new UserMapper();

    @Test
    public void shouldMapUserDTOtoUser() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO(
                "userId",
                "John Doe",
                "john@example.com"
        );

        // when
        User user = userMapper.toEntity(userDTO);

        // then
        assertThat(user.getId()).isEqualTo("userId");
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertNull(user.getPurchases());
    }

}