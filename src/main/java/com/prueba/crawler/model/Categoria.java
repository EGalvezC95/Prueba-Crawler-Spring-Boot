package com.prueba.crawler.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Nombre de la categoría
    private String url; // URL de la categoría
    private int cantidadPaginas; // Número total de páginas
    private int productosporPaginas; // Número de productos por página

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;

    // Constructores
    public Categoria() {}

    public Categoria(String nombre, String url, int cantidadPaginas, int productosporPaginas) {
        this.nombre = nombre;
        this.url = url;
        this.cantidadPaginas = cantidadPaginas;
        this.productosporPaginas = productosporPaginas;
    }

    // Getters y Setters
    public Long getId(){ return id;}
    public void setId(Long id){ this.id = id;}

    public String getNombre(){ return nombre;}
    public void setNombre(String nombre){ this.nombre = nombre;}

    public String getUrl(){ return url;}
    public void setUrl(String url){ this.url = url;}

    public int getCantidadPaginas(){ return cantidadPaginas;}
    public void setCantidadPaginas(int cantidadPaginas){ this.cantidadPaginas = cantidadPaginas; }

    public int getProductosporPaginas(){ return productosporPaginas;}
    public void setProductosporPaginas(int productosporPaginas){ this.productosporPaginas = productosporPaginas; }

    public List<Producto> getProductos(){ return productos;}
    public void setProductos(List<Producto> productos){ this.productos = productos;}


}
