package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.DetalleMatricula;

import java.util.List;

@Repository
public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Long> {
    List<DetalleMatricula> findByMatriculaId(Long matriculaId);

    @org.springframework.data.jpa.repository.Query("SELECT dm FROM DetalleMatricula dm WHERE dm.horario.docente.id = :docenteId AND dm.matricula.periodo = :periodo")
    List<DetalleMatricula> findCalificablesPorDocenteYPeriodo(
            @org.springframework.data.repository.query.Param("docenteId") Long docenteId, 
            @org.springframework.data.repository.query.Param("periodo") String periodo);
}
