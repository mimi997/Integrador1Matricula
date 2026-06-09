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
    private final pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository;
    private final pe.edu.utp.matricula.service.PeriodoService periodoService;
    private final pe.edu.utp.matricula.repository.NotaRepository notaRepository;

    public NotaController(NotaService notaService, UsuarioRepository usuarioRepository, DocenteRepository docenteRepository, pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository, pe.edu.utp.matricula.service.PeriodoService periodoService, pe.edu.utp.matricula.repository.NotaRepository notaRepository) {
        this.notaService = notaService;
        this.usuarioRepository = usuarioRepository;
        this.docenteRepository = docenteRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.periodoService = periodoService;
        this.notaRepository = notaRepository;
    }

    @GetMapping("/notas")
    public String verNotas(Model model, Authentication auth) {
        Usuario user = usuarioRepository.findByEmailAndActivoTrue(auth.getName()).orElse(null);
        if (user == null) return "redirect:/";
        
        Docente docente = docenteRepository.findById(user.getId()).orElse(null);
        if (docente == null) return "redirect:/";

        String periodo = periodoService.getPeriodoActual();
        java.util.List<pe.edu.utp.matricula.entity.DetalleMatricula> detalles = 
                detalleMatriculaRepository.findCalificablesPorDocenteYPeriodo(docente.getId(), periodo);
        
        java.util.Map<Long, java.math.BigDecimal> notasMap = new java.util.HashMap<>();
        for (pe.edu.utp.matricula.entity.DetalleMatricula det : detalles) {
            notaRepository.findByDetalleMatriculaId(det.getId())
                    .ifPresent(n -> notasMap.put(det.getId(), n.getValor()));
        }

        model.addAttribute("detalles", detalles);
        model.addAttribute("notas", notasMap);
        model.addAttribute("periodoActual", periodo);
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
