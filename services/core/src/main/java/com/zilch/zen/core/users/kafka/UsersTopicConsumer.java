package com.zilch.zen.core.users.kafka;

import com.zilch.zen.core.users.UserService;
import com.zilch.zen.core.users.kafka.model.CreateUserDTO;
import com.zilch.zen.core.utils.JsonSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UsersTopicConsumer {

    private final JsonSerializer jsonSerializer;
    private final UserService userService;

    public UsersTopicConsumer(JsonSerializer jsonSerializer, UserService userService) {
        this.jsonSerializer = jsonSerializer;
        this.userService = userService;
    }

    @KafkaListener(
            topics = "${spring.kafka.consumers.users.topic-name}",
            groupId = "${spring.kafka.consumers.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        CreateUserDTO createUserDTO = jsonSerializer.deserialize(message, CreateUserDTO.class);
        userService.saveUser(createUserDTO);
    }

}
