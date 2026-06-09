package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.Nota;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    
    @Query("SELECT n FROM Nota n WHERE n.detalleMatricula.matricula.estudiante.id = :estudianteId")
    List<Nota> findNotasByEstudianteId(Long estudianteId);

    java.util.Optional<Nota> findByDetalleMatriculaId(Long detalleMatriculaId);
}
