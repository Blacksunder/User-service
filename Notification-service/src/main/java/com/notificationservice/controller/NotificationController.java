package com.notificationservice.controller;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;
import com.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<?> sendCreateMessage(@RequestBody MessageDto messageDto) {
        ResponseCode code = notificationService.sendMessage(messageDto, MessageType.CREATE);
        return handleServiceResponse(code);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> sendDeleteMessage(@RequestBody MessageDto messageDto) {
        ResponseCode code = notificationService.sendMessage(messageDto, MessageType.DELETE);
        return handleServiceResponse(code);
    }

    private ResponseEntity<?> handleServiceResponse(ResponseCode code) {
        if (code == ResponseCode.OK) {
            return  ResponseEntity.ok().build();
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Sending error");
    }

}
