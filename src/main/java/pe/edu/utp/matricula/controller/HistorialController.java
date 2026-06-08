package pe.edu.utp.matricula.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.utp.matricula.entity.Nota;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.service.NotaService;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class HistorialController {

    private final NotaService notaService;
    private final UsuarioRepository usuarioRepository;

    public HistorialController(NotaService notaService, UsuarioRepository usuarioRepository) {
        this.notaService = notaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String verHistorial(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                
        // El ID del usuario es el mismo que el del estudiante por @MapsId
        List<Nota> notas = notaService.buscarPorEstudiante(usuario.getId());
        
        model.addAttribute("notas", notas);
        return "historial";
    }
}
