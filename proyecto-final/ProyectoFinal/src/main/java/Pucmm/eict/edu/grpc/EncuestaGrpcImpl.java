package Pucmm.eict.edu.grpc;

import io.grpc.stub.StreamObserver;

public class EncuestaGrpcImpl extends EncuestaServiceGrpc.EncuestaServiceImplBase {

    @Override
    public void crearEncuesta(CrearEncuestaRequest req,
                              StreamObserver<EncuestaResponse> respuesta) {
        System.out.println("[gRPC] Llego: " + req.getNombre());

        EncuestaResponse r = EncuestaResponse.newBuilder()
                .setUuid(req.getUuid())
                .setNombre(req.getNombre())
                .setEstado("creada")
                .build();

        respuesta.onNext(r);
        respuesta.onCompleted();
    }

    @Override
    public void listarPorUsuario(ListarPorUsuarioRequest req,
                                 StreamObserver<ListaEncuestasResponse> respuesta) {
        respuesta.onNext(ListaEncuestasResponse.newBuilder().build());
        respuesta.onCompleted();
    }
}