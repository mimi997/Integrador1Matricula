package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entidad pivote entre MallaCurricular y Curso.
 */
@Entity
@Table(name = "malla_curso")
@IdClass(MallaCursoId.class)
public class MallaCurso {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "malla_id", nullable = false)
    private MallaCurricular malla;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @NotNull
    @Column(nullable = false)
    private Integer ciclo;

    public MallaCurso() {
    }

    public MallaCurso(MallaCurricular malla, Curso curso, Integer ciclo) {
        this.malla = malla;
        this.curso = curso;
        this.ciclo = ciclo;
    }

    public MallaCurricular getMalla() {
        return malla;
    }

    public void setMalla(MallaCurricular malla) {
        this.malla = malla;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MallaCurso that = (MallaCurso) o;
        return Objects.equals(malla, that.malla) && Objects.equals(curso, that.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(malla, curso);
    }
}
