package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.Curso;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByCarreraAndCiclo(String carrera, Integer ciclo);
    List<Curso> findByActivoTrue();
}
