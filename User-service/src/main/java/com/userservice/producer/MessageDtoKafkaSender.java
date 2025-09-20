package com.userservice.producer;

import com.userservice.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class MessageDtoKafkaSender {

    private final KafkaTemplate<String, MessageDto> kafkaTemplate;

    @Value("${creation_topic}")
    private String userCreationTopic;

    @Value("${deletion_topic}")
    private String userDeletionTopic;

    public MessageDtoKafkaSender(KafkaTemplate<String, MessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCreationMessage(MessageDto messageDto) {
        try {
            kafkaTemplate.send(userCreationTopic, messageDto);
        } catch (Exception e) {
            logException(e, messageDto);
        }
    }

    public void sendDeletionMessage(MessageDto messageDto) {
        try {
            kafkaTemplate.send(userDeletionTopic, messageDto);
        } catch (Exception e) {
            logException(e, messageDto);
        }

    }

    private void logException(Exception e, MessageDto messageDto) {
        log.error(e.getMessage() + " with message " + messageDto);
        log.error(Arrays.toString(e.getStackTrace()));
    }

}
