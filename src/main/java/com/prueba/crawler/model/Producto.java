package com.prueba.crawler.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String nombre;
    private Double precioActual;
    private Double precioAnterior;
    private String disponibilidad;

    @ElementCollection
    @CollectionTable(name = "producto_imagenes",joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url")
    private List<String> imagenes;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto(){}

    public Producto(String sku,String nombre, Double precioActual, Double precioAnterior, String disponibilidad, List<String> imagenes, Categoria categoria){
        this.sku = sku;
        this.nombre = nombre;
        this.precioActual = precioActual;
        this.precioAnterior = precioAnterior;
        this.disponibilidad = disponibilidad;
        this.imagenes = imagenes;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {this.id = id;}

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {this.sku = sku;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Double getPrecioActual() {return precioActual;}
    public void setPrecioActual(Double precioActual) {this.precioActual = precioActual;}

    public Double getPrecioAnterior() {return precioAnterior;}
    public void setPrecioAnterior(Double precioAnterior) {this.precioAnterior = precioAnterior;}

    public String getDisponibilidad() {return disponibilidad;}
    public void setDisponibilidad(String disponibilidad) {this.disponibilidad = disponibilidad;}

    public List<String> getImagenes() {return imagenes;}
    public void setImagenes(List<String> imagenes) {this.imagenes = imagenes;}

    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

}
