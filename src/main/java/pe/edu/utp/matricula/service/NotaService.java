package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.matricula.entity.DetalleMatricula;
import pe.edu.utp.matricula.entity.Docente;
import pe.edu.utp.matricula.entity.Nota;
import pe.edu.utp.matricula.exception.RecursoNoEncontradoException;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.DetalleMatriculaRepository;
import pe.edu.utp.matricula.repository.DocenteRepository;
import pe.edu.utp.matricula.repository.NotaRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final DocenteRepository docenteRepository;

    public NotaService(NotaRepository notaRepository, DetalleMatriculaRepository detalleMatriculaRepository, DocenteRepository docenteRepository) {
        this.notaRepository = notaRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.docenteRepository = docenteRepository;
    }

    @Transactional
    public Nota registrarNota(Long detalleMatriculaId, Long docenteId, BigDecimal valor) {
        DetalleMatricula dm = detalleMatriculaRepository.findById(detalleMatriculaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle de matrícula no encontrado"));

        Docente doc = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Docente no encontrado"));

        if (valor.compareTo(BigDecimal.ZERO) < 0 || valor.compareTo(new BigDecimal("20.0")) > 0) {
            throw new ReglaNegocioException("La nota debe estar entre 0 y 20");
        }

        boolean aprobado = valor.compareTo(new BigDecimal("10.5")) >= 0;

        Nota nota = new Nota(dm, doc, valor, aprobado);
        return notaRepository.save(nota);
    }

    public List<Nota> buscarPorEstudiante(Long estudianteId) {
        return notaRepository.findNotasByEstudianteId(estudianteId);
    }
}
