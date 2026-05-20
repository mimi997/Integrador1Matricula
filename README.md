# Sistema de Control Académico y Matrícula Universitaria (UTP)

## Integrador 1 - Grupo 6

Este proyecto es una aplicación web de matrícula universitaria construida con Spring Boot.

## Tecnologías
- Java 17 LTS
- Spring Boot 3.x
- Spring MVC + Thymeleaf
- Spring Data JPA (Hibernate)
- Spring Security + BCrypt
- MySQL 8
- Tomcat embebido
- Maven 3.9
- Bootstrap 5.3

## Requisitos Previos
- JDK 17
- MySQL 8
- IDE (IntelliJ IDEA recomendado)

## Cómo ejecutar localmente
1. Clonar el repositorio.
2. Configurar la base de datos `matricula_utp` en MySQL.
3. Asegurarse de que el usuario `root` con contraseña `password` exista (o cambiar los valores en `application-local.yml`).
4. Ejecutar el proyecto con Maven y el perfil `local`:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
5. Acceder a `http://localhost:8080`.
