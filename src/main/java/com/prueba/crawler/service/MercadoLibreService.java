package com.prueba.crawler.service;

import com.prueba.crawler.model.Categoria;
import com.prueba.crawler.model.Producto;
import com.prueba.crawler.repository.CategoriaRepository;
import com.prueba.crawler.repository.ProductoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoLibreService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Producto extraerProductos(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        // SKU
        String sku = doc.select("meta[name=twitter:app:url:googleplay]").attr("content");
        if (sku.contains("id=")) {
            sku = sku.substring(sku.indexOf("id=") + 3);
        }

        // Nombre
        String nombre = doc.select("h1.ui-pdp-title").text();

        // Precio actual
        String precioActualTexto = doc.select("span.andes-money-amount__fraction").first() != null
                ? doc.select("span.andes-money-amount__fraction").first().text()
                : "0";
        Double precioActual = parsePrecio(precioActualTexto);

        // Precio anterior (si existe)
        String precioAnteriorTexto = doc.select("span.andes-money-amount__fraction.andes-money-amount__fraction--previous").text();
        Double precioAnterior = parsePrecio(precioAnteriorTexto);

        // Imágenes
        List<String> imagenes = new ArrayList<>();
        for (Element img : doc.select("img.ui-pdp-image.ui-pdp-gallery__figure__image")) {
            imagenes.add(img.attr("src"));
        }

        // Disponibilidad
        String disponibilidad = doc.select("p.ui-pdp-stock-information__title").text();
        if (disponibilidad.isEmpty()) {
            disponibilidad = "Disponible";
        }

        // Categoría
        String categoriaNombre = doc.select("a.ui-pdp-breadcrumb__link").last() != null
                ? doc.select("a.ui-pdp-breadcrumb__link").last().text()
                : "Sin categoría";

        Categoria categoria = categoriaRepository.findByNombre(categoriaNombre)
                .orElseGet(() -> {
                    Categoria nueva = new Categoria(categoriaNombre, "https://www.mercadolibre.com.ar/", 1, 1);
                    return categoriaRepository.save(nueva);
                });

        // Crear el producto
        Producto producto = new Producto(sku, nombre, precioActual, precioAnterior, disponibilidad, imagenes, categoria);

        // Guardar en base de datos
        if(productoRepository.existsBySku(sku)) {
            return producto;
        }else {
            return productoRepository.save(producto);
        }
    }

    private Double parsePrecio(String texto) {
        try {
            texto = texto.replace(".", "").replace(",", ".").replaceAll("[^\\d.]", "");
            return texto.isEmpty() ? 0.0 : Double.parseDouble(texto);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
