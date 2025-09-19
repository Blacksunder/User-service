package com.userservice.producer;

import com.userservice.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MessageDtoKafkaSender {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    private static final String USER_CREATION_TOPIC = "user_creation";
    private static final String USER_DELETION_TOPIC = "user_deletion";

    public void sendCreationMessage(MessageDto messageDto) {
        try {
            kafkaTemplate.send(USER_CREATION_TOPIC, messageDto);
        } catch (Exception e) {
            log.error(e.getMessage() + " with message " + messageDto);
        }
    }

    public void sendDeletionMessage(MessageDto messageDto) {
        try {
            kafkaTemplate.send(USER_DELETION_TOPIC, messageDto);
        } catch (Exception e) {
            log.error(e.getMessage() + " with message " + messageDto);
        }

    }
}
