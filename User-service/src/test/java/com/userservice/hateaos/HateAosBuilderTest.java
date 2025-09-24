package com.userservice.hateaos;


import com.userservice.dto.UserDto;
import com.userservice.dto.UserIdDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HateAosBuilderTest {

    private final HateaosBuilder hateaosBuilder = new HateaosBuilder();

    @Test
    public void userDtoEntityModel_checkForCorrectBuilding() {
        String uuid = "12345";
        UserDto userDto = new UserDto("a", "b", 5);

        EntityModel<UserDto> actual = hateaosBuilder.userDtoEntityModel(uuid, userDto);

        assertTrue(actual.hasLink("users"));
        assertTrue(actual.hasLink(IanaLinkRelations.SELF));
    }

    @Test
    public void userIdDtoCollectionModel_checkForCorrectBuild() {
        List<String> ids = List.of("1", "2", "3");

        CollectionModel<EntityModel<UserIdDto>> actual = hateaosBuilder.userIdDtoCollectionModel(ids);
        long linkedEntities = actual.getContent().stream()
                        .filter(entity -> entity.hasLink(IanaLinkRelations.SELF))
                                .count();

        assertTrue(actual.hasLink(IanaLinkRelations.SELF));
        assertEquals(linkedEntities, ids.size());
    }

}
