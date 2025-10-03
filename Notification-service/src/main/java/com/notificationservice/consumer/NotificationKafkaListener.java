package com.notificationservice.consumer;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class NotificationKafkaListener {

    private final NotificationService notificationService;

    @org.springframework.kafka.annotation.KafkaListener(topics = "#{'${listenable-topics}'.split(',')}",
    containerFactory = "messageDtoKafkaListenerContainerFactory")
    public void listenMessageDto(MessageDto messageDto,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        MessageType type;
        if (topic.equals("user_creation")) {
            type = MessageType.CREATE;
        } else {
            type = MessageType.DELETE;
        }
        notificationService.sendMessage(messageDto, type);
    }

}
