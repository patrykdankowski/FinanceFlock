package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserFactoryAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserFactoryAdapter userFactoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserFromVO_shouldCreateUserWithEncodedPassword_andDefaultRole() {
        String plainPassword = "plainPassword";
        String encodedPassword = "encodedPassword";

        UserRegisterVO userRegisterVO = new UserRegisterVO("John Doe", "johndoe@example.com", plainPassword);

        when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);

        UserDomainEntity user = userFactoryAdapter.createUserFromVO(userRegisterVO);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.isShareData()).isTrue();
        assertThat(user.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());

        verify(passwordEncoder, times(1)).encode(plainPassword);
    }

    @Test
    void createUserFromVO_shouldSetDefaultValues() {
        String plainPassword = "plainPassword";
        String encodedPassword = "encodedPassword";

        UserRegisterVO userRegisterVO = new UserRegisterVO("Jane Smith", "janesmith@example.com", plainPassword);

        when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);

        UserDomainEntity user = userFactoryAdapter.createUserFromVO(userRegisterVO);

        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.isShareData()).isTrue();
    }
    @Test
    void createUserFromVO_shouldThrowException_whenPasswordEncoderFails() {
        String plainPassword = "plainPassword";
        UserRegisterVO userRegisterVO = new UserRegisterVO("John Doe", "johndoe@example.com", plainPassword);

        when(passwordEncoder.encode(plainPassword)).thenThrow(new RuntimeException("Encoding error"));

        assertThatThrownBy(() -> userFactoryAdapter.createUserFromVO(userRegisterVO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Encoding error");
    }
}
