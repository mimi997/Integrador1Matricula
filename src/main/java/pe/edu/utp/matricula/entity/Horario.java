package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Entidad que representa un horario asignado a un curso y docente.
 */
@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String dia;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String horas;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String aula;

    public Horario() {
    }

    public Horario(Curso curso, Docente docente, String dia, String horas, String aula) {
        this.curso = curso;
        this.docente = docente;
        this.dia = dia;
        this.horas = horas;
        this.aula = aula;
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

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horario horario = (Horario) o;
        return Objects.equals(id, horario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
