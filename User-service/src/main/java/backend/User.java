package backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    private String uuid;
    private String name;
    private String email;
    private int age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {}

    public User(String name, String email, int age) {
        uuid = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.age = age;
        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Id: " + uuid + ", name: " + name + ", email: " + email +
                ", age: " + age + ", created at: " + createdAt;
    }
}
