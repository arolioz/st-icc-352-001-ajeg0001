package Pucmm.eict.edu.grpc;

import Pucmm.eict.edu.Entidades.Encuesta;
import Pucmm.eict.edu.Services.EncuestaServices;
import io.grpc.stub.StreamObserver;
import org.bson.types.ObjectId;

public class EncuestaGrpcImpl extends EncuestaServiceGrpc.EncuestaServiceImplBase {

    private static final String USUARIO_PRUEBA = "6a877d7a6e2fd0198d27cf06";

    @Override
    public void crearEncuesta(CrearEncuestaRequest req,
                              StreamObserver<EncuestaResponse> respuesta) {
        try {
            Encuesta e = new Encuesta(
                    req.getUuid(),
                    req.getNombre(),
                    req.getSector(),
                    null,
                    req.getLatitud(),
                    req.getLongitud(),
                    req.getFotoBase64(),
                    new ObjectId(USUARIO_PRUEBA),
                    "admin"
            );

            EncuestaServices.Resultado r = EncuestaServices.getInstancia().crear(e);

            respuesta.onNext(EncuestaResponse.newBuilder()
                    .setId(r.encuesta().getId().toHexString())
                    .setUuid(r.encuesta().getUuid())
                    .setNombre(r.encuesta().getNombre())
                    .setEstado(r.yaExistia() ? "ya_existia" : "creada")
                    .build());
            respuesta.onCompleted();

        } catch (Exception ex) {
            respuesta.onError(io.grpc.Status.INTERNAL
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        }

    }

    @Override
    public void listarPorUsuario(ListarPorUsuarioRequest req,
                                 StreamObserver<ListaEncuestasResponse> respuesta) {
        respuesta.onNext(ListaEncuestasResponse.newBuilder().build());
        respuesta.onCompleted();
    }
}