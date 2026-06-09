package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.repository.CursoRepository;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.MatriculaRepository;
import pe.edu.utp.matricula.util.ExcelGenerator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final ExcelGenerator excelGenerator;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final pe.edu.utp.matricula.repository.NotaRepository notaRepository;
    private final pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository;

    public ReporteService(ExcelGenerator excelGenerator, EstudianteRepository estudianteRepository,
                          CursoRepository cursoRepository, MatriculaRepository matriculaRepository,
                          pe.edu.utp.matricula.repository.NotaRepository notaRepository,
                          pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository) {
        this.excelGenerator = excelGenerator;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
    }

    public long getCursosAlLimite() {
        return cursoRepository.findAll().stream().filter(c -> c.getCupos() <= 5).count();
    }

    public double getTasaAprobacion() {
        List<pe.edu.utp.matricula.entity.Nota> notas = notaRepository.findAll();
        if (notas.isEmpty()) return 100.0;
        long aprobadas = notas.stream().filter(pe.edu.utp.matricula.entity.Nota::getAprobado).count();
        return (double) aprobadas * 100.0 / notas.size();
    }

    public long getEstudiantesRiesgo() {
        return notaRepository.findAll().stream()
                .filter(n -> !n.getAprobado())
                .map(n -> n.getDetalleMatricula().getMatricula().getEstudiante().getId())
                .distinct()
                .count();
    }

    public List<java.util.Map<String, Object>> getCursosMasDemandados() {
        List<pe.edu.utp.matricula.entity.DetalleMatricula> detalles = detalleMatriculaRepository.findAll();
        java.util.Map<pe.edu.utp.matricula.entity.Curso, Long> counts = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getHorario().getCurso(), Collectors.counting()));
        
        return counts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(e -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("nombre", e.getKey().getNombre());
                    map.put("matriculados", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public byte[] exportarEstudiantes() throws IOException {
        String[] headers = {"ID", "Código", "Email", "Carrera", "Ciclo", "Créditos", "Estado"};
        List<String[]> data = estudianteRepository.findAll().stream().map(e -> new String[]{
                e.getId().toString(),
                e.getCodigoEstudiante(),
                e.getUsuario().getEmail(),
                e.getCarrera(),
                e.getCiclo().toString(),
                e.getCreditos().toString(),
                e.getUsuario().getActivo() ? "Activo" : "Inactivo"
        }).collect(Collectors.toList());

        return excelGenerator.generarReporte("Estudiantes", headers, data);
    }

    public byte[] exportarCursos() throws IOException {
        String[] headers = {"ID", "Código", "Nombre", "Créditos", "Ciclo", "Carrera", "Cupos", "Estado"};
        List<String[]> data = cursoRepository.findAll().stream().map(c -> new String[]{
                c.getId().toString(),
                c.getCodigo(),
                c.getNombre(),
                c.getCreditos().toString(),
                c.getCiclo().toString(),
                c.getCarrera(),
                c.getCupos().toString(),
                c.getActivo() ? "Activo" : "Inactivo"
        }).collect(Collectors.toList());

        return excelGenerator.generarReporte("Cursos", headers, data);
    }

    public byte[] exportarMatriculas() throws IOException {
        String[] headers = {"ID Matrícula", "Periodo", "Estado", "Código Estudiante", "Email Estudiante"};
        List<String[]> data = matriculaRepository.findAll().stream().map(m -> new String[]{
                m.getId().toString(),
                m.getPeriodo(),
                m.getEstado(),
                m.getEstudiante().getCodigoEstudiante(),
                m.getEstudiante().getUsuario().getEmail()
        }).collect(Collectors.toList());

        return excelGenerator.generarReporte("Matrículas", headers, data);
    }
}
