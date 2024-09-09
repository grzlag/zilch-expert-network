package com.zilch.zen.core.users.kafka.model;

public record CreateUserDTO(
        String id,
        String username,
        String email
) {
}
