package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entidad que representa a un estudiante.
 */
@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @NotBlank
    @Column(name = "codigo_estudiante", nullable = false, unique = true, length = 20)
    private String codigoEstudiante;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String carrera;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer ciclo;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer creditos = 0;

    public Estudiante() {
    }

    public Estudiante(Usuario usuario, String codigoEstudiante, String carrera, Integer ciclo, Integer creditos) {
        this.usuario = usuario;
        this.codigoEstudiante = codigoEstudiante;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.creditos = creditos;
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

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(codigoEstudiante, that.codigoEstudiante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoEstudiante);
    }
}
