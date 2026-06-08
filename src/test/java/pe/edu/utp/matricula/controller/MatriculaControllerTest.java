package pe.edu.utp.matricula.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.service.CursoService;
import pe.edu.utp.matricula.service.HorarioService;
import pe.edu.utp.matricula.service.MatriculaService;
import pe.edu.utp.matricula.config.SecurityConfig;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MatriculaController.class)
@Import(SecurityConfig.class)
public class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private HorarioService horarioService;

    @MockBean
    private MatriculaService matriculaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EstudianteRepository estudianteRepository;

    @Test
    @WithMockUser(roles = "ESTUDIANTE")
    public void testVerFormularioMatricula() throws Exception {
        mockMvc.perform(get("/matricula"))
                .andExpect(status().isOk())
                .andExpect(view().name("matricula/form"));
    }

    @Test
    @WithMockUser(roles = "DOCENTE")
    public void testVerFormularioMatriculaForbiddenParaDocente() throws Exception {
        mockMvc.perform(get("/matricula"))
                .andExpect(status().isForbidden());
    }
}
