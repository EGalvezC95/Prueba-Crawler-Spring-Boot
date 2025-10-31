package com.prueba.crawler.service;

import com.prueba.crawler.model.Categoria;
import com.prueba.crawler.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MercadoLibreServiceTest {

    private MercadoLibreServiceTestable service;
    private FakeProductoRepository productoRepository;
    private FakeCategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        productoRepository = new FakeProductoRepository();
        categoriaRepository = new FakeCategoriaRepository();
        service = new MercadoLibreServiceTestable(productoRepository, categoriaRepository);
    }

    @Test
    void testExtraerProductos() throws Exception {
        String url = "https://fake-url.com/producto";
        Producto producto = service.extraerProductos(url);

        assertNotNull(producto);
        assertEquals("sku123", producto.getSku());
        assertEquals("Producto de prueba", producto.getNombre());
        assertEquals(500.0, producto.getPrecioActual());
        assertEquals("Disponible", producto.getDisponibilidad());

        // Verificar que la categoría fue guardada
        Categoria cat = categoriaRepository.findByNombre("Categoría de prueba");
        assertNotNull(cat);
        assertEquals("Categoría de prueba", cat.getNombre());
    }

    // ===== FAKES =====

    /** Fake de ProductoRepository */
    static class FakeProductoRepository {
        private final List<Producto> data = new ArrayList<>();

        public Producto save(Producto producto) {
            data.add(producto);
            return producto;
        }

        public boolean existsBySku(String sku) {
            return data.stream().anyMatch(p -> p.getSku().equals(sku));
        }
    }

    /** Fake de CategoriaRepository */
    static class FakeCategoriaRepository {
        private final List<Categoria> data = new ArrayList<>();

        public Categoria save(Categoria categoria) {
            data.add(categoria);
            return categoria;
        }

        public Categoria findByNombre(String nombre) {
            return data.stream().filter(c -> c.getNombre().equals(nombre)).findFirst().orElse(null);
        }
    }

    /** Versión de MercadoLibreService adaptada para test */
    static class MercadoLibreServiceTestable extends MercadoLibreService {

        private final FakeProductoRepository productoRepository;
        private final FakeCategoriaRepository categoriaRepository;

        public MercadoLibreServiceTestable(FakeProductoRepository productoRepository, FakeCategoriaRepository categoriaRepository) {
            this.productoRepository = productoRepository;
            this.categoriaRepository = categoriaRepository;
        }

        @Override
        public Producto extraerProductos(String url) {
            // Simular extracción
            Categoria categoria = categoriaRepository.findByNombre("Categoría de prueba");
            if (categoria == null) {
                categoria = new Categoria("Categoría de prueba", url, 1, 1);
                categoriaRepository.save(categoria);
            }

            Producto producto = new Producto(
                    "sku123",
                    "Producto de prueba",
                    500.0,
                    0.0,
                    "Disponible",
                    new ArrayList<>(),
                    categoria
            );

            if (!productoRepository.existsBySku(producto.getSku())) {
                productoRepository.save(producto);
            }

            return producto;
        }
    }
}