package be.parcial.services;

import be.parcial.entities.RefreshTokenEntity;
import be.parcial.entities.UserEntity;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(UserEntity user);
    RefreshTokenEntity validateRefreshToken(String token);
    void revokeRefreshToken(String token);
}
