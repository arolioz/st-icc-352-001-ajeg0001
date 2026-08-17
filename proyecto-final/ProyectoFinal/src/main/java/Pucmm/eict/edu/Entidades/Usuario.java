package Pucmm.eict.edu.Entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.util.Objects;

@Entity
public class Usuario {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String user;
    private String password;

    public Usuario(){

    }

    public Usuario(String user, String password){
        this.user = user;
        this.password = password;
    }

    public Usuario(String user, String password, ObjectId id){
        this.user = user;
        this.password = password;
        this.id = id;
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

}
