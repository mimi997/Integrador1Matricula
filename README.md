# Sistema de Matrícula Universitaria (UTP)

Este es un proyecto de aplicación web para simular el proceso de matrícula de estudiantes en la UTP.

## Tecnologías
* Java 17
* Spring Boot
* MySQL
* Thymeleaf y Bootstrap

## Pasos para ejecutar
1. Crea la base de datos `matricula_utp` en tu MySQL local.
2. Configura las credenciales de tu base de datos en el archivo `src/main/resources/application-local.yml` (usa de plantilla el archivo `.example`).
3. Ejecuta el proyecto desde la terminal:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
4. Ingresa en tu navegador a: `http://localhost:8080`

## Cuentas de prueba (Contraseña para todos: `123456`)
* **Administrador:** `admin@utp.edu.pe`
* **Docente:** `docente@utp.edu.pe`
* **Estudiante:** `estudiante1@utp.edu.pe`
