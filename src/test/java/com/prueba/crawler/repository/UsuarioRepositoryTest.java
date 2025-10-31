package com.prueba.crawler.repository;

import com.prueba.crawler.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRepositoryTest {

    private FakeUsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = new FakeUsuarioRepository();
    }

    @Test
    void testGuardarYBuscarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("juan123");

        usuarioRepository.save(usuario);

        // Buscar por username
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("juan123");
        assertTrue(encontrado.isPresent());
        assertEquals("juan123", encontrado.get().getUsername());

        // Verificar existencia
        assertTrue(usuarioRepository.existsByUsername("juan123"));
        assertFalse(usuarioRepository.existsByUsername("noexiste"));
    }

    // ===== Fake UsuarioRepository =====
    static class FakeUsuarioRepository implements UsuarioRepository {
        private final List<Usuario> data = new ArrayList<>();

        @Override
        public Optional<Usuario> findByUsername(String username) {
            return data.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        }

        @Override
        public boolean existsByUsername(String username) {
            return data.stream().anyMatch(u -> u.getUsername().equals(username));
        }

        @Override
        public <S extends Usuario> S save(S entity) {
            data.add(entity);
            return entity;
        }

        // Métodos de JpaRepository no usados en el test
        @Override public Optional<Usuario> findById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public boolean existsById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public List<Usuario> findAll() { throw new UnsupportedOperationException(); }
        @Override public long count() { throw new UnsupportedOperationException(); }
        @Override public void deleteById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public void delete(Usuario entity) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllById(Iterable<? extends Long> longs) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll(Iterable<? extends Usuario> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll() { throw new UnsupportedOperationException(); }
        @Override public <S extends Usuario> List<S> saveAll(Iterable<S> entities) { throw new UnsupportedOperationException(); }
        @Override public void flush() { throw new UnsupportedOperationException(); }
        @Override public <S extends Usuario> S saveAndFlush(S entity) { throw new UnsupportedOperationException(); }
        @Override public void deleteInBatch(Iterable<Usuario> entities) { throw new UnsupportedOperationException(); }

        @Override
        public void deleteAllInBatch(Iterable<Usuario> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override public void deleteAllInBatch() { throw new UnsupportedOperationException(); }
        @Override public Usuario getOne(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public Usuario getById(Long aLong) { throw new UnsupportedOperationException(); }

        @Override
        public Usuario getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Usuario> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Usuario> List<S> findAll(Example<S> example) {
            return List.of();
        }

        @Override
        public <S extends Usuario> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
        }

        @Override
        public <S extends Usuario> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends Usuario> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Usuario> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Usuario, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override public List<Usuario> findAllById(Iterable<Long> longs) { throw new UnsupportedOperationException(); }
        @Override public List<Usuario> findAll(org.springframework.data.domain.Sort sort) { throw new UnsupportedOperationException(); }

        @Override
        public Page<Usuario> findAll(Pageable pageable) {
            return null;
        }

        @Override public <S extends Usuario> List<S> saveAllAndFlush(Iterable<S> entities) { throw new UnsupportedOperationException(); }
    }
}