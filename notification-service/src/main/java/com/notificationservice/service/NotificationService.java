package com.notificationservice.service;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;

public interface NotificationService {

    ResponseCode sendMessage(MessageDto messageDto, MessageType messageType);

}
