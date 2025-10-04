package com.userservice.mapper;

import com.userservice.dto.MessageDto;
import com.userservice.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;

public class MessageMapperTest {

    private final String testMail = "gmail.com";
    private final UserEntity testEntity = new UserEntity("aaa", testMail, 15);
    private final MessageMapper mapper = new MessageMapper();

    @Test
    public void userEntityToMessageDto_correctDto() {
        MessageDto expected = new MessageDto(testMail);

        MessageDto actual = mapper.userEntityToMessageDto(testEntity);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void userEntityMessageDto_nullMapping() {
        MessageDto actual = mapper.userEntityToMessageDto(null);

        Assert.assertNull(actual);
    }

}
