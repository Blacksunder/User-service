package com.userservice.mapper;

import com.userservice.dto.MessageDto;
import com.userservice.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageDto userEntityToMessageDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new MessageDto(userEntity.getEmail());
    }

}
