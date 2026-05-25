package pe.edu.utp.matricula.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * ID Compuesto para la entidad MallaCurso.
 */
public class MallaCursoId implements Serializable {

    private Long malla;
    private Long curso;

    public MallaCursoId() {
    }

    public MallaCursoId(Long malla, Long curso) {
        this.malla = malla;
        this.curso = curso;
    }

    public Long getMalla() {
        return malla;
    }

    public void setMalla(Long malla) {
        this.malla = malla;
    }

    public Long getCurso() {
        return curso;
    }

    public void setCurso(Long curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MallaCursoId that = (MallaCursoId) o;
        return Objects.equals(malla, that.malla) && Objects.equals(curso, that.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(malla, curso);
    }
}
