package pe.edu.utp.matricula.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.utp.matricula.service.PeriodoService;

@Controller
@RequestMapping("/periodo")
public class PeriodoController {

    private final PeriodoService periodoService;

    public PeriodoController(PeriodoService periodoService) {
        this.periodoService = periodoService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("periodoActual", periodoService.getPeriodoActual());
        model.addAttribute("activo", periodoService.isActivo());
        return "periodo/index";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam("periodo") String periodo,
                             @RequestParam(value = "activo", required = false) boolean activo) {
        periodoService.setPeriodoActual(periodo);
        periodoService.setActivo(activo);
        return "redirect:/periodo?exito=true";
    }
}
