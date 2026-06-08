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

    public MatriculaController(CursoService cursoService, HorarioService horarioService, MatriculaService matriculaService, UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository) {
        this.cursoService = cursoService;
        this.horarioService = horarioService;
        this.matriculaService = matriculaService;
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping("/matricula")
    public String verFormularioMatricula(Model model, Authentication auth) {
        model.addAttribute("cursos", cursoService.listarCursosActivos());
        // En una app real, se cargan los horarios mediante AJAX al seleccionar un curso,
        // o se envía la lista de cursos con sus horarios
        return "matricula/form";
    }

    @PostMapping("/matricula")
    public String procesarMatricula(@RequestParam List<Long> horariosIds,
                                    @RequestParam String periodo,
                                    Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElseThrow();
            Estudiante est = estudianteRepository.findById(user.getId()).orElseThrow();

            Matricula matricula = matriculaService.matricular(est.getId(), horariosIds, periodo);
            redirectAttributes.addFlashAttribute("mensajeExito", "Matrícula realizada exitosamente");
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
        // Here we could fetch the matricula details. We can use a repository for now.
        // Assuming we have a MatriculaRepository or we can just fetch it somehow.
        // Let's add it to the model.
        model.addAttribute("matriculaId", id);
        return "matricula/comprobante";
    }
}
