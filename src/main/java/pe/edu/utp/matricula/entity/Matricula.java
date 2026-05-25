package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Entidad que representa una matrícula de un estudiante en un periodo.
 */
@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String periodo;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String estado;

    @Column(length = 255)
    private String comprobante;

    public Matricula() {
    }

    public Matricula(Estudiante estudiante, String periodo, String estado, String comprobante) {
        this.estudiante = estudiante;
        this.periodo = periodo;
        this.estado = estado;
        this.comprobante = comprobante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return Objects.equals(id, matricula.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
