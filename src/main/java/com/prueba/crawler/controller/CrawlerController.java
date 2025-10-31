package com.prueba.crawler.controller;


import com.prueba.crawler.model.Producto;
import com.prueba.crawler.service.FalabellaService;
import com.prueba.crawler.service.MercadoLibreService;
import com.prueba.crawler.service.ParisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlerController {

    @Autowired
    private MercadoLibreService mercadoLibreService;

    @Autowired
    private ParisService parisService;

    @Autowired
    private FalabellaService falabellaService;

    @GetMapping("/")
    public String index() {
        return "index"; // Nuestra vista HTML
    }

    @PostMapping("/extraer")
    public String extraerProductos(@RequestParam("url") String url, Model model) throws IOException, InterruptedException {
        List<Producto> productos = new ArrayList<>();

        if (url.contains("mercadolibre.com")) {
            Producto p = mercadoLibreService.extraerProductos(url); // devuelve un solo producto
            productos.add(p);
        } else if (url.contains("paris.cl")) {
            productos = parisService.extraerProductos(url);
        } else if (url.contains("falabella.com") || url.contains("falabella-cl")) {
            productos = falabellaService.extraerProductos(url);
        } else {
            model.addAttribute("error", "URL no soportada");
            return "index";
        }

        model.addAttribute("productos", productos);
        return "resultado";
    }
}
