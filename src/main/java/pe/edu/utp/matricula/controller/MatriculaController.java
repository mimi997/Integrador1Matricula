package pe.edu.utp.matricula.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.entity.Matricula;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.service.CursoService;
import pe.edu.utp.matricula.service.HorarioService;
import pe.edu.utp.matricula.service.MatriculaService;

import java.util.List;

@Controller
public class MatriculaController {

    private final CursoService cursoService;
    private final HorarioService horarioService;
    private final MatriculaService matriculaService;
    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final pe.edu.utp.matricula.service.ValidadorPrerrequisitos validadorPrerrequisitos;
    private final pe.edu.utp.matricula.service.PeriodoService periodoService;
    private final pe.edu.utp.matricula.repository.MatriculaRepository matriculaRepository;
    private final pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository;
    private final pe.edu.utp.matricula.repository.HorarioRepository horarioRepository;
    private final pe.edu.utp.matricula.service.DetectorConflictoHorario detectorConflictoHorario;

    public MatriculaController(CursoService cursoService, HorarioService horarioService, MatriculaService matriculaService, UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository, pe.edu.utp.matricula.service.ValidadorPrerrequisitos validadorPrerrequisitos, pe.edu.utp.matricula.service.PeriodoService periodoService, pe.edu.utp.matricula.repository.MatriculaRepository matriculaRepository, pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository, pe.edu.utp.matricula.repository.HorarioRepository horarioRepository, pe.edu.utp.matricula.service.DetectorConflictoHorario detectorConflictoHorario) {
        this.cursoService = cursoService;
        this.horarioService = horarioService;
        this.matriculaService = matriculaService;
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.validadorPrerrequisitos = validadorPrerrequisitos;
        this.periodoService = periodoService;
        this.matriculaRepository = matriculaRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.horarioRepository = horarioRepository;
        this.detectorConflictoHorario = detectorConflictoHorario;
    }

    @GetMapping("/matricula")
    public String verFormularioMatricula(Model model, Authentication auth) {
        Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElse(null);
        if (user == null) return "redirect:/";
        
        List<pe.edu.utp.matricula.entity.Curso> aprobados = estudianteRepository.findCursosAprobados(user.getId());
        List<Long> idsAprobados = aprobados.stream().map(pe.edu.utp.matricula.entity.Curso::getId).collect(java.util.stream.Collectors.toList());
        
        List<pe.edu.utp.matricula.entity.Horario> horariosDisponibles = horarioService.listarTodos().stream()
                .filter(h -> validadorPrerrequisitos.cumplePrerrequisitos(user.getId(), h.getCurso()))
                .filter(h -> !idsAprobados.contains(h.getCurso().getId()))
                .collect(java.util.stream.Collectors.toList());
                
        String periodoActual = periodoService.getPeriodoActual();
        Matricula matriculaActual = matriculaRepository.findByEstudianteIdAndPeriodo(user.getId(), periodoActual).orElse(null);
        List<pe.edu.utp.matricula.entity.DetalleMatricula> detallesActuales = java.util.Collections.emptyList();
        if (matriculaActual != null) {
            detallesActuales = detalleMatriculaRepository.findByMatriculaId(matriculaActual.getId());
        }

        final List<pe.edu.utp.matricula.entity.DetalleMatricula> finalDetalles = detallesActuales;

        List<pe.edu.utp.matricula.entity.Curso> cursosOrdenados = horariosDisponibles.stream()
                .map(pe.edu.utp.matricula.entity.Horario::getCurso)
                .distinct()
                .sorted(java.util.Comparator.comparing(pe.edu.utp.matricula.entity.Curso::getCiclo)
                        .thenComparing(pe.edu.utp.matricula.entity.Curso::getNombre))
                .collect(java.util.stream.Collectors.toList());

        List<CursoMatriculaDTO> cursosMatricula = new java.util.ArrayList<>();
        for (pe.edu.utp.matricula.entity.Curso c : cursosOrdenados) {
            List<pe.edu.utp.matricula.entity.Horario> horariosDeCurso = horariosDisponibles.stream()
                    .filter(h -> h.getCurso().getId().equals(c.getId()))
                    .collect(java.util.stream.Collectors.toList());

            pe.edu.utp.matricula.entity.DetalleMatricula detalleEnrolled = finalDetalles.stream()
                    .filter(d -> d.getHorario().getCurso().getId().equals(c.getId()))
                    .findFirst()
                    .orElse(null);

            boolean matriculado = (detalleEnrolled != null);
            Long horarioMatriculadoId = matriculado ? detalleEnrolled.getHorario().getId() : null;
            Long detalleMatriculaId = matriculado ? detalleEnrolled.getId() : null;

            cursosMatricula.add(new CursoMatriculaDTO(c, horariosDeCurso, matriculado, horarioMatriculadoId, detalleMatriculaId));
        }

        model.addAttribute("cursosMatricula", cursosMatricula);
        model.addAttribute("periodoActual", periodoActual);
        return "matricula/form";
    }

