package pe.edu.utp.matricula.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final pe.edu.utp.matricula.repository.UsuarioRepository usuarioRepository;
    private final pe.edu.utp.matricula.repository.EstudianteRepository estudianteRepository;
    private final pe.edu.utp.matricula.repository.CursoRepository cursoRepository;
    private final pe.edu.utp.matricula.repository.DocenteRepository docenteRepository;
    private final pe.edu.utp.matricula.service.PeriodoService periodoService;
    private final pe.edu.utp.matricula.repository.MatriculaRepository matriculaRepository;
    private final pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository;

    public HomeController(pe.edu.utp.matricula.repository.UsuarioRepository usuarioRepository, pe.edu.utp.matricula.repository.EstudianteRepository estudianteRepository, pe.edu.utp.matricula.repository.CursoRepository cursoRepository, pe.edu.utp.matricula.repository.DocenteRepository docenteRepository, pe.edu.utp.matricula.service.PeriodoService periodoService, pe.edu.utp.matricula.repository.MatriculaRepository matriculaRepository, pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.periodoService = periodoService;
        this.matriculaRepository = matriculaRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
    }

    @GetMapping({"/", "/home"})
    public String index(org.springframework.ui.Model model, java.security.Principal principal) {
        model.addAttribute("periodoActual", periodoService.getPeriodoActual());
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalCursos", cursoRepository.count());
        
        if (principal != null) {
            pe.edu.utp.matricula.entity.Usuario user = usuarioRepository.findByEmailAndActivoTrue(principal.getName()).orElse(null);
            if (user != null) {
                if (user.getNombres() != null && !user.getNombres().isEmpty()) {
                    model.addAttribute("nombreUsuario", user.getNombres() + " " + (user.getApellidos() != null ? user.getApellidos() : ""));
                }
                if ("ESTUDIANTE".equals(user.getRol().name())) {
                    pe.edu.utp.matricula.entity.Estudiante est = estudianteRepository.findById(user.getId()).orElse(null);
                    if (est != null) {
                        model.addAttribute("estudianteCreditos", est.getCreditos());
                        model.addAttribute("estudianteCiclo", est.getCiclo());
                        
                        pe.edu.utp.matricula.entity.Matricula mat = matriculaRepository.findByEstudianteIdAndPeriodo(est.getId(), periodoService.getPeriodoActual()).orElse(null);
                        int numCursos = 0;
                        if (mat != null) {
                            numCursos = detalleMatriculaRepository.findByMatriculaId(mat.getId()).size();
                            model.addAttribute("matriculaId", mat.getId());
                        }
                        model.addAttribute("cursosMatriculados", numCursos);
                    }
                }
            }
        }
        return "home";
    }}
