package be.parcial.security;

import be.parcial.entities.UserEntity;
import be.parcial.exceptions.ResourceNotFoundException;
import be.parcial.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@email.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setName("Test User");
        userEntity.setRole(UserEntity.Role.USER);
    }

    @Test
    @DisplayName("should load user by email")
    void loadUserByUsername_existingEmail_returnsUserDetails() {
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(userEntity));

        UserDetails result = userDetailsService.loadUserByUsername("test@email.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("test@email.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("should throw exception when user not found")
    void loadUserByUsername_nonExistingEmail_throwsException() {
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("notfound@email.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with email: notfound@email.com");
    }

    @Test
    @DisplayName("should load admin user with correct role")
    void loadUserByUsername_adminEmail_returnsAdminRole() {
        userEntity.setRole(UserEntity.Role.ADMIN);
        when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(userEntity));

        UserDetails result = userDetailsService.loadUserByUsername("admin@email.com");

        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }
}
