package pe.edu.utp.matricula;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SistemaMatriculaUtpApplicationTests {

    @Autowired
    private DataSource dataSource;

	@Test
	void contextLoads() {
        assertThat(dataSource).isNotNull();
	}

}
