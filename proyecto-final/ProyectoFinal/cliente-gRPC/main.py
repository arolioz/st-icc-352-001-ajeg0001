import grpc
import uuid
import encuesta_pb2
import encuesta_pb2_grpc
import requests

canal = grpc.insecure_channel('localhost:9090')
stub = encuesta_pb2_grpc.EncuestaServiceStub(canal)

# --- crear ---
peticion = encuesta_pb2.CrearEncuestaRequest(
    uuid=str(uuid.uuid4()),
    nombre='TEST3',
    sector='Cerros de Gurabo',
    nivel_escolar="Medio",
    latitud=19.45,
    longitud=-70.69,

)

respuesta = stub.CrearEncuesta(peticion)
print('CREAR ->', respuesta)

# --- listar ---
lista = stub.ListarPorUsuario(
    encuesta_pb2.ListarPorUsuarioRequest(usuario_id='000000000000000000000001')
)
print('LISTAR ->', len(lista.encuestas), 'encuestas')
for e in lista.encuestas:
    print('  -', e.nombre, '|', e.sector)
