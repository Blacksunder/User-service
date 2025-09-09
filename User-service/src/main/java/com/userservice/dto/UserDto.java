package com.userservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private String name;
    private String email;
    private int age;

    @Override
    public String toString() {
        return "Name: " + name + ", email: " + email +
                ", age: " + age;
    }
}
