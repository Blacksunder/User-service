package com.notificationservice.service;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Value("${spring.mail.username}")
    private String fromAddress;

    private final MailSender mailSender;

    public NotificationServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ResponseCode sendMessage(MessageDto messageDto, MessageType messageType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(messageDto.getDestination());
        if (messageType == MessageType.CREATE) {
            mailMessage.setText("Your account was created");
        } else {
            mailMessage.setText("Your account was deleted");
        }
        mailMessage.setSubject("Account notification");
        try {
            mailSender.send(mailMessage);
            return ResponseCode.OK;
        } catch (MailException e) {
            return ResponseCode.ERROR;
        }
    }

}
