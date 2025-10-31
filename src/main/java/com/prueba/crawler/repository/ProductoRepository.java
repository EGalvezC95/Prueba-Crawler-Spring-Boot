package com.prueba.crawler.repository;

import com.prueba.crawler.model.Producto;
import com.prueba.crawler.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    List<Producto> findByCategoria(Categoria categoria);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    boolean existsBySku(String sku);

    boolean existsByNombre(String nombre);
}
