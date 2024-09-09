package com.zilch.zen.core.messagesender;

import com.zilch.zen.core.payments.kafka.PaymentDTO;
import com.zilch.zen.core.users.entity.User;
import com.zilch.zen.core.users.entity.UserRepository;
import com.zilch.zen.core.utils.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class EmailMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonSerializer jsonSerializer;
    private final UserRepository userRepository;
    private final MessageDTOMapper messageDTOMapper;

    @Value("${spring.kafka.producers.payments-send-message.topic-name}")
    private String producerTopicName;

    public EmailMessageSender(
            KafkaTemplate<String, String> kafkaTemplate,
            JsonSerializer jsonSerializer,
            UserRepository userRepository,
            MessageDTOMapper messageDTOMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonSerializer = jsonSerializer;
        this.userRepository = userRepository;
        this.messageDTOMapper = messageDTOMapper;
    }

    public void sendMessage(PaymentDTO paymentDTO) {
        User user = userRepository.findById(paymentDTO.userId()).orElseThrow(() -> new IllegalStateException("User not found"));
        MessageDTO messageDTO = messageDTOMapper.mapToMessageDTO(paymentDTO, user);
        String serializedEvent = jsonSerializer.serialize(messageDTO);
        kafkaTemplate.send(producerTopicName, serializedEvent);
    }


}
