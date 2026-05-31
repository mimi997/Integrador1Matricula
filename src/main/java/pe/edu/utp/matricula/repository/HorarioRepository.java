package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.Horario;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    
    List<Horario> findByCursoId(Long cursoId);

    @Query("SELECT dm.horario FROM DetalleMatricula dm WHERE dm.matricula.estudiante.id = :estudianteId AND dm.matricula.periodo = :periodo")
    List<Horario> findHorariosByEstudianteAndPeriodo(Long estudianteId, String periodo);
}
