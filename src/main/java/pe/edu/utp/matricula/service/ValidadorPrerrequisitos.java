package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Curso;

public interface ValidadorPrerrequisitos {
    void validar(Long estudianteId, Curso curso);
    boolean cumplePrerrequisitos(Long estudianteId, Curso curso);
}
