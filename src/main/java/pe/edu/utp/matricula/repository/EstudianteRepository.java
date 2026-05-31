package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);

    @Query("SELECT c FROM Nota n JOIN n.detalleMatricula dm JOIN dm.horario h JOIN h.curso c JOIN dm.matricula m WHERE m.estudiante.id = :estudianteId AND n.aprobado = true")
    List<Curso> findCursosAprobados(Long estudianteId);
}
