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

<img width="667" height="436" alt="Image" src="https://github.com/user-attachments/assets/4d08bed3-a2fd-49d1-b4b4-fbaed4f0dd93" />

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

<img width="1358" height="630" alt="Image" src="https://github.com/user-attachments/assets/ef2031c2-84b9-4d91-888b-66e7ac2bb695" />

Registro

<img width="1347" height="637" alt="Image" src="https://github.com/user-attachments/assets/1ff2081b-1754-4d09-a49f-c33a1a115002" />

Pantalla principal

<img width="1353" height="636" alt="Image" src="https://github.com/user-attachments/assets/f97f7952-140f-4607-8673-a71404b2914c" />

Lista de productos extraídos

<img width="1357" height="652" alt="Image" src="https://github.com/user-attachments/assets/e590fed9-8ec9-4c5b-a1ff-f23d12663693" />

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
