package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.entity.UserEntity;
import com.userservice.enums.ResponseCode;
import com.userservice.hateaos.HateaosBuilder;
import com.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
        UserDataController.class,
        HateaosBuilder.class
})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class UserDataControllerTest {

    private static final LocalDateTime TIME = LocalDateTime.now();
    private final List<UserEntity> testEntities = List.of(
            new UserEntity("1", "1", "1", 1, TIME),
            new UserEntity("2", "2", "2", 2, TIME),
            new UserEntity("3", "3", "3", 3, TIME)
    );
    private final List<UserDto> testDtos = List.of(
            new UserDto("1", "1", 1),
            new UserDto("2", "2", 2),
            new UserDto("3", "3", 3)
    );
    private final String json = new ObjectMapper().writeValueAsString(testDtos.get(0));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    public UserDataControllerTest() throws JsonProcessingException {
    }

    @Test
    public void getAll_shouldReturnAllUsersIds() throws Exception {
        Mockito.when(userService.getAllUsersId()).thenReturn(testEntities.stream()
                .map(UserEntity::getUuid)
                .toList());

        mockMvc.perform(get("/user-service/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.*.length()").value(3));
    }

    @Test
    public void getById_shouldReturnUserById() throws Exception {
        Mockito.when(userService.getUserById(testEntities.get(0).getUuid()))
                .thenReturn(testDtos.get(0));
        UserDto expected = testDtos.get(0);

        mockMvc.perform(get("/user-service/user/{uuid}", testEntities.get(0).getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.email").value(expected.getEmail()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("._links.users.href").exists());
    }

    @Test
    public void getById_shouldReturnBadRequest() throws Exception {
        Mockito.when(userService.getUserById(testEntities.get(0).getUuid()))
                .thenReturn(null);

        mockMvc.perform(get("/user-service/user/{uuid}", testEntities.get(0).getUuid()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_shouldSaveUser() throws Exception {
        Mockito.when(userService.saveUser(testDtos.get(0))).thenReturn(ResponseCode.OK);

        mockMvc.perform(post("/user-service/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void save_shouldReturnBadRequest() throws Exception {
        Mockito.when(userService.saveUser(any(UserDto.class))).thenReturn(ResponseCode.ERROR);

        mockMvc.perform(post("/user-service/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_shouldUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(testDtos.get(0), testEntities.get(0).getUuid()))
                .thenReturn(ResponseCode.OK);

        mockMvc.perform(patch("/user-service/update/{uuid}", testEntities.get(0).getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void update_shouldReturnBadRequest() throws Exception {
        Mockito.when(userService.updateUser(any(UserDto.class), any(String.class)))
                .thenReturn(ResponseCode.ERROR);

        mockMvc.perform(patch("/user-service/update/{uuid}", testEntities.get(0).getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_shouldDeleteUser() throws Exception {
        Mockito.when(userService.deleteUser(testEntities.get(0).getUuid())).thenReturn(ResponseCode.OK);

        mockMvc.perform(delete("/user-service/delete/{uuid}", testEntities.get(0).getUuid()))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_shouldReturnBadRequest() throws Exception {
        Mockito.when(userService.deleteUser(testEntities.get(0).getUuid())).thenReturn(ResponseCode.ERROR);

        mockMvc.perform(delete("/user-service/delete/{uuid}", testEntities.get(0).getUuid()))
                .andExpect(status().isBadRequest());
    }
}
