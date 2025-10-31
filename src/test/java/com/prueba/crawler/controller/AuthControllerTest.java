package com.prueba.crawler.controller;

import com.prueba.crawler.model.Usuario;
import com.prueba.crawler.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanup() {
        usuarioRepository.findAll().stream()
                .filter(u -> u.getUsername().startsWith("test_"))
                .forEach(u -> usuarioRepository.deleteById(u.getId()));
    }

    @Test
    void shouldShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "test_nuevoUsuario")
                        .param("password", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        Usuario user = usuarioRepository.findByUsername("test_nuevoUsuario").orElse(null);
        assertThat(user).isNotNull();
        assertThat(passwordEncoder.matches("12345", user.getPassword())).isTrue();
    }

    @Test
    void shouldHandleDuplicateUser() throws Exception {
        Usuario existing = new Usuario();
        existing.setUsername("test_duplicado");
        existing.setPassword(passwordEncoder.encode("12345"));
        usuarioRepository.save(existing);

        mockMvc.perform(post("/register")
                        .param("username", "test_duplicado")
                        .param("password", "99999"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("errorMsg"));
    }
}