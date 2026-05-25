package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Entidad que representa el detalle de una matrícula (los cursos inscritos).
 */
@Entity
@Table(name = "detalle_matricula")
public class DetalleMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @NotBlank
    @Column(name = "estado_prereq", nullable = false, length = 50)
    private String estadoPrereq;

    public DetalleMatricula() {
    }

    public DetalleMatricula(Matricula matricula, Horario horario, String estadoPrereq) {
        this.matricula = matricula;
        this.horario = horario;
        this.estadoPrereq = estadoPrereq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getEstadoPrereq() {
        return estadoPrereq;
    }

    public void setEstadoPrereq(String estadoPrereq) {
        this.estadoPrereq = estadoPrereq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleMatricula that = (DetalleMatricula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
