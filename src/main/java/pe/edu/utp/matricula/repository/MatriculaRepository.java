package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.Matricula;

import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    Optional<Matricula> findByEstudianteIdAndPeriodo(Long estudianteId, String periodo);
    boolean existsByEstudianteIdAndPeriodo(Long estudianteId, String periodo);
}
