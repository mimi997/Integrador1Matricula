package pe.edu.utp.matricula.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entidad que representa un curso.
 */
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer creditos;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer ciclo;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String carrera;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer cupos;

    @Column(nullable = false)
    private Boolean activo = true;

    public Curso() {
    }

    public Curso(String codigo, String nombre, Integer creditos, Integer ciclo, String carrera, Integer cupos, Boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.ciclo = ciclo;
        this.carrera = carrera;
        this.cupos = cupos;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(codigo, curso.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
