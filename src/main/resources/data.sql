-- ====================================================================
-- SEMILLA DE DATOS DE PRUEBA E INICIALIZACIÓN (IDEMPOTENTE)
-- ====================================================================

-- 1. Usuarios demo del sistema (Contraseña desencriptada: 123456)
INSERT IGNORE INTO usuario (id, email, password_hash, rol, activo, nombres, apellidos) VALUES 
(1, 'admin@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ADMIN', true, 'Administrador', 'General'),
(2, 'docente@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'DOCENTE', true, 'Noe', 'Docente'),
(3, 'estudiante1@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Victor', 'Mendivil'),
(4, 'estudiante2@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Estudiante', 'Dos'),
(5, 'victor.m@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Victor', 'Manuel'),
(6, 'estudiante4@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Estudiante', 'Cuatro'),
(7, 'estudiante5@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Estudiante', 'Cinco'),
(8, 'noemi.hidalgo@utp.edu.pe', '$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6', 'ESTUDIANTE', true, 'Noemi', 'Hidalgo');

-- 2. Docentes
INSERT IGNORE INTO docente (id, codigo_docente, especialidad) VALUES 
(2, 'DOC001', 'Ingeniería de Sistemas');

-- 3. Estudiantes
INSERT IGNORE INTO estudiante (id, codigo_estudiante, carrera, ciclo, creditos) VALUES 
(3, 'U20210001', 'INGENIERÍA DE SISTEMAS', 3, 40),
(4, 'U20210002', 'INGENIERÍA DE SISTEMAS', 2, 20),
(5, 'U20210003', 'INGENIERÍA DE SISTEMAS', 1, 0),
(6, 'U20210004', 'INGENIERÍA DE SISTEMAS', 4, 60),
(7, 'U20210005', 'INGENIERÍA DE SISTEMAS', 5, 80),
(8, 'U20210006', 'INGENIERÍA DE SISTEMAS', 8, 120);

-- 4. Cursos
INSERT IGNORE INTO curso (id, codigo, nombre, creditos, ciclo, carrera, cupos, activo) VALUES 
(1, 'MAT101', 'Matemática I', 4, 1, 'INGENIERÍA DE SISTEMAS', 40, true),
(2, 'FIS101', 'Física I', 4, 1, 'INGENIERÍA DE SISTEMAS', 40, true),
(3, 'PROG1', 'Programación I', 4, 1, 'INGENIERÍA DE SISTEMAS', 40, true),
(4, 'MAT102', 'Matemática II', 4, 2, 'INGENIERÍA DE SISTEMAS', 40, true),
(5, 'FIS102', 'Física II', 4, 2, 'INGENIERÍA DE SISTEMAS', 40, true),
(6, 'PROG2', 'Programación II', 4, 2, 'INGENIERÍA DE SISTEMAS', 40, true),
(7, 'ALG01', 'Algoritmos', 4, 3, 'INGENIERÍA DE SISTEMAS', 40, true),
(8, 'BD01', 'Base de Datos', 4, 3, 'INGENIERÍA DE SISTEMAS', 40, true),
(9, 'WEB01', 'Programación Web', 4, 4, 'INGENIERÍA DE SISTEMAS', 40, true),
(10, 'IS01', 'Ingeniería de Software', 4, 5, 'INGENIERÍA DE SISTEMAS', 40, true),
(11, 'ARQ01', 'Arquitectura de Software', 4, 7, 'INGENIERÍA DE SISTEMAS', 40, true),
(12, 'SEG01', 'Seguridad de la Información', 4, 7, 'INGENIERÍA DE SISTEMAS', 40, true),
(13, 'IA01', 'Inteligencia Artificial', 4, 8, 'INGENIERÍA DE SISTEMAS', 40, true),
(14, 'GP01', 'Gestión de Proyectos', 4, 8, 'INGENIERÍA DE SISTEMAS', 40, true);

-- 5. Prerrequisitos de Cursos
INSERT IGNORE INTO prerrequisito (id, curso_id, curso_prereq_id) VALUES 
(1, 4, 1),   -- Matemática II requiere Matemática I
(2, 5, 2),   -- Física II requiere Física I
(3, 6, 3),   -- Programación II requiere Programación I
(4, 7, 6),   -- Algoritmos requiere Programación II
(5, 8, 6),   -- Base de Datos requiere Programación II
(6, 9, 8),   -- Programación Web requiere Base de Datos
(7, 10, 7),  -- Ingeniería de Software requiere Algoritmos
(8, 11, 10), -- Arquitectura de Software requiere Ingeniería de Software
(9, 13, 11), -- Inteligencia Artificial requiere Arquitectura de Software
(10, 14, 11);-- Gestión de Proyectos requiere Arquitectura de Software

-- 6. Horarios Disponibles
INSERT IGNORE INTO horario (id, curso_id, docente_id, dia, horas, aula) VALUES 
(1, 4, 2, 'Lunes', '08:00-10:00', 'A-101'),
(11, 4, 2, 'Miércoles', '10:00-12:00', 'A-101'), -- Horario alternativo para Matemática II
(2, 5, 2, 'Martes', '10:00-12:00', 'A-102'),
(3, 6, 2, 'Miércoles', '08:00-10:00', 'Lab-1'),
(12, 6, 2, 'Jueves', '08:00-10:00', 'Lab-1'), -- Horario alternativo para Programación II
(4, 7, 2, 'Jueves', '14:00-16:00', 'Lab-2'),
(14, 7, 2, 'Lunes', '14:00-16:00', 'Lab-2'), -- Horario alternativo para Algoritmos
(5, 8, 2, 'Viernes', '16:00-18:00', 'Lab-3'),
(13, 8, 2, 'Sábado', '10:00-12:00', 'Lab-3'), -- Horario alternativo para Base de Datos
(6, 1, 2, 'Lunes', '10:00-12:00', 'A-103'),
(7, 3, 2, 'Martes', '14:00-16:00', 'Lab-4'),
(8, 13, 2, 'Lunes', '18:00-20:00', 'Lab-5'),
(9, 14, 2, 'Miércoles', '18:00-20:00', 'A-201'),
(100, 11, 2, 'Sábado', '08:00-10:00', 'Virtual');

-- 7. Historial y Matrícula de Estudiantes (Para demostrar cumplimiento e incumplimiento de prerrequisitos)

-- Caso A: Estudiante 1 (Victor Mendivil) tiene Matemática I y Programación I aprobados en 2023-1, y Programación II aprobado en 2023-2.
-- Por lo tanto, puede matricularse en Algoritmos (horario 4) y Base de Datos (horario 5).
INSERT IGNORE INTO matricula (id, estudiante_id, periodo, estado) VALUES 
(1, 3, '2023-1', 'CERRADA'),
(2, 3, '2023-2', 'CERRADA');

INSERT IGNORE INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES 
(2, 1, 6, 'CUMPLE'), -- Matemática I
(3, 1, 7, 'CUMPLE'), -- Programación I
(4, 2, 3, 'CUMPLE'); -- Programación II

INSERT IGNORE INTO nota (id, detalle_matricula_id, docente_id, valor, aprobado) VALUES 
(2, 2, 2, 15.0, true),
(3, 3, 2, 16.0, true),
(4, 4, 2, 14.0, true);

-- Caso B: Estudiante 8 (Noemi Hidalgo) tiene Matemática I, Programación I y Arquitectura de Software aprobados.
-- Por lo tanto, cumple con los prerrequisitos para llevar Inteligencia Artificial (horario 8) y Gestión de Proyectos (horario 9).
INSERT IGNORE INTO matricula (id, estudiante_id, periodo, estado) VALUES 
(100, 8, '2025-2', 'CERRADA'),
(101, 8, '2021-1', 'CERRADA');

INSERT IGNORE INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES 
(100, 100, 100, 'CUMPLE'), -- Arquitectura de Software
(101, 101, 6, 'CUMPLE'),   -- Matemática I
(102, 101, 7, 'CUMPLE');   -- Programación I

INSERT IGNORE INTO nota (id, detalle_matricula_id, docente_id, valor, aprobado) VALUES 
(100, 100, 2, 18.0, true),
(101, 101, 2, 15.0, true),
(102, 102, 2, 16.0, true);

-- 8. Malla Curricular
INSERT IGNORE INTO malla_curricular (id, carrera, anio) VALUES 
(1, 'Ingeniería de Sistemas', 2021);

-- 9. Asociación Malla Cursos
INSERT IGNORE INTO malla_curso (malla_id, curso_id, ciclo) VALUES 
(1, 1, 1), (1, 2, 1), (1, 3, 1),
(1, 4, 2), (1, 5, 2), (1, 6, 2),
(1, 7, 3), (1, 8, 3),
(1, 9, 4),
(1, 10, 5),
(1, 11, 7), (1, 12, 7), 
(1, 13, 8), (1, 14, 8);
