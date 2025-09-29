package com.notificationservice.service;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceImplTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final NotificationService notificationService = new NotificationServiceImpl(mailSender);
    private final MessageDto testMessageDto = new MessageDto("test@mail.ru");

    @Test
    public void sendMessage_successCreationMessageSending() {
        ResponseCode result = notificationService.sendMessage(testMessageDto, MessageType.CREATE);

        assertEquals(ResponseCode.OK, result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void sendMessage_successDeletionMessageSending() {
        ResponseCode result = notificationService.sendMessage(testMessageDto, MessageType.DELETE);

        assertEquals(ResponseCode.OK, result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void sendMessage_failureMessageSending() {
        doThrow(new MailSendException("SMTP exception"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        ResponseCode result = notificationService.sendMessage(testMessageDto, MessageType.CREATE);

        assertEquals(ResponseCode.ERROR, result);
    }

}
