package com.userservice.producer;

import com.userservice.dto.MessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
@ContextConfiguration(classes = TestKafkaConfig.class)
public class MessageDtoKafkaSenderTest {

    @Autowired
    private MessageDtoKafkaSender messageDtoKafkaSender;

    private final MessageDto messageDto = new MessageDto("test@mail.ru");

    @Test
    public void sendCreationMessage_successfulSending() {
        assertDoesNotThrow(() -> messageDtoKafkaSender.sendCreationMessage(messageDto));
    }

    @Test
    public void sendDeletionMessage_successfulSending() {
        assertDoesNotThrow(() -> messageDtoKafkaSender.sendDeletionMessage(messageDto));
    }
}
