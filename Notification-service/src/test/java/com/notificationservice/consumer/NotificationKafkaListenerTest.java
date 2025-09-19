package com.notificationservice.consumer;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.service.NotificationService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(topics = {"user_creation", "user_deletion"})
@ContextConfiguration(classes = TestKafkaConfig.class)
public class NotificationKafkaListenerTest {

    private final String messageJson = """
        {
            "destination": "test@example.com"
        }
        """;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    public void listenMessageDto_shouldGetMessageServiceShouldSendCreation() throws Exception {

        try (Producer<String, String> producer = createTestProducer()) {
            producer.send(new ProducerRecord<>("user_creation", messageJson));
        }

        verify(notificationService, timeout(5000))
                .sendMessage(any(MessageDto.class), eq(MessageType.CREATE));
    }

    @Test
    public void listenMessageDto_shouldGetMessageServiceShouldSendDeletion() throws Exception {
        try (Producer<String, String> producer = createTestProducer()) {
            producer.send(new ProducerRecord<>("user_deletion", messageJson));
        }

        verify(notificationService, timeout(5000))
                .sendMessage(any(MessageDto.class), eq(MessageType.DELETE));
    }

    @Test
    public void listenMessageDto_shouldNotGetAnyMessage() throws Exception {
        try (Producer<String, String> producer = createTestProducer()) {
            producer.send(new ProducerRecord<>("something_else", messageJson));
            producer.flush();
        }

        verify(notificationService, never())
                .sendMessage(any(MessageDto.class), any(MessageType.class));
    }

    private Producer<String, String> createTestProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                embeddedKafkaBroker.getBrokersAsString());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        return new KafkaProducer<>(props);
    }

}
