package Pucmm.eict.edu.grpc;

import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GrpcServidor {
    public static void iniciar(int puerto) throws IOException {
        ServerBuilder.forPort(puerto)
                .addService(new EncuestaGrpcImpl())
                .addService(ProtoReflectionService.newInstance())  // para probar con Postman
                .build()
                .start();

        System.out.println("[gRPC] Escuchando en el puerto " + puerto);
    }
}
