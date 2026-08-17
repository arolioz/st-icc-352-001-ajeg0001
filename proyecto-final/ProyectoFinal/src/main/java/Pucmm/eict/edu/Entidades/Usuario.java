package Pucmm.eict.edu.Entidades;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Objects;

@Entity
public class Usuario {
    @Id
    private ObjectId id;
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

    public ObjectId getId(){
        return this.id;
    }

}
