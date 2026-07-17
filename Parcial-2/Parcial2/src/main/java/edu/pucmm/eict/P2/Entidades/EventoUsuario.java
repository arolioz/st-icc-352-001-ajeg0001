package edu.pucmm.eict.P2.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EventoUsuario {
    @Id
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Evento evento;



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
}
