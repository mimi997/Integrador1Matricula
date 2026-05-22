CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE estudiante (
    id BIGINT PRIMARY KEY,
    codigo_estudiante VARCHAR(20) NOT NULL UNIQUE,
    carrera VARCHAR(100) NOT NULL,
    ciclo INT NOT NULL,
    creditos INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_estudiante_usuario FOREIGN KEY (id) REFERENCES usuario(id)
);

CREATE TABLE docente (
    id BIGINT PRIMARY KEY,
    codigo_docente VARCHAR(20) NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    CONSTRAINT fk_docente_usuario FOREIGN KEY (id) REFERENCES usuario(id)
);

CREATE TABLE curso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    ciclo INT NOT NULL,
    carrera VARCHAR(100) NOT NULL,
    cupos INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE prerrequisito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    curso_prereq_id BIGINT NOT NULL,
    CONSTRAINT fk_prereq_curso FOREIGN KEY (curso_id) REFERENCES curso(id),
    CONSTRAINT fk_prereq_curso_prereq FOREIGN KEY (curso_prereq_id) REFERENCES curso(id)
);

CREATE TABLE horario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    dia VARCHAR(20) NOT NULL,
    horas VARCHAR(50) NOT NULL,
    aula VARCHAR(20) NOT NULL,
    CONSTRAINT fk_horario_curso FOREIGN KEY (curso_id) REFERENCES curso(id),
    CONSTRAINT fk_horario_docente FOREIGN KEY (docente_id) REFERENCES docente(id)
);

CREATE TABLE matricula (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    periodo VARCHAR(20) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    comprobante VARCHAR(255),
    CONSTRAINT fk_matricula_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(id)
);

CREATE TABLE detalle_matricula (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    horario_id BIGINT NOT NULL,
    estado_prereq VARCHAR(50) NOT NULL,
    CONSTRAINT fk_detalle_matricula FOREIGN KEY (matricula_id) REFERENCES matricula(id),
    CONSTRAINT fk_detalle_horario FOREIGN KEY (horario_id) REFERENCES horario(id)
);

CREATE TABLE nota (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    detalle_matricula_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    valor DECIMAL(4,2),
    aprobado BOOLEAN,
    CONSTRAINT fk_nota_detalle FOREIGN KEY (detalle_matricula_id) REFERENCES detalle_matricula(id),
    CONSTRAINT fk_nota_docente FOREIGN KEY (docente_id) REFERENCES docente(id)
);

CREATE TABLE malla_curricular (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrera VARCHAR(100) NOT NULL,
    anio INT NOT NULL
);

CREATE TABLE malla_curso (
    malla_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    ciclo INT NOT NULL,
    PRIMARY KEY (malla_id, curso_id),
    CONSTRAINT fk_mallacurso_malla FOREIGN KEY (malla_id) REFERENCES malla_curricular(id),
    CONSTRAINT fk_mallacurso_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);
