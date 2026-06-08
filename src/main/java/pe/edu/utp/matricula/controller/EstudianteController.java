package pe.edu.utp.matricula.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.matricula.dto.EstudianteDTO;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.service.EstudianteService;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estudiantes", estudianteService.findAll());
        return "estudiantes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("estudianteDTO", new EstudianteDTO());
        return "estudiantes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("estudianteDTO") EstudianteDTO estudianteDTO,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "estudiantes/form";
        }
        
        try {
            estudianteService.guardarEstudiante(estudianteDTO);
            return "redirect:/estudiantes?exito=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar estudiante: " + e.getMessage());
            return "estudiantes/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Estudiante estudiante = estudianteService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
                
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setEmail(estudiante.getUsuario().getEmail());
        dto.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        dto.setCarrera(estudiante.getCarrera());
        dto.setCiclo(estudiante.getCiclo());
        
        model.addAttribute("estudianteDTO", dto);
        return "estudiantes/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        estudianteService.eliminarEstudiante(id);
        return "redirect:/estudiantes?eliminado=true";
    }
}
