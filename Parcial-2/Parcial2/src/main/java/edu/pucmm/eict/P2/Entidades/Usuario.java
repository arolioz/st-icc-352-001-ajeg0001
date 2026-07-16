package edu.pucmm.eict.P2.Entidades;

import Util.RolesApp;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Usuario.findByUsuarioAndPassword",
                query = "SELECT u FROM Usuario u WHERE LOWER(u.usuario) = LOWER(:usuario) AND u.password = :password"
        ),
        @NamedQuery(
                name = "Usuario.findByUsuario",
                query = "SELECT u FROM Usuario u WHERE LOWER(u.usuario) = LOWER(:usuario)"
        )
})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String nombre;
    private String password;
    private String email;
    private String institucion;
    Set<RolesApp> listaRoles;

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

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getInstitucion() {return institucion;}

    public void setInstitucion(String institucion) {this.institucion = institucion;}

    public Set<RolesApp> getListaRoles() {return listaRoles;}

    public void setListaRoles(Set<RolesApp> listaRoles) {this.listaRoles = listaRoles;}
}
