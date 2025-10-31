package com.prueba.crawler.service;

import com.prueba.crawler.model.Categoria;
import com.prueba.crawler.model.Producto;
import com.prueba.crawler.repository.CategoriaRepository;
import com.prueba.crawler.repository.ProductoRepository;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ParisService {

    @Setter
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ChromeDriver driver;

    public void setCategoryRepository(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public void setDriver(WebDriver driver) {
        this.driver = (ChromeDriver) driver;
    }

    /**
     * Extrae productos desde una URL de categoría de Paris.cl y los guarda en la base de datos.
     * @param urlCategoria URL de la categoría a scrapear
     * @return Lista de productos nuevos extraídos
     */
    public List<Producto> extraerProductos(String urlCategoria) throws InterruptedException {
        List<Producto> productos = new ArrayList<>();


        driver.get(urlCategoria);

        // Extraer nombre de la categoría desde el título de la página
        String nombreCategoria = driver.getTitle();


        // Espera para que cargue JS
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='gridcell']")));

        Integer cantidadPagina=0;

        while (true) {
            List<WebElement> botones = driver.findElements(By.cssSelector("button[data-testid='paris-button']"));
            WebElement mostrarMas = null;

            for (WebElement boton : botones) {
                try {
                    WebElement divInterno = boton.findElement(By.cssSelector("div > div"));
                    if (divInterno.getText().contains("Mostrar más")) {
                        mostrarMas = boton;
                        break;
                    }
                } catch (NoSuchElementException e) {}
            }

            if (mostrarMas != null) {
                mostrarMas.click();
                cantidadPagina++;
                Thread.sleep(2000); // espera que carguen más productos
            } else {
                break; // ya no hay más botón
            }
        }

        // Extraer productos de la página
        List<WebElement> productosHtml = driver.findElements(By.cssSelector("[role='gridcell']"));

        // Buscar o crear categoría
        Categoria categoria = categoriaRepository.findByUrl(urlCategoria);
        if (categoria == null) {
            categoria = new Categoria();
            categoria.setNombre(nombreCategoria);
            categoria.setUrl(urlCategoria);
            categoria.setCantidadPaginas(cantidadPagina);
            categoria.setProductosporPaginas(productosHtml.size());
            categoria = categoriaRepository.save(categoria);
        }

        for (WebElement prodElem : productosHtml) {
            try {
                WebElement nombreDiv = prodElem.findElement(By.cssSelector("div.ui-flex-col.ui-gap-1.desktop\\:ui-gap-0"));
                String nombre = nombreDiv.findElements(By.tagName("span")).get(1).getText();

                String precioActualStr = "";
                String precioAnteriorStr = "";
                List<WebElement> precios = prodElem.findElements(By.cssSelector("div[data-testid='paris-pod-price']"));

                for (WebElement precioCont : precios) {
                    List<WebElement> spanLineThrough = precioCont.findElements(By.cssSelector("span.ui-line-through"));
                    if (!spanLineThrough.isEmpty()) {
                        precioAnteriorStr = spanLineThrough.get(0).getText().replaceAll("[^\\d.]", "");
                    } else {
                        List<WebElement> spans = precioCont.findElements(By.cssSelector("span"));
                        if (!spans.isEmpty()) {
                            precioActualStr = spans.get(0).getText().replaceAll("[^\\d.]", "");
                        }
                    }
                }

                Double precioActual = precioActualStr.isEmpty() ? 0.0 : Double.parseDouble(precioActualStr);
                Double precioAnterior = precioAnteriorStr.isEmpty() ? null : Double.parseDouble(precioAnteriorStr);

                String sku = prodElem.getAttribute("data-cnstrc-item-id");
                String disponibilidad = prodElem.findElements(By.cssSelector(".out-of-stock-label")).isEmpty() ? "Disponible" : "Agotado";

                List<String> imagenes = new ArrayList<>();
                List<WebElement> imgs = prodElem.findElements(By.cssSelector("img.ui-object-contain"));
                for (WebElement img : imgs) {
                    String src = img.getAttribute("data-src");
                    if (src == null || src.isEmpty()) {
                        src = img.getAttribute("src");
                    }
                    imagenes.add(src);
                }

                if (!productoRepository.existsBySku(sku)) {
                    Producto producto = new Producto(sku, nombre, precioActual, precioAnterior, disponibilidad, imagenes, categoria);
                    if (!productoRepository.existsByNombre(producto.getNombre())) {
                        productoRepository.save(producto);
                        productos.add(producto);
                    }
                }
            } catch (Exception e) {
                // Log para productos que fallaron y continuar con los demás
                System.err.println("Error extrayendo producto: " + e.getMessage());
            }
        }

        return productos;
    }
}
