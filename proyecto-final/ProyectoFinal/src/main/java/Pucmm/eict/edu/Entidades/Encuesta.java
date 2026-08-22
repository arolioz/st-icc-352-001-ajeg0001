package Pucmm.eict.edu.Entidades;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.time.Instant;

@Entity
public class Encuesta {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private String uuid;

    private String nombre;
    private String sector;
    private String nivelEscolar;

    private Double latitud;
    private Double longitud;

    private String fotoBase64;

    @Indexed
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId usuarioId;
    private String usuarioNombre;

    @JsonSerialize(using = ToStringSerializer.class)
    private Instant fechaRegistro;

    public Encuesta() {}

    public Encuesta(String uuid, String nombre, String sector, String nivelEscolar,
                    Double latitud, Double longitud, String fotoBase64,
                    ObjectId usuarioId, String usuarioNombre) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fotoBase64 = fotoBase64;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.fechaRegistro = Instant.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getNivelEscolar() {
        return nivelEscolar;
    }

    public void setNivelEscolar(String nivelEscolar) {
        this.nivelEscolar = nivelEscolar;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public ObjectId getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(ObjectId usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }



    @Override
    public String toString() {
        return "Encuesta{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", nombre='" + nombre + '\'' +
                ", sector='" + sector + '\'' +
                ", nivelEscolar=" + nivelEscolar +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", usuarioNombre='" + usuarioNombre + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}

