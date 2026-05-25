package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad que representa la nota final de un curso.
 */
@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_matricula_id", nullable = false)
    private DetalleMatricula detalleMatricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Min(0)
    @Max(20)
    @Column(precision = 4, scale = 2)
    private BigDecimal valor;

    @Column
    private Boolean aprobado;

    public Nota() {
    }

    public Nota(DetalleMatricula detalleMatricula, Docente docente, BigDecimal valor, Boolean aprobado) {
        this.detalleMatricula = detalleMatricula;
        this.docente = docente;
        this.valor = valor;
        this.aprobado = aprobado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetalleMatricula getDetalleMatricula() {
        return detalleMatricula;
    }

    public void setDetalleMatricula(DetalleMatricula detalleMatricula) {
        this.detalleMatricula = detalleMatricula;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return Objects.equals(id, nota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
