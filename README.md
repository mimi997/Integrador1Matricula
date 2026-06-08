# Sistema de Control Académico y Matrícula Universitaria (UTP)

![Build Status](https://github.com/usuario/sistema-matricula-utp/actions/workflows/ci.yml/badge.svg)
![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)

## Integrador 1 - Grupo 6

Este proyecto es una aplicación web integral para la gestión de matrículas universitarias, construida sobre el ecosistema Spring Boot. Aplica rigurosamente la arquitectura MVC, principios SOLID y Clean Code para ofrecer una solución escalable y mantenible.

### Características Principales
*   **Gestión de Usuarios y Roles:** Autenticación segura usando Spring Security con BCrypt. Roles diferenciados: `ADMIN`, `DOCENTE`, y `ESTUDIANTE`.
*   **Motor de Matrícula:** Algoritmo transaccional que previene el cruce de horarios, valida prerrequisitos y controla estrictamente los cupos disponibles y topes de créditos (ACID compliant).
*   **Historial y Calificaciones:** Ingreso de notas por parte de docentes y visualización de récord académico para estudiantes.
*   **Exportación Analítica:** Reportes operacionales en tiempo real generados en Microsoft Excel usando Apache POI.
*   **Interfaz Gráfica Responsiva:** Interfaces desarrolladas con Thymeleaf y Bootstrap 5, respetando la línea gráfica institucional UTP (Responsive Design, Mobile First).

## Stack Tecnológico
- **Lenguaje:** Java 17 LTS
- **Framework Principal:** Spring Boot 3.3.x
- **Capa Web:** Spring MVC + Thymeleaf
- **Persistencia:** Spring Data JPA (Hibernate)
- **Base de Datos:** MySQL 8.0 (Producción) / H2 Database (Testing)
- **Seguridad:** Spring Security
- **Utilitarios:** Guava, Apache Commons, Apache POI, Bootstrap Icons

## Documentación y Diseño
- [Documentación API (Javadoc)](docs/api/index.html)
- [Diagrama de Arquitectura](docs/diagramas/arquitectura.png)
- [Diagrama de Clases](docs/diagramas/clases.png)
- [Diagrama de Despliegue](docs/diagramas/despliegue.png)
- [Referencias y Bibliografía](docs/referencias.md)

## Requisitos Previos
- JDK 17
- MySQL 8 instalado y en ejecución
- Maven 3.9 (se incluye wrapper `mvnw`)

## Instrucciones de Ejecución Local
1. **Clonar el repositorio.**
2. **Configurar Base de Datos:** Crear el esquema `matricula_utp` en MySQL.
3. **Credenciales:** Asegurarse de que el usuario `root` con contraseña `password` exista (o cambiar los valores en `src/main/resources/application-local.yml`).
4. **Ejecutar el proyecto con Maven:**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
5. **Acceder a la aplicación:** `http://localhost:8080`.

### Credenciales de Prueba Iniciales
*   **Administrador:** `admin@utp.edu.pe` / `password`
*   **Estudiante:** `u12345678@utp.edu.pe` / `password`
*   **Docente:** `d12345@utp.edu.pe` / `password`

## Pruebas y CI
El proyecto cuenta con una sólida base de pruebas unitarias y de integración que cubren la capa de servicios y controladores (JUnit 5 + Mockito + MockMvc).

Para ejecutar los tests y generar el reporte de cobertura (JaCoCo):
```bash
./mvnw clean test jacoco:report
```
El reporte estará disponible en `target/site/jacoco/index.html`.
