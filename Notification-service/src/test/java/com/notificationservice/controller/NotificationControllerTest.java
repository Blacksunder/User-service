package com.notificationservice.controller;

import com.notificationservice.dto.MessageDto;
import com.notificationservice.enums.MessageType;
import com.notificationservice.enums.ResponseCode;
import com.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotificationController.class)
public class NotificationControllerTest {

    private final String messageJson = """
        {
            "destination": "test@mail.ru"
        }
        """;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    NotificationService notificationService;

    @Test
    public void sendCreateMessage_successCreation() throws Exception {
        when(notificationService.sendMessage(any(MessageDto.class), eq(MessageType.CREATE)))
                .thenReturn(ResponseCode.OK);

        mockMvc.perform(post("/notification/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(messageJson))
                .andExpect(status().isOk());
    }

    @Test
    public void sendCreateMessage_failureCreation() throws Exception {
        when(notificationService.sendMessage(any(MessageDto.class), eq(MessageType.CREATE)))
                .thenReturn(ResponseCode.ERROR);

        mockMvc.perform(post("/notification/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sendCreateMessage_successDeletion() throws Exception {
        when(notificationService.sendMessage(any(MessageDto.class), eq(MessageType.DELETE)))
                .thenReturn(ResponseCode.OK);

        mockMvc.perform(post("/notification/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isOk());
    }

    @Test
    public void sendCreateMessage_failureDeletion() throws Exception {
        when(notificationService.sendMessage(any(MessageDto.class), eq(MessageType.DELETE)))
                .thenReturn(ResponseCode.ERROR);

        mockMvc.perform(post("/notification/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isBadRequest());
    }
}
