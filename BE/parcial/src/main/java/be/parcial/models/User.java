package be.parcial.models;

import be.parcial.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private UserEntity.Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
