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

    public ReporteService(ExcelGenerator excelGenerator, EstudianteRepository estudianteRepository,
                          CursoRepository cursoRepository, MatriculaRepository matriculaRepository) {
        this.excelGenerator = excelGenerator;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
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
