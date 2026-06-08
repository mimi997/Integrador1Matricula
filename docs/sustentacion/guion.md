# Guion de Sustentación - Sistema de Matrícula UTP

## 1. Introducción (3 min)
*   **Saludo:** Buenas tardes jurado y público presente. Somos el Grupo 6.
*   **Problema:** El proceso de matrícula en la UTP presentaba problemas de lentitud, cruce de horarios y falta de escalabilidad.
*   **Solución:** Presentamos un Sistema Integrado de Matrícula desarrollado bajo la arquitectura MVC usando Spring Boot, Thymeleaf y MySQL, aplicando principios SOLID para garantizar mantenibilidad.

## 2. Arquitectura y Tecnologías (3 min)
*   **Backend:** Spring Boot (Java 17), Spring Data JPA para persistencia, y Spring Security para gestión de roles (ADMIN, DOCENTE, ESTUDIANTE) y cifrado BCrypt.
*   **Frontend:** Interfaz web construida con Thymeleaf, CSS puro ajustado a la paleta institucional (UTP Navy, Red) y Bootstrap 5 para responsividad.
*   **Calidad:** Cobertura de pruebas (JUnit 5 + Mockito) que asegura la confiabilidad de la lógica de negocio (validación de cupos y prerrequisitos).

## 3. Demostración del Sistema (Live Demo - 5 min)
*   **Flujo Administrador:** Gestión de periodo (abrir/cerrar matrícula), gestión de cursos y descarga de reportes Excel.
*   **Flujo Estudiante:** Selección de cursos, validación en tiempo real (evita cruce de horarios, verifica prerrequisitos) y visualización del comprobante.
*   **Flujo Docente:** Ingreso de notas (evaluaciones), las cuales se reflejan en el historial académico del estudiante.

## 4. Conclusiones y Próximos Pasos (2 min)
*   Se logró un producto 100% funcional que automatiza el control de vacantes.
*   **Futuro:** Integración de pasarela de pagos y notificaciones por correo usando AWS SES.
*   *Gracias por su atención, pasamos a la ronda de preguntas.*
