package edu.pucmm.eict.P2.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Date fecha;
    private LocalTime hora;
    private String lugar;
    private int cupoMaximo;
    private Boolean activo;
    private Long idOrganizador;


    public Evento() {
    }


    public Evento(Long id, String titulo, String descripcion, Date fecha,LocalTime hora ,String lugar, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.cupoMaximo = cupoMaximo;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fechaHora) {
        this.fecha = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public void setActivo(Boolean lugar) {
        this.activo = activo;
    }

    public Boolean getActivo() {return activo;
    }

    public void setHora(LocalTime hora) {this.hora = hora;}

    public LocalTime getHora(){return hora;}

    public void setIdOirganizador(Long idOrganizador) {this.idOrganizador = idOrganizador;}

    public Long getIdOrganizador(){return idOrganizador;}

}
