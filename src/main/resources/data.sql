-- Usuarios
INSERT INTO usuario (id, email, password_hash, rol, activo) VALUES 
(1, 'admin@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ADMIN', true),
(2, 'docente@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'DOCENTE', true),
(3, 'estudiante1@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ESTUDIANTE', true),
(4, 'estudiante2@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ESTUDIANTE', true),
(5, 'estudiante3@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ESTUDIANTE', true),
(6, 'estudiante4@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ESTUDIANTE', true),
(7, 'estudiante5@utp.edu.pe', '$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i', 'ESTUDIANTE', true);

-- Docente
INSERT INTO docente (id, codigo_docente, especialidad) VALUES 
(2, 'DOC001', 'Ingeniería de Sistemas');

-- Estudiantes
INSERT INTO estudiante (id, codigo_estudiante, carrera, ciclo, creditos) VALUES 
(3, 'U20210001', 'Ingeniería de Sistemas', 3, 40),
(4, 'U20210002', 'Ingeniería de Sistemas', 2, 20),
(5, 'U20210003', 'Ingeniería de Sistemas', 1, 0),
(6, 'U20210004', 'Ingeniería de Sistemas', 4, 60),
(7, 'U20210005', 'Ingeniería de Sistemas', 5, 80);

-- Cursos
INSERT INTO curso (id, codigo, nombre, creditos, ciclo, carrera, cupos, activo) VALUES 
(1, 'MAT101', 'Matemática I', 4, 1, 'Ingeniería de Sistemas', 40, true),
(2, 'FIS101', 'Física I', 4, 1, 'Ingeniería de Sistemas', 40, true),
(3, 'PROG1', 'Programación I', 4, 1, 'Ingeniería de Sistemas', 40, true),
(4, 'MAT102', 'Matemática II', 4, 2, 'Ingeniería de Sistemas', 40, true),
(5, 'FIS102', 'Física II', 4, 2, 'Ingeniería de Sistemas', 40, true),
(6, 'PROG2', 'Programación II', 4, 2, 'Ingeniería de Sistemas', 40, true),
(7, 'ALG01', 'Algoritmos', 4, 3, 'Ingeniería de Sistemas', 40, true),
(8, 'BD01', 'Base de Datos', 4, 3, 'Ingeniería de Sistemas', 40, true),
(9, 'WEB01', 'Programación Web', 4, 4, 'Ingeniería de Sistemas', 40, true),
(10, 'IS01', 'Ingeniería de Software', 4, 5, 'Ingeniería de Sistemas', 40, true);

-- Prerrequisitos
INSERT INTO prerrequisito (curso_id, curso_prereq_id) VALUES 
(4, 1), -- Mat II requiere Mat I
(5, 2), -- Fis II requiere Fis I
(6, 3), -- Prog II requiere Prog I
(7, 6), -- Algoritmos requiere Prog II
(8, 6), -- BD requiere Prog II
(9, 8), -- Web requiere BD
(10, 7); -- IS requiere Algoritmos

-- Horarios
INSERT INTO horario (id, curso_id, docente_id, dia, horas, aula) VALUES 
(1, 4, 2, 'Lunes', '08:00-10:00', 'A-101'),
(2, 5, 2, 'Martes', '10:00-12:00', 'A-102'),
(3, 6, 2, 'Miércoles', '08:00-10:00', 'Lab-1'),
(4, 7, 2, 'Jueves', '14:00-16:00', 'Lab-2'),
(5, 8, 2, 'Viernes', '16:00-18:00', 'Lab-3');

-- Notas históricas (para que Estudiante 1 U20210001 tenga Prog I y Prog II aprobados, y pueda llevar Algoritmos y BD)
-- Periodo 2023-1
INSERT INTO matricula (id, estudiante_id, periodo, estado) VALUES (1, 3, '2023-1', 'CERRADA');
INSERT INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES (1, 1, 1, 'CUMPLE'); -- Un horario falso para justificar la nota, o mejor asociarlo a los cursos 1 y 3.
-- Para mantener la integridad, insertaré horarios para los cursos 1 y 3.
INSERT INTO horario (id, curso_id, docente_id, dia, horas, aula) VALUES (6, 1, 2, 'Lunes', '10:00-12:00', 'A-103');
INSERT INTO horario (id, curso_id, docente_id, dia, horas, aula) VALUES (7, 3, 2, 'Martes', '14:00-16:00', 'Lab-4');
INSERT INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES (2, 1, 6, 'CUMPLE');
INSERT INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES (3, 1, 7, 'CUMPLE');
INSERT INTO nota (detalle_matricula_id, docente_id, valor, aprobado) VALUES (2, 2, 15.0, true);
INSERT INTO nota (detalle_matricula_id, docente_id, valor, aprobado) VALUES (3, 2, 16.0, true);

-- Periodo 2023-2 (Aprueba Prog II)
INSERT INTO matricula (id, estudiante_id, periodo, estado) VALUES (2, 3, '2023-2', 'CERRADA');
INSERT INTO detalle_matricula (id, matricula_id, horario_id, estado_prereq) VALUES (4, 2, 3, 'CUMPLE'); -- Horario 3 es Prog II
INSERT INTO nota (detalle_matricula_id, docente_id, valor, aprobado) VALUES (4, 2, 14.0, true);

-- Malla Curricular
INSERT INTO malla_curricular (id, carrera, anio) VALUES (1, 'Ingeniería de Sistemas', 2021);

-- Malla Cursos
INSERT INTO malla_curso (malla_id, curso_id, ciclo) VALUES 
(1, 1, 1), (1, 2, 1), (1, 3, 1),
(1, 4, 2), (1, 5, 2), (1, 6, 2),
(1, 7, 3), (1, 8, 3),
(1, 9, 4),
(1, 10, 5);
