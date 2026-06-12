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

    @MockBean
    private pe.edu.utp.matricula.service.ValidadorPrerrequisitos validadorPrerrequisitos;

    @MockBean
    private pe.edu.utp.matricula.service.PeriodoService periodoService;

    @MockBean
    private pe.edu.utp.matricula.repository.MatriculaRepository matriculaRepository;

    @MockBean
    private pe.edu.utp.matricula.repository.DetalleMatriculaRepository detalleMatriculaRepository;

    @MockBean
    private pe.edu.utp.matricula.repository.HorarioRepository horarioRepository;

    @MockBean
    private pe.edu.utp.matricula.service.DetectorConflictoHorario detectorConflictoHorario;

    @Test
    @WithMockUser(username = "user@utp.edu.pe", roles = "ESTUDIANTE")
    public void testVerFormularioMatricula() throws Exception {
        pe.edu.utp.matricula.entity.Usuario mockUser = new pe.edu.utp.matricula.entity.Usuario();
        mockUser.setId(1L);
        mockUser.setEmail("user@utp.edu.pe");
        mockUser.setRol(pe.edu.utp.matricula.entity.RolUsuario.ESTUDIANTE);
        mockUser.setActivo(true);
        org.mockito.Mockito.when(usuarioRepository.findByEmailAndActivoTrue("user@utp.edu.pe"))
                .thenReturn(java.util.Optional.of(mockUser));
        org.mockito.Mockito.when(estudianteRepository.findCursosAprobados(1L)).thenReturn(java.util.Collections.emptyList());
        org.mockito.Mockito.when(horarioService.listarTodos()).thenReturn(java.util.Collections.emptyList());
        org.mockito.Mockito.when(periodoService.getPeriodoActual()).thenReturn("2026-1");

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
