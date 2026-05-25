package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidad que representa un prerrequisito de un curso.
 */
@Entity
@Table(name = "prerrequisito")
public class Prerrequisito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_prereq_id", nullable = false)
    private Curso cursoPrereq;

    public Prerrequisito() {
    }

    public Prerrequisito(Curso curso, Curso cursoPrereq) {
        this.curso = curso;
        this.cursoPrereq = cursoPrereq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Curso getCursoPrereq() {
        return cursoPrereq;
    }

    public void setCursoPrereq(Curso cursoPrereq) {
        this.cursoPrereq = cursoPrereq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prerrequisito that = (Prerrequisito) o;
        return Objects.equals(curso, that.curso) && Objects.equals(cursoPrereq, that.cursoPrereq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curso, cursoPrereq);
    }
}
