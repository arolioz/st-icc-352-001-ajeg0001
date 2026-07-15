package edu.pucmm.eict.P2.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String nombre;
    private String password;
    private String email;
    private Date fechaNacimiento;
    private String institucion;

    public Usuario(String usuario, String nombre, String password){
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
    }

    public Usuario() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario () {return usuario;}

    public String getNombre () {return nombre;}

    public String getPassword () {return password;}

    public void setUsuario (String s) {this.usuario = s;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setPassword(String password) {this.password = password;}
}
