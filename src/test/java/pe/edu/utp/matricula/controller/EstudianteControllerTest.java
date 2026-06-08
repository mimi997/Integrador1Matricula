package pe.edu.utp.matricula.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.matricula.config.SecurityConfig;
import pe.edu.utp.matricula.service.EstudianteService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.context.annotation.Import;

@WebMvcTest(EstudianteController.class)
@Import(SecurityConfig.class)
public class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstudianteService estudianteService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testListarEstudiantes() throws Exception {
        when(estudianteService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(view().name("estudiantes/lista"));
    }

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    public void testListarEstudiantesForbiddenParaEstudiante() throws Exception {
        mockMvc.perform(get("/estudiantes"))
                .andExpect(status().isForbidden());
    }
}
