package pe.edu.utp.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.matricula.entity.MallaCurso;
import pe.edu.utp.matricula.entity.MallaCursoId;

import java.util.List;

@Repository
public interface MallaCursoRepository extends JpaRepository<MallaCurso, MallaCursoId> {
    List<MallaCurso> findByMallaId(Long mallaId);
}