    @PostMapping("/matricula")
    public String procesarMatricula(@RequestParam(value = "horariosIds", required = false) List<Long> horariosIds,
                                    @RequestParam String periodo,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElseThrow();
            Estudiante est = estudianteRepository.findById(user.getId()).orElseThrow();

            if (horariosIds == null) {
                horariosIds = java.util.Collections.emptyList();
            }

            Matricula matricula = matriculaService.matricular(est.getId(), horariosIds, periodo);
            redirectAttributes.addFlashAttribute("mensajeExito", "Matrícula actualizada exitosamente");
            return "redirect:/matricula/comprobante/" + matricula.getId();
        } catch (ReglaNegocioException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado.");
        }
        return "redirect:/matricula";
    }

    @GetMapping("/matricula/comprobante/{id}")
    public String verComprobante(@org.springframework.web.bind.annotation.PathVariable("id") Long id, Model model) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new pe.edu.utp.matricula.exception.RecursoNoEncontradoException("Matrícula no encontrada"));
        
        List<pe.edu.utp.matricula.entity.DetalleMatricula> detalles = detalleMatriculaRepository.findByMatriculaId(id);
        int totalCreditos = detalles.stream()
                .mapToInt(d -> d.getHorario().getCurso().getCreditos())
                .sum();
        
        model.addAttribute("matricula", matricula);
        model.addAttribute("estudiante", matricula.getEstudiante());
        model.addAttribute("detalles", detalles);
        model.addAttribute("totalCreditos", totalCreditos);
        model.addAttribute("periodo", matricula.getPeriodo());
        model.addAttribute("fecha", new java.util.Date());
        return "matricula/comprobante";
    }

    @PostMapping("/matricula/cancelar/{detalleId}")
    public String cancelarMatriculaDetalle(@org.springframework.web.bind.annotation.PathVariable("detalleId") Long detalleId,
                                           Authentication auth,
                                           RedirectAttributes redirectAttributes) {
        try {
            Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElseThrow();
            matriculaService.cancelarDetalle(user.getId(), detalleId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Matrícula del curso cancelada correctamente.");
        } catch (ReglaNegocioException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al cancelar la matrícula del curso.");
        }
        return "redirect:/matricula";
    }

    @GetMapping("/matricula/validar")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> validarHorario(@RequestParam Long horarioId, Authentication auth) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElseThrow();
            pe.edu.utp.matricula.entity.Horario horario = horarioRepository.findById(horarioId)
                    .orElseThrow(() -> new pe.edu.utp.matricula.exception.RecursoNoEncontradoException("Horario no encontrado"));
            
            validadorPrerrequisitos.validar(user.getId(), horario.getCurso());
            
            // Validate conflict of schedule, excluding any active schedule of the same course
            List<pe.edu.utp.matricula.entity.Horario> horariosExistentes = horarioRepository.findHorariosByEstudianteAndPeriodo(user.getId(), periodoService.getPeriodoActual());
            List<pe.edu.utp.matricula.entity.Horario> horariosFiltrados = horariosExistentes.stream()
                    .filter(h -> !h.getCurso().getId().equals(horario.getCurso().getId()))
                    .collect(java.util.stream.Collectors.toList());

            if (detectorConflictoHorario.hayConflicto(horario, horariosFiltrados)) {
                response.put("ok", false);
                response.put("motivo", "Existe conflicto de horario con el curso: " + horario.getCurso().getNombre());
                return response;
            }
            
            response.put("ok", true);
        } catch (ReglaNegocioException ex) {
            response.put("ok", false);
            response.put("motivo", ex.getMessage());
        } catch (Exception ex) {
            response.put("ok", false);
            response.put("motivo", "Error al validar.");
        }
        return response;
    }

    public static class CursoMatriculaDTO {
        private final pe.edu.utp.matricula.entity.Curso curso;
        private final List<pe.edu.utp.matricula.entity.Horario> horarios;
        private final boolean matriculado;
        private final Long horarioMatriculadoId;
        private final Long detalleMatriculaId;

        public CursoMatriculaDTO(pe.edu.utp.matricula.entity.Curso curso, List<pe.edu.utp.matricula.entity.Horario> horarios, boolean matriculado, Long horarioMatriculadoId, Long detalleMatriculaId) {
            this.curso = curso;
            this.horarios = horarios;
            this.matriculado = matriculado;
            this.horarioMatriculadoId = horarioMatriculadoId;
            this.detalleMatriculaId = detalleMatriculaId;
        }

        public pe.edu.utp.matricula.entity.Curso getCurso() { return curso; }
        public List<pe.edu.utp.matricula.entity.Horario> getHorarios() { return horarios; }
        public boolean isMatriculado() { return matriculado; }
        public Long getHorarioMatriculadoId() { return horarioMatriculadoId; }
        public Long getDetalleMatriculaId() { return detalleMatriculaId; }
    }
}
