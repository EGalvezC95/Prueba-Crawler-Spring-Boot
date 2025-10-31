# Crawler de Productos

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.2-blue)](https://maven.apache.org/)

---

## Descripción

Esta aplicación permite extraer información de productos desde diferentes sitios web y almacenarlos en una base de datos MySQL. La información extraída incluye:

- Nombre del producto
- SKU
- Precio actual y anterior
- Disponibilidad
- Imágenes del producto
- Categoría

El proyecto está desarrollado con **Spring Boot**, **Thymeleaf**, **Tailwind CSS**, y **JSoup** para el scraping de productos.

---

## Requisitos previos

1. Tener **Java 17** instalado.
2. Tener **Maven** instalado.
3. Tener **MySQL** corriendo (puede ser vía XAMPP o cualquier servidor local).
4. Crear la base de datos antes de iniciar la aplicación.

### Ejemplo XAMPP:

[![XAMPP](docs/images/xampp.png)](docs/images/xampp.png)

---

## Instalación y ejecución


1. Clonar el repositorio:

```bash
git clone https://github.com/tu-usuario/crawler-productos.git
cd crawler-productos
```

2.Configurar la conexión a la base de datos en src/main/resources/application.properties:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/nombre_base
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```

3.Construir y ejecutar la aplicación:

```bash
mvn clean install
mvn spring-boot:run
```
4.Abrir el navegador en:

http://localhost:8080

Capturas de pantalla
Login

[![Login](docs/images/login.png)](docs/images/login.png)

Registro

[![Registro](docs/images/registro.png)](docs/images/registro.png)

Pantalla principal

[![Principal](docs/images/principal.png)](docs/images/principal.png)

Lista de productos extraídos

[![Lista](docs/images/lista.png)](docs/images/lista.png)

Funcionalidades

Registro y login de usuarios.
Extracción de productos desde URLs.
Guardado automático en MySQL.
Seguridad básica con Spring Security.

Tecnologías utilizadas

Java 17
Spring Boot 3
Spring Security
Thymeleaf
JSoup
Tailwind CSS
MySQL
Maven

Autor
Eduardo Gálvez
