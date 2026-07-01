package be.parcial.services.implementations;

import be.parcial.dtos.RegisterRequestDTO;
import be.parcial.entities.UserEntity;
import be.parcial.exceptions.Messages;
import be.parcial.models.User;
import be.parcial.repositories.UserRepository;
import be.parcial.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserEntity registerUser(RegisterRequestDTO request, UserEntity.Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    String.format(Messages.USER_ALREADY_EXISTS, request.getEmail()));
        }
        User user = modelMapper.map(request, User.class);
        user.setRole(role);
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(entity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
