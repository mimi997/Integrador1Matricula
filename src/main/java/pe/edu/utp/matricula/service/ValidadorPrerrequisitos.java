package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Prerrequisito;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.EstudianteRepository;

import pe.edu.utp.matricula.util.CachePrerrequisitos;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidadorPrerrequisitos {

    private final CachePrerrequisitos cachePrerrequisitos;
    private final EstudianteRepository estudianteRepository;

    public ValidadorPrerrequisitos(CachePrerrequisitos cachePrerrequisitos, EstudianteRepository estudianteRepository) {
        this.cachePrerrequisitos = cachePrerrequisitos;
        this.estudianteRepository = estudianteRepository;
    }

    public void validar(Long estudianteId, Curso curso) {
        List<Prerrequisito> prerrequisitos = cachePrerrequisitos.getPrerrequisitos(curso.getId());
        if (prerrequisitos.isEmpty()) {
            return;
        }

        List<Curso> cursosAprobados = estudianteRepository.findCursosAprobados(estudianteId);
        List<Long> idsAprobados = cursosAprobados.stream().map(Curso::getId).collect(Collectors.toList());

        for (Prerrequisito pre : prerrequisitos) {
            if (!idsAprobados.contains(pre.getCursoPrereq().getId())) {
                throw new ReglaNegocioException("No cumple con el prerrequisito: " + pre.getCursoPrereq().getNombre() + " para el curso " + curso.getNombre());
            }
        }
    }

    public boolean cumplePrerrequisitos(Long estudianteId, Curso curso) {
        List<Prerrequisito> prerrequisitos = cachePrerrequisitos.getPrerrequisitos(curso.getId());
        if (prerrequisitos.isEmpty()) {
            return true;
        }

        List<Curso> cursosAprobados = estudianteRepository.findCursosAprobados(estudianteId);
        List<Long> idsAprobados = cursosAprobados.stream().map(Curso::getId).collect(Collectors.toList());

        for (Prerrequisito pre : prerrequisitos) {
            if (!idsAprobados.contains(pre.getCursoPrereq().getId())) {
                return false;
            }
        }
        return true;
    }
}
