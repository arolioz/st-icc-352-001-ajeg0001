package edu.pucmm.eict.P2.Entidades;

import jakarta.persistence.*;
import jdk.jfr.Name;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_id", "evento_id"})
        }
)
@NamedQueries({
        @NamedQuery(
                name = "EventoUsuario.findUsuarioEnEvento",
                query = "SELECT eu FROM EventoUsuario eu WHERE eu.usuario.id = :idUsuario AND eu.evento.id = :idEvento"
        ),
        @NamedQuery(
                name = "EventoUsuario.findRegistroToken",
                query = "SELECT eu FROM EventoUsuario eu WHERE eu.token = :token"
        )
})
public class EventoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Evento evento;

    private String token;
    private Boolean asistencia;
    private LocalDateTime fechaAsistencia;

    public EventoUsuario(){
        this.asistencia = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario () {return usuario;}

    public Evento getEvento () {return evento;}

    public void setUsuario (Usuario u) {this.usuario = u;}

    public void setEvento(Evento event) {this.evento = event;}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

    public LocalDateTime getFechaAsistencia() {
        return fechaAsistencia;
    }

    public void setFechaAsistencia(LocalDateTime fechaAsistencia) {
        this.fechaAsistencia = fechaAsistencia;
    }
}
