package be.parcial.services;

import be.parcial.dtos.RegisterRequestDTO;
import be.parcial.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity registerUser(RegisterRequestDTO request, UserEntity.Role role);
    Optional<UserEntity> findByEmail(String email);
}
