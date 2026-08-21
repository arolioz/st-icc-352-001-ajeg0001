package Pucmm.eict.edu.grpc;

import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class GrpcServidor {
    public static void iniciar(int puerto) throws IOException {
        java.util.logging.Logger
                .getLogger("io.grpc.netty.shaded.io.grpc.netty.NettyServerTransport")
                .setLevel(java.util.logging.Level.WARNING);


        ServerBuilder.forPort(puerto)
                .addService(new EncuestaGrpcImpl())
                .addService(ProtoReflectionService.newInstance())  // para probar con Postman
                .build()
                .start();

        System.out.println("[gRPC] Escuchando en el puerto " + puerto);
    }
}
