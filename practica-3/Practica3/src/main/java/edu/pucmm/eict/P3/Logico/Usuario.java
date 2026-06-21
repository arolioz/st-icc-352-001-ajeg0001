package edu.pucmm.eict.P3.Logico;

public class Usuario {

    private String usuario;
    private String nombre;
    private String password;

    public Usuario(String usuario, String nombre, String password){
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
    }

    public String getUsuario () {return usuario;}

    public String getNombre () {return nombre;}

    public String getPassword () {return password;}

    public void setUsuario (String s) {this.usuario = s;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setPassword(String password) {this.password = password;}

}
