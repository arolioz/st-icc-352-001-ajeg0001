package edu.pucmm.eict.P3.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Usuario.findByUsuario",
                query = "SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.password = :password"
        )
})
public class Usuario {


    @Id
    private Long id;
    private String usuario;
    private String nombre;
    private String password;

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
