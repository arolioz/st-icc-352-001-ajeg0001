package Pucmm.eict.edu.Entidades;

import Pucmm.eict.edu.Util.RolesApp;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Set;

@Entity
public class Usuario {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String user;
    private String password;
    Set<RolesApp> listaRoles;

    public Usuario(){

    }

    public Usuario(String user, String password){
        this.user = user;
        this.password = password;
        this.listaRoles = Set.of(RolesApp.ROLE_USUARIO);
    }

    public Usuario(String user, String password, ObjectId id){
        this.user = user;
        this.password = password;
        this.id = id;
        this.listaRoles = Set.of(RolesApp.ROLE_USUARIO);
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getUser() { return user; }
    public void setUser(String usuario) { this.user = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String clave) { this.password = clave; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", usuario='" + this.user + "'}";
    }

    public Set<RolesApp> getListaRoles() {return listaRoles;}

    public void setListaRoles(Set<RolesApp> listaRoles) {this.listaRoles = listaRoles;}

}
