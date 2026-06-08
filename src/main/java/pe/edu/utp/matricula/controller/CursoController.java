package pe.edu.utp.matricula.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.matricula.dto.CursoDTO;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.service.CursoService;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.findAll());
        return "cursos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cursoDTO", new CursoDTO());
        return "cursos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("cursoDTO") CursoDTO cursoDTO,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "cursos/form";
        }
        
        try {
            Curso curso;
            if (cursoDTO.getId() != null) {
                curso = cursoService.findById(cursoDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
            } else {
                curso = new Curso();
            }
            
            curso.setCodigo(cursoDTO.getCodigo());
            curso.setNombre(cursoDTO.getNombre());
            curso.setCreditos(cursoDTO.getCreditos());
            curso.setCiclo(cursoDTO.getCiclo());
            curso.setCarrera(cursoDTO.getCarrera());
            curso.setCupos(cursoDTO.getCupos());
            curso.setActivo(cursoDTO.getActivo() != null ? cursoDTO.getActivo() : true);
            
            cursoService.guardarCurso(curso);
            
            return "redirect:/cursos?exito=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar curso: " + e.getMessage());
            return "cursos/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Curso curso = cursoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
                
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setCodigo(curso.getCodigo());
        dto.setNombre(curso.getNombre());
        dto.setCreditos(curso.getCreditos());
        dto.setCiclo(curso.getCiclo());
        dto.setCarrera(curso.getCarrera());
        dto.setCupos(curso.getCupos());
        dto.setActivo(curso.getActivo());
        
        model.addAttribute("cursoDTO", dto);
        return "cursos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id) {
        cursoService.eliminarCurso(id);
        return "redirect:/cursos?eliminado=true";
    }
}
