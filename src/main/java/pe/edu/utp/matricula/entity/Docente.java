package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Entidad que representa a un docente.
 */
@Entity
@Table(name = "docente")
public class Docente {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @NotBlank
    @Column(name = "codigo_docente", nullable = false, unique = true, length = 20)
    private String codigoDocente;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String especialidad;

    public Docente() {
    }

    public Docente(Usuario usuario, String codigoDocente, String especialidad) {
        this.usuario = usuario;
        this.codigoDocente = codigoDocente;
        this.especialidad = especialidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCodigoDocente() {
        return codigoDocente;
    }

    public void setCodigoDocente(String codigoDocente) {
        this.codigoDocente = codigoDocente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return Objects.equals(codigoDocente, docente.codigoDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoDocente);
    }
}
