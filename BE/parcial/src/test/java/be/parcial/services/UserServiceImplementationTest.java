package be.parcial.services;

import be.parcial.dtos.RegisterRequestDTO;
import be.parcial.entities.UserEntity;
import be.parcial.exceptions.Messages;
import be.parcial.models.User;
import be.parcial.repositories.UserRepository;
import be.parcial.services.implementations.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImplementation userService;

    private UserEntity userEntity;
    private User userModel;
    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@email.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setName("Test User");
        userEntity.setRole(UserEntity.Role.USER);

        userModel = new User();
        userModel.setEmail("test@email.com");
        userModel.setPassword("password123");
        userModel.setName("Test User");
        userModel.setRole(UserEntity.Role.USER);

        registerRequest = new RegisterRequestDTO("test@email.com", "password123", "Test User");
    }

    @Nested
    @DisplayName("registerUser")
    class RegisterUser {

        @Test
        @DisplayName("should register user successfully")
        void registerUser_validData_returnsUser() {
            when(userRepository.existsByEmail("test@email.com")).thenReturn(false);
            when(modelMapper.map(registerRequest, User.class)).thenReturn(userModel);
            when(modelMapper.map(userModel, UserEntity.class)).thenReturn(userEntity);
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

            UserEntity result = userService.registerUser(registerRequest, UserEntity.Role.USER);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("test@email.com");
        }

        @Test
        @DisplayName("should throw when email already exists")
        void registerUser_existingEmail_throwsException() {
            when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.registerUser(registerRequest, UserEntity.Role.USER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User already exists with email:");
        }
    }

    @Nested
    @DisplayName("findByEmail")
    class FindByEmail {

        @Test
        @DisplayName("should return user when found")
        void findByEmail_existingEmail_returnsUser() {
            when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(userEntity));

            Optional<UserEntity> result = userService.findByEmail("test@email.com");

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("test@email.com");
        }

        @Test
        @DisplayName("should return empty when not found")
        void findByEmail_nonExistingEmail_returnsEmpty() {
            when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

            Optional<UserEntity> result = userService.findByEmail("notfound@email.com");

            assertThat(result).isEmpty();
        }
    }
}
