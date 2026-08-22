
"""
Cliente gRPC de escritorio - Requerimiento 19
Proyecto Final ICC-352 - PUCMM
 
Ejecutar:  python main.py
Requiere:  pip install grpcio PySide6
"""

import base64
import sys
import uuid

import grpc
from PySide6.QtCore import Qt, QBuffer, QByteArray, QIODevice
from PySide6.QtGui import QImage, QPixmap
from PySide6.QtWidgets import (
    QApplication, QComboBox, QDoubleSpinBox, QFileDialog, QFormLayout,
    QGroupBox, QHBoxLayout, QHeaderView, QLabel, QLineEdit, QMainWindow,
    QMessageBox, QPushButton, QTableWidget, QTableWidgetItem, QVBoxLayout,
    QWidget
)

import encuesta_pb2
import encuesta_pb2_grpc

SERVIDOR_POR_DEFECTO = "localhost:9090"

# id fijo del usuario de servicio creado en DbService
USUARIO_GRPC = "000000000000000000000001"

ANCHO_MAXIMO_FOTO = 800
CALIDAD_JPEG = 70

ESTILO = """
* { color: #1f2933; }
 
QMainWindow, QWidget { background: #eef1f5; }
 
QGroupBox {
    background: #ffffff;
    border: 1px solid #d3dae2;
    border-radius: 8px;
    margin-top: 14px;
    padding: 16px 14px 14px 14px;
    font-weight: 600;
    font-size: 13px;
}
QGroupBox::title {
    subcontrol-origin: margin;
    left: 12px;
    padding: 0 6px;
    color: #2563eb;
}
 
QLabel { color: #3e4c59; font-size: 13px; }
 
QLineEdit, QComboBox, QDoubleSpinBox {
    color: #1f2933;
    background: #ffffff;
    border: 1px solid #c2cbd6;
    border-radius: 5px;
    padding: 6px 9px;
    font-size: 13px;
    selection-background-color: #2563eb;
    selection-color: #ffffff;
}
QLineEdit:focus, QComboBox:focus, QDoubleSpinBox:focus {
    border: 1px solid #2563eb;
}
 
QComboBox QAbstractItemView {
    color: #1f2933;
    background: #ffffff;
    selection-background-color: #2563eb;
    selection-color: #ffffff;
}
 
QPushButton {
    background: #2563eb;
    color: #ffffff;
    border: none;
    border-radius: 5px;
    padding: 8px 18px;
    font-weight: 600;
    font-size: 13px;
}
QPushButton:hover   { background: #1d4ed8; }
QPushButton:pressed { background: #1e40af; }
QPushButton:disabled{ background: #b6c0cb; color: #eef1f5; }
 
QPushButton#secundario {
    background: #ffffff;
    color: #2563eb;
    border: 1px solid #2563eb;
}
QPushButton#secundario:hover { background: #eff4ff; }
 
QTableWidget {
    color: #1f2933;
    background: #ffffff;
    border: 1px solid #d3dae2;
    border-radius: 6px;
    gridline-color: #e6ebf0;
    font-size: 13px;
}
QTableWidget::item:selected { background: #dbe7ff; color: #1f2933; }
 
QHeaderView::section {
    background: #f3f6f9;
    color: #3e4c59;
    border: none;
    border-bottom: 1px solid #d3dae2;
    padding: 8px 6px;
    font-weight: 600;
}
"""


