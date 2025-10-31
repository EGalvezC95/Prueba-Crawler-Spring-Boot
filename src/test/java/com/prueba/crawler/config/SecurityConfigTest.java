package com.prueba.crawler.config;

import com.prueba.crawler.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void contextLoadsAndBeansCreated() {
        assertThat(securityConfig).isNotNull();
        assertThat(usuarioService).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(authenticationManager).isNotNull();
    }
}