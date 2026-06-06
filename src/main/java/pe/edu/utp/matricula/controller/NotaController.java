package pe.edu.utp.matricula.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.utp.matricula.entity.Docente;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.DocenteRepository;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.service.NotaService;

import java.math.BigDecimal;

@Controller
public class NotaController {

    private final NotaService notaService;
    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;

    public NotaController(NotaService notaService, UsuarioRepository usuarioRepository, DocenteRepository docenteRepository) {
        this.notaService = notaService;
        this.usuarioRepository = usuarioRepository;
        this.docenteRepository = docenteRepository;
    }

    @GetMapping("/notas")
    public String verNotas(Model model, Authentication auth) {
        // Lógica para mostrar estudiantes/cursos asignados al docente
        return "notas/lista";
    }

    @PostMapping("/notas/registrar")
    public String registrarNota(@RequestParam Long detalleMatriculaId,
                                @RequestParam BigDecimal valor,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElseThrow();
            Docente doc = docenteRepository.findById(user.getId()).orElseThrow();

            notaService.registrarNota(detalleMatriculaId, doc.getId(), valor);
            redirectAttributes.addFlashAttribute("mensajeExito", "Nota registrada exitosamente");
        } catch (ReglaNegocioException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado.");
        }
        return "redirect:/notas";
    }
}
