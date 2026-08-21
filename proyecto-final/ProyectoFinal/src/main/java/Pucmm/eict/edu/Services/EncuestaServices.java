package Pucmm.eict.edu.Services;

import Pucmm.eict.edu.Entidades.Encuesta;
import Pucmm.eict.edu.Entidades.Usuario;
import Pucmm.eict.edu.config.DbConfig;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.List;

public class EncuestaServices {
    private static EncuestaServices instancia;
    public record Resultado(Encuesta encuesta, boolean yaExistia) {}

    public EncuestaServices() {}

    public static EncuestaServices getInstancia(){
        if (instancia == null){
            instancia = new EncuestaServices();
        }
        return instancia;
    }

    public Resultado crear(Encuesta e) {
        if (e.getUuid() == null || e.getUuid().isBlank()) {
            throw new IllegalArgumentException("El uuid es obligatorio");
        }

        Datastore ds = DbConfig.getDatastore();

        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(Instant.now());
        }

        try {
            return new Resultado(ds.save(e), false);

        } catch (MongoWriteException ex) {
            if (ex.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {

                Encuesta existente = ds.find(Encuesta.class)
                        .filter(Filters.eq("uuid", e.getUuid()))
                        .first();
                return new Resultado(existente, true);
            }
            throw ex;
        }
    }

    public List<Encuesta> listarEncuestas() throws Exception {

        Datastore ds = DbConfig.getDatastore();

        List<Encuesta> encuestas = ds.find(Encuesta.class).iterator().toList();

        IO.println("ELIMINAR DEBUG DESPUES -> Encuesta: ");
        for (Encuesta e : encuestas){
            IO.println(e.toString());
        }

        return encuestas;
    }

    public Encuesta obtenerEncuesta(ObjectId id){
        Datastore ds = DbConfig.getDatastore();

        Encuesta e = ds.find(Encuesta.class).filter(Filters.eq("_id", id)).first();

        assert e != null;
        IO.println(e.toString());

        return e;
    }
    public void eliminarEncuesta(ObjectId id){
        Datastore ds = DbConfig.getDatastore();

        Encuesta e = ds.find(Encuesta.class).filter(Filters.eq("_id", id)).first();

        if (e == null){
            IO.println("Encuesta no encontrado");
            return;
        }

        ds.find(Encuesta.class).filter(Filters.eq("_id", id)).delete();

        IO.println("Encuesta eliminada" + e.toString());

    }



    public Encuesta modificarEncuesta(ObjectId id, Encuesta nuevaEncuesta) {
        Datastore ds = DbConfig.getDatastore();

        Encuesta e = ds.find(Encuesta.class).filter(Filters.eq("_id", id)).first();
        if (e == null) {
            throw new IllegalArgumentException("Encuesta no encontrada");
        }

        e.setNombre(nuevaEncuesta.getNombre());
        e.setNivelEscolar(nuevaEncuesta.getNivelEscolar());
        e.setSector(nuevaEncuesta.getSector());

        ds.save(e);

        return e;
    }

    public List<Encuesta> listarPorUsuario(ObjectId usuarioId) {
        return DbConfig.getDatastore()
                .find(Encuesta.class)
                .filter(Filters.eq("usuarioId", usuarioId))
                .iterator()
                .toList();
    }
}
