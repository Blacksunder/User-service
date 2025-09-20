package com.userservice.producer;

import com.userservice.dto.MessageDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {KafkaProducerConfiguration.class, MessageDtoKafkaSender.class})
@Testcontainers
public class MessageDtoKafkaSenderTest {

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.0")
    );

    @DynamicPropertySource
    private static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.producer.acks", () -> "all");
    }

    @Autowired
    private MessageDtoKafkaSender messageDtoKafkaSender;

    private final MessageDto messageDto = new MessageDto("test@mail.ru");

    @Test
    public void sendCreationMessage_successfulSending() {
        assertDoesNotThrow(() -> messageDtoKafkaSender.sendCreationMessage(messageDto));
        System.out.println("Message is sent");
        checkForMessageSending();
    }

    @Test
    public void sendDeletionMessage_successfulSending() {
        assertDoesNotThrow(() -> messageDtoKafkaSender.sendDeletionMessage(messageDto));
        checkForMessageSending();
    }

    private Consumer<String, MessageDto> createTestConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        ConsumerFactory<String, MessageDto> factory = new DefaultKafkaConsumerFactory<>(props);
        return factory.createConsumer();
    }

    private void checkForMessageSending() {
        try (Consumer<String, MessageDto> consumer = createTestConsumer()) {
            consumer.subscribe(List.of("user_creation", "user_deletion"));
            ConsumerRecords<String, MessageDto> records = consumer.poll(Duration.ofSeconds(10));

            assertFalse(records.isEmpty());

            MessageDto message = records.iterator().next().value();
            assertEquals(messageDto, message);
        }
    }

}
