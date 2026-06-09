package pe.edu.utp.matricula.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.utp.matricula.service.ReporteService;

import java.io.IOException;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;
    private final pe.edu.utp.matricula.repository.EstudianteRepository estudianteRepository;
    private final pe.edu.utp.matricula.repository.CursoRepository cursoRepository;

    public ReporteController(ReporteService reporteService, 
                             pe.edu.utp.matricula.repository.EstudianteRepository estudianteRepository,
                             pe.edu.utp.matricula.repository.CursoRepository cursoRepository) {
        this.reporteService = reporteService;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public String index(org.springframework.ui.Model model) {
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalCursos", cursoRepository.count());
        model.addAttribute("cursosAlLimite", reporteService.getCursosAlLimite());
        model.addAttribute("tasaAprobacion", String.format(java.util.Locale.US, "%.1f", reporteService.getTasaAprobacion()));
        model.addAttribute("estudiantesRiesgo", reporteService.getEstudiantesRiesgo());
        model.addAttribute("cursosDemandados", reporteService.getCursosMasDemandados());
        return "reportes/index";
    }

    @GetMapping("/estudiantes")
    public ResponseEntity<byte[]> descargarReporteEstudiantes() throws IOException {
        byte[] excelBytes = reporteService.exportarEstudiantes();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_estudiantes.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/cursos")
    public ResponseEntity<byte[]> descargarReporteCursos() throws IOException {
        byte[] excelBytes = reporteService.exportarCursos();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_cursos.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/matriculas")
    public ResponseEntity<byte[]> descargarReporteMatriculas() throws IOException {
        byte[] excelBytes = reporteService.exportarMatriculas();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_matriculas.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
