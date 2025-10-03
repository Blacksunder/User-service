package com.userservice.hateaos;

import com.userservice.controller.UserDataController;
import com.userservice.dto.UserDto;
import com.userservice.dto.UserIdDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateaosBuilder {

    public EntityModel<UserDto> userDtoEntityModel(String uuid, UserDto userDto) {
        EntityModel<UserDto> entityModel = EntityModel.of(userDto);
        entityModel.add(linkTo(methodOn(UserDataController.class).getById(uuid)).withSelfRel());
        entityModel.add(linkTo(methodOn(UserDataController.class).getAll()).withRel("users"));
        return entityModel;
    }

    public CollectionModel<EntityModel<UserIdDto>> userIdDtoCollectionModel(List<String> usersIds) {
        List<EntityModel<UserIdDto>> entityModels = usersIds.stream()
                .map(uuid -> {
                    EntityModel<UserIdDto> model = EntityModel.of(new UserIdDto(uuid));
                    model.add(linkTo(methodOn(UserDataController.class).getById(uuid)).withSelfRel());
                    return model;
                })
                .toList();
        return CollectionModel.of(entityModels, linkTo(methodOn(UserDataController.class).getAll()).withSelfRel());
    }

}
