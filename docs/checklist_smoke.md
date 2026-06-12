# Checklist de Humo Manual End-to-End

Este checklist detalla los pasos para realizar la verificación manual de los flujos críticos del sistema utilizando una base de datos MySQL real con los datos semilla cargados.

## Flujos de Verificación

### 1. Control de Acceso y Redirección de Roles
- [ ] Iniciar sesión como Administrador (`admin@utp.edu.pe` / `123456`) y verificar redirección al panel de administración.
- [ ] Iniciar sesión como Docente (`docente@utp.edu.pe` / `123456`) y verificar redirección al listado de notas.
- [ ] Iniciar sesión como Estudiante (`estudiante1@utp.edu.pe` / `123456`) y verificar redirección al formulario de matrícula o dashboard del estudiante.
- [ ] Intentar acceder a rutas protegidas de administrador con un rol de estudiante y verificar que se retorne un error de acceso denegado (HTTP 403).

### 2. Gestión de Estudiantes (CRUD - Admin)
- [ ] Crear un nuevo estudiante con código único e email institucional válido (ej. `u20261001@utp.edu.pe`).
- [ ] Listar los estudiantes y buscar el nuevo registro.
- [ ] Modificar los datos del estudiante creado (nombre, apellidos, ciclo, créditos) y guardar los cambios.
- [ ] Desactivar o eliminar al estudiante y verificar que ya no aparezca como activo en las consultas del sistema.

### 3. Gestión de Cursos y Mallas (CRUD - Admin)
- [ ] Registrar un nuevo curso (ej. "Taller de Programación", 3 créditos, ciclo 5, carrera INGENIERÍA DE SISTEMAS, cupo inicial 5) con prerrequisitos si aplica.
- [ ] Buscar y modificar los cupos del curso recién creado.
- [ ] Verificar que el curso se asocie a la malla correspondiente y aparezca en la lista general de cursos.

### 4. Gestión del Período de Matrícula (Admin)
- [ ] Acceder a `/periodo` y cambiar el estado del período académico a "CERRADO".
- [ ] Iniciar sesión como estudiante e intentar registrar una matrícula. Verificar que el sistema bloquee el flujo mostrando un mensaje claro: `"El período de matrícula está cerrado"`.
- [ ] Volver como administrador y cambiar el estado del período académico a "ACTIVO" bajo el ciclo correspondiente (ej. `2026-I`).

### 5. Flujo de Matrícula Estudiante (Caso Feliz)
- [ ] Iniciar sesión como estudiante que cumple prerrequisitos (ej. `estudiante1@utp.edu.pe`).
- [ ] Seleccionar horarios de cursos permitidos (ej. Matemática II y Programación II si aprobó Matemática I y Programación I).
- [ ] Confirmar la matrícula y verificar que se redirija a la pantalla del comprobante con la información correcta de créditos acumulados y cursos inscritos.
- [ ] Comprobar en el panel de administrador que las vacantes de dichos horarios se hayan reducido en 1.

### 6. Matrícula - Restricciones y Reglas de Negocio
- [ ] **Bloqueo por Prerrequisito Pendiente:** Intentar matricularse en "Base de Datos" (horario 5) con un estudiante que no ha aprobado "Programación II" (ej. `estudiante2@utp.edu.pe`). Verificar que el sistema lance un aviso o error impidiendo la matrícula.
- [ ] **Bloqueo por Cruce de Horario:** Intentar matricularse en dos cursos programados el mismo día a la misma hora. Verificar que el sistema marque conflicto de horario e impida la matrícula.
- [ ] **Bloqueo por Falta de Cupos:** Establecer los cupos de un horario a 0 como Administrador. Intentar matricularse con un estudiante y verificar que no se permita la selección por falta de vacantes.
- [ ] **Evitar Duplicados:** Intentar reenviar el formulario de matrícula o registrar dos veces el mismo curso/horario en el mismo período y verificar que solo se registre una vez y no se descuente cupo extra.

### 7. Comprobante de Matrícula Imprimible
- [ ] Acceder al comprobante de matrícula recién generado (`/matricula/comprobante/{id}`).
- [ ] Verificar que muestre datos reales del estudiante (código, nombres, carrera), período académico actual, total de créditos inscritos y la tabla detallada de cursos con horarios y aulas.
- [ ] Presionar el botón "Imprimir" y verificar que se abra el cuadro de diálogo de impresión del navegador (`window.print()`) aplicando estilos aptos para impresión (ocultando botones y menús).

### 8. Registro de Calificaciones por Docente
- [ ] Iniciar sesión como Docente (`docente@utp.edu.pe`).
- [ ] Acceder a `/notas` y verificar la lista de estudiantes matriculados en sus cursos en el período actual.
- [ ] Registrar una nota aprobatoria (ej. 15.5) a un estudiante y guardar.
- [ ] Registrar una nota desaprobatoria (ej. 09.0) a otro estudiante y guardar.
- [ ] Verificar que las notas se guarden exitosamente y el estado cambie a "Aprobado" o "Desaprobado" según corresponda.

### 9. Récord e Historial Académico (Estudiante)
- [ ] Iniciar sesión con el estudiante calificado en el paso anterior.
- [ ] Navegar a su historial académico o récord de notas.
- [ ] Verificar que aparezca la nota registrada por el docente de manera correcta y con su respectivo estado de aprobación.

### 10. Dashboard de Reportes y Estadísticas Reales
- [ ] Iniciar sesión como Administrador y navegar a la sección de reportes.
- [ ] Verificar que los paneles informativos del dashboard muestren estadísticas reales calculadas desde la base de datos (tasa de aprobación real, cantidad de alumnos en riesgo académico, cursos con cupos críticos, etc.).
- [ ] Descargar el reporte en Excel y abrirlo para corroborar que contenga las filas correspondientes de estudiantes, cursos y matrículas generadas de manera ordenada y legible.
