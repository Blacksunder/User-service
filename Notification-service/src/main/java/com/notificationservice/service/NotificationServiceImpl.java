package com.notificationservice.service;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public ResponseCode sendMessage(MessageDto messageDto, MessageType messageType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(messageDto.getDestination());
        if (messageType == MessageType.CREATE) {
            mailMessage.setText("Your account was created");
        } else {
            mailMessage.setText("Your account was deleted");
        }

        try {
            mailSender.send(mailMessage);
            return ResponseCode.OK;
        } catch (MailException e) {
            return ResponseCode.ERROR;
        }
    }

}
