package com.prueba.crawler.service;

import com.prueba.crawler.model.Categoria;
import com.prueba.crawler.model.Producto;
import com.prueba.crawler.repository.CategoriaRepository;
import com.prueba.crawler.repository.ProductoRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class FalabellaService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ChromeDriver driver;

    public List<Producto> extraerProductos(String urlCategoria) {
        List<Producto> productos = new ArrayList<>();
        driver.get(urlCategoria);

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        String nombreCategoria = driver.getTitle();

        Categoria categoria = categoriaRepository.findByUrl(urlCategoria);
        if (categoria == null) {
            categoria = new Categoria();
            categoria.setNombre(nombreCategoria);
            categoria.setUrl(urlCategoria);
            categoria.setCantidadPaginas(0);
            categoria.setProductosporPaginas(0);
            categoria = categoriaRepository.save(categoria);
        }

        int contadorPaginas = 0;
        boolean hayMasPaginas = true;

        while (hayMasPaginas) {
            contadorPaginas++;
            System.out.println("📄 Extrayendo página " + contadorPaginas);

            // Seleccionamos los contenedores de producto reales
            List<WebElement> productosHtml = driver.findElements(By.cssSelector("a[data-pod]"));
            int productosEnEstaPagina = 0;

            for (WebElement prodElem : productosHtml) {
                try {
                    String sku = prodElem.getAttribute("id");
                    if (sku == null || sku.isEmpty()) continue;

                    String nombre = "";
                    try {
                        nombre = prodElem.findElement(By.cssSelector("b.pod-subTitle")).getText();
                    } catch (NoSuchElementException ignored) {}

                    // Precios
                    Double precioActual = 0.0;
                    Double precioAnterior = null;
                    try {
                        String precioActualStr = prodElem.findElement(By.cssSelector("li.prices-0 span")).getText()
                                .replaceAll("[^\\d.]", "");
                        precioActual = precioActualStr.isEmpty() ? 0.0 : Double.parseDouble(precioActualStr);
                    } catch (NoSuchElementException ignored) {}

                    try {
                        String precioAnteriorStr = prodElem.findElement(By.cssSelector("li.prices-2 span")).getText()
                                .replaceAll("[^\\d.]", "");
                        if (!precioAnteriorStr.isEmpty()) precioAnterior = Double.parseDouble(precioAnteriorStr);
                    } catch (NoSuchElementException ignored) {}

                    // Imágenes
                    List<String> imagenes = new ArrayList<>();
                    try {
                        List<WebElement> imgs = prodElem.findElements(By.cssSelector("picture img"));
                        for (WebElement img : imgs) {
                            String src = img.getAttribute("src");
                            if (src != null && !src.isEmpty() && !imagenes.contains(src)) {
                                imagenes.add(src);
                            }
                        }
                    } catch (Exception ignored) {}

                    String disponibilidad = "Disponible";

                    if (!productoRepository.existsBySku(sku)) {
                        Producto producto = new Producto(sku, nombre, precioActual, precioAnterior, disponibilidad, imagenes, categoria);
                        productoRepository.save(producto);
                        productos.add(producto);
                        productosEnEstaPagina++;

                    }

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

            // Intentar ir a la siguiente página
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement siguiente = wait.until(
                        ExpectedConditions.elementToBeClickable(By.id("testId-pagination-bottom-arrow-right"))
                );

                // Asegurarnos de que esté visible en pantalla
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", siguiente);

                // Hacer clic
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", siguiente);

                // Esperar a que los productos se carguen en la nueva página
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector("div[pod-layout='2_GRID']") // selector de los productos
                ));
            } catch (TimeoutException | NoSuchElementException e) {
                hayMasPaginas = false;

            }

            categoria.setProductosporPaginas(productosEnEstaPagina);
        }

        categoria.setCantidadPaginas(contadorPaginas);
        categoriaRepository.save(categoria);

        return productos;
    }
}