class ClienteGrpc(QMainWindow):

    def __init__(self):
        super().__init__()
        self.setWindowTitle("Cliente gRPC — Encuestas OP")
        self.resize(1000, 640)
        self.setStyleSheet(ESTILO)

        self._canal = None
        self._stub = None
        self._direccion_actual = None

        self.foto_base64 = ""

        self._construir()

    # ==============================================================
    # Interfaz
    # ==============================================================
    def _construir(self):
        central = QWidget()
        raiz = QVBoxLayout(central)
        raiz.setSpacing(12)
        raiz.setContentsMargins(14, 14, 14, 14)

        raiz.addWidget(self._grupo_servidor())

        cuerpo = QHBoxLayout()
        cuerpo.setSpacing(12)
        cuerpo.addWidget(self._grupo_crear(), 1)
        cuerpo.addWidget(self._grupo_listar(), 1)
        raiz.addLayout(cuerpo)

        self.estado = QLabel("Listo")
        self.estado.setStyleSheet("color:#6b7885; padding:4px 2px;")
        raiz.addWidget(self.estado)

        self.setCentralWidget(central)

    def _grupo_servidor(self):
        caja = QGroupBox("Servidor gRPC")
        fila = QHBoxLayout(caja)

        self.txt_servidor = QLineEdit(SERVIDOR_POR_DEFECTO)
        self.txt_servidor.setPlaceholderText("host:puerto")

        fila.addWidget(QLabel("Direccion:"))
        fila.addWidget(self.txt_servidor)

        return caja

    def _grupo_crear(self):
        caja = QGroupBox("Crear encuesta  ·  CrearEncuesta")
        form = QFormLayout(caja)
        form.setSpacing(8)

        self.txt_nombre = QLineEdit()
        self.txt_sector = QLineEdit()

        self.cbo_nivel = QComboBox()
        self.cbo_nivel.addItem("Test")

        self.spn_lat = QDoubleSpinBox()
        self.spn_lat.setRange(-90, 90)
        self.spn_lat.setDecimals(6)
        self.spn_lat.setValue(19.451700)

        self.spn_lng = QDoubleSpinBox()
        self.spn_lng.setRange(-180, 180)
        self.spn_lng.setDecimals(6)
        self.spn_lng.setValue(-70.697000)

        btn_foto = QPushButton("Seleccionar foto...")
        btn_foto.setObjectName("secundario")
        btn_foto.clicked.connect(self.elegir_foto)

        self.lbl_foto = QLabel("Sin foto")
        self.lbl_foto.setFixedHeight(110)
        self.lbl_foto.setAlignment(Qt.AlignCenter)
        self.lbl_foto.setStyleSheet(
            "border:1px dashed #c2cbd6; border-radius:5px;"
            "color:#9aa5b1; background:#fafbfc;")

        btn_crear = QPushButton("Enviar encuesta")
        btn_crear.clicked.connect(self.crear_encuesta)

        form.addRow("Nombre:", self.txt_nombre)
        form.addRow("Sector:", self.txt_sector)
        form.addRow("Nivel escolar:", self.cbo_nivel)
        form.addRow("Latitud:", self.spn_lat)
        form.addRow("Longitud:", self.spn_lng)
        form.addRow(btn_foto)
        form.addRow(self.lbl_foto)
        form.addRow(btn_crear)

        return caja

    def _grupo_listar(self):
        caja = QGroupBox("Encuestas del usuario  ·  ListarPorUsuario")
        col = QVBoxLayout(caja)
        col.setSpacing(8)

        fila = QHBoxLayout()
        self.txt_usuario_id = QLineEdit(USUARIO_GRPC)
        self.txt_usuario_id.setPlaceholderText("usuario_id (24 caracteres hex)")

        btn_listar = QPushButton("Listar")
        btn_listar.clicked.connect(self.listar)

        fila.addWidget(self.txt_usuario_id)
        fila.addWidget(btn_listar)
        col.addLayout(fila)

        self.tabla = QTableWidget(0, 4)
        self.tabla.setHorizontalHeaderLabels(["Nombre", "Sector", "Nivel", "UUID"])
        self.tabla.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.tabla.verticalHeader().setVisible(False)
        self.tabla.setEditTriggers(QTableWidget.NoEditTriggers)
        col.addWidget(self.tabla)

        return caja

    # ==============================================================
    # Conexion perezosa: el canal se crea al primer uso y se reutiliza
    # ==============================================================
    def stub(self):
        direccion = self.txt_servidor.text().strip() or SERVIDOR_POR_DEFECTO

        if self._stub is None or direccion != self._direccion_actual:
            if self._canal is not None:
                self._canal.close()

            self._canal = grpc.insecure_channel(direccion)
            self._stub = encuesta_pb2_grpc.EncuestaServiceStub(self._canal)
            self._direccion_actual = direccion

        return self._stub

    # ==============================================================
    # Foto
    # ==============================================================
    def elegir_foto(self):
        ruta, _ = QFileDialog.getOpenFileName(
            self, "Seleccionar foto", "", "Imagenes (*.png *.jpg *.jpeg *.bmp)")

        if not ruta:
            return

        imagen = QImage(ruta)
        if imagen.isNull():
            self.error("Imagen invalida", "No se pudo leer el archivo.")
            return

        # Se reduce antes de codificar: una foto de camara sin procesar puede
        # pesar 4 MB, y base64 le suma otro 33%.
        if imagen.width() > ANCHO_MAXIMO_FOTO:
            imagen = imagen.scaledToWidth(ANCHO_MAXIMO_FOTO, Qt.SmoothTransformation)

        bytes_jpeg = QByteArray()
        buffer = QBuffer(bytes_jpeg)
        buffer.open(QIODevice.WriteOnly)
        imagen.save(buffer, "JPEG", CALIDAD_JPEG)
        buffer.close()

        self.foto_base64 = base64.b64encode(bytes_jpeg.data()).decode()

        self.lbl_foto.setPixmap(
            QPixmap.fromImage(imagen).scaledToHeight(105, Qt.SmoothTransformation))

        self.informar(f"Foto lista: {len(self.foto_base64) / 1024:.0f} KB en base 64")

    # ==============================================================
    # Operaciones gRPC
    # ==============================================================
    def crear_encuesta(self):
        if not self.txt_nombre.text().strip():
            self.error("Falta el nombre", "El nombre es obligatorio.")
            return

        peticion = encuesta_pb2.CrearEncuestaRequest(
            uuid=str(uuid.uuid4()),
            nombre=self.txt_nombre.text().strip(),
            sector=self.txt_sector.text().strip(),
            nivel_escolar=(self.cbo_nivel.currentText()),
            latitud=self.spn_lat.value(),
            longitud=self.spn_lng.value(),
            foto_base64=self.foto_base64,
        )

        try:
            respuesta = self.stub().CrearEncuesta(peticion, timeout=15)

            QMessageBox.information(
                self, "Encuesta enviada",
                f"Estado: {respuesta.estado}\nUUID: {respuesta.uuid}\nId: {respuesta.id}")

            self.informar(f"Creada ({respuesta.estado})")
            self.limpiar_formulario()

        except grpc.RpcError as ex:
            self.error_grpc(ex)

    def listar(self):
        peticion = encuesta_pb2.ListarPorUsuarioRequest(
            usuario_id=self.txt_usuario_id.text().strip())

        try:
            respuesta = self.stub().ListarPorUsuario(peticion, timeout=15)

            self.tabla.setRowCount(0)
            for e in respuesta.encuestas:
                fila = self.tabla.rowCount()
                self.tabla.insertRow(fila)
                self.tabla.setItem(fila, 0, QTableWidgetItem(e.nombre))
                self.tabla.setItem(fila, 1, QTableWidgetItem(e.sector))
                self.tabla.setItem(
                    fila, 2,
                    QTableWidgetItem(encuesta_pb2.NivelEscolar.Name(e.nivel_escolar)))
                self.tabla.setItem(fila, 3, QTableWidgetItem(e.uuid))

            self.informar(f"{len(respuesta.encuestas)} encuestas recibidas")

        except grpc.RpcError as ex:
            self.error_grpc(ex)

    # ==============================================================
    # Utilidades
    # ==============================================================
    def limpiar_formulario(self):
        self.txt_nombre.clear()
        self.txt_sector.clear()
        self.foto_base64 = ""
        self.lbl_foto.clear()
        self.lbl_foto.setText("Sin foto")

    def informar(self, texto):
        self.estado.setText(texto)

    def error(self, titulo, detalle):
        QMessageBox.warning(self, titulo, detalle)
        self.informar(titulo)

    def error_grpc(self, ex):
        """Traduce los codigos de gRPC a algo que el usuario entienda."""
        codigo = ex.code()

        mensajes = {
            grpc.StatusCode.UNAVAILABLE: "No hay conexion con el servidor. Revise la direccion.",
            grpc.StatusCode.INVALID_ARGUMENT: "Datos invalidos.",
            grpc.StatusCode.UNAUTHENTICATED: "El servidor exige autenticacion.",
            grpc.StatusCode.PERMISSION_DENIED: "Sin permisos para esta operacion.",
            grpc.StatusCode.DEADLINE_EXCEEDED: "El servidor no respondio a tiempo.",
        }

        self.error(codigo.name,
                   f"{mensajes.get(codigo, 'Error del servidor.')}\n\n{ex.details()}")


if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")          # ignora el tema oscuro del sistema
    ventana = ClienteGrpc()
    ventana.show()
    sys.exit(app.exec())