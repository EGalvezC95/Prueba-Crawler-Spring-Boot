package com.prueba.crawler.service;

import com.prueba.crawler.model.Usuario;
import com.prueba.crawler.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test") // Usa el perfil con H2
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanup() {
        // Solo elimina los usuarios creados en las pruebas
        usuarioRepository.deleteAll(
                usuarioRepository.findAll()
                        .stream()
                        .filter(u -> u.getUsername().startsWith("testuser"))
                        .toList()
        );
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        String username = "testuser1";
        String password = "password123";

        Usuario savedUser = usuarioService.registerNewUser(username, password);

        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals(username, savedUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(password, savedUser.getPassword()));
    }

    @Test
    void shouldFindUserByUsername() {
        String username = "testuser2";
        String password = "secret";

        usuarioService.registerNewUser(username, password);
        Optional<Usuario> found = usuarioRepository.findByUsername(username);

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(username, found.get().getUsername());
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        String username = "testuser3";
        String password = "abc123";

        usuarioService.registerNewUser(username, password);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            usuarioService.registerNewUser(username, password);
        });

        Assertions.assertTrue(exception.getMessage().toLowerCase().contains("ya existe"));
    }
}