package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.dto.EstudianteDTO;
import pe.edu.utp.matricula.entity.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteService {
    List<Estudiante> findAll();
    Optional<Estudiante> findById(Long id);
    Estudiante guardarEstudiante(EstudianteDTO dto);
    void eliminarEstudiante(Long id);
}
