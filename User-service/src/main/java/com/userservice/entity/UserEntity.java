package com.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String uuid;

    private String name;
    private String email;
    private int age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserEntity(String name, String email, int age) {
        uuid = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.age = age;
        createdAt = LocalDateTime.now();
    }

}
