package com.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "User entity")
public class UserDto {

    @NotNull
    @Schema(description = "User's name", example = "Freddy")
    private String name;

    @NotNull
    @Schema(description = "User's email", example = "example@gmail.com")
    private String email;

    @Schema(description = "User's age", example = "20")
    @Min(0)
    @Max(99)
    private int age;

}
