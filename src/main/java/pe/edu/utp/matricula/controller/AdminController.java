package pe.edu.utp.matricula.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.CursoRepository;

@Controller
public class AdminController {

    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public AdminController(EstudianteRepository estudianteRepository, CursoRepository cursoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "estudiantes/lista";
    }

    @GetMapping("/cursos")
    public String cursos(Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "cursos/lista";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        return "reportes/index";
    }
}
