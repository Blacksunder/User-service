package com.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InputMode {
    GET_ALL(1),
    GET_USER(2),
    UPDATE(3),
    SAVE(4),
    DELETE(5),
    EXIT(0),
    OTHER(-1);

    final int menuCode;
}
