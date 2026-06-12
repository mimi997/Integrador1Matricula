package pe.edu.utp.matricula.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.repository.CursoRepository;
import pe.edu.utp.matricula.repository.DetalleMatriculaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MatriculaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DetalleMatriculaRepository detalleMatriculaRepository;

    @Autowired
    private javax.sql.DataSource dataSource;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        if (cursoRepository.count() == 0) {
            org.springframework.jdbc.datasource.init.ResourceDatabasePopulator populator = 
                new org.springframework.jdbc.datasource.init.ResourceDatabasePopulator();
            populator.addScript(new org.springframework.core.io.ClassPathResource("schema.sql"));
            populator.addScript(new org.springframework.core.io.ClassPathResource("data.sql"));
            org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute(populator, dataSource);
        }
    }

    @Test
    @WithMockUser(username = "estudiante1@utp.edu.pe", roles = "ESTUDIANTE")
    public void testFlujoCompletoMatriculaYReporte() throws Exception {
        // Estudiante 1 (ID=3) tiene Prog II aprobado. El curso Algoritmos (ID=7, Horario ID=4) requiere Prog II.
        // Por lo tanto, cumple prerrequisitos y puede matricularse.
        
        // Obtener estado inicial del cupo de Algoritmos (ID=7)
        System.out.println("DEBUG - Courses count in db: " + cursoRepository.count());
        System.out.println("DEBUG - All courses: " + cursoRepository.findAll());
        Curso curso = cursoRepository.findById(7L).orElseThrow();
        int cuposIniciales = curso.getCupos();
        assertThat(cuposIniciales).isGreaterThan(0);

        // Realizar matrícula
        mockMvc.perform(post("/matricula")
                        .param("horariosIds", "4")
                        .param("periodo", "2026-1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/matricula/comprobante/*"));

        // Verificar que el detalle de matrícula existe en la base de datos
        var detalles = detalleMatriculaRepository.findAll().stream()
                .filter(d -> d.getMatricula().getEstudiante().getId() == 3L)
                .filter(d -> "2026-1".equals(d.getMatricula().getPeriodo()))
                .toList();
        assertThat(detalles).isNotEmpty();
        assertThat(detalles.get(0).getHorario().getId()).isEqualTo(4L);

        // Verificar que el cupo del curso se redujo en 1
        Curso cursoPost = cursoRepository.findById(7L).orElseThrow();
        assertThat(cursoPost.getCupos()).isEqualTo(cuposIniciales - 1);
    }

    @Test
    @WithMockUser(username = "admin@utp.edu.pe", roles = "ADMIN")
    public void testDescargarReportesExcel() throws Exception {
        mockMvc.perform(get("/reportes/estudiantes"))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/reportes/cursos"))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/reportes/matriculas"))
                .andExpect(status().isOk());
    }
}
