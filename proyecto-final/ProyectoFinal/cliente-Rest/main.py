"""
Cliente REST de escritorio - Requerimiento 18
Proyecto Final ICC-352 - PUCMM

Ejecutar:  python cliente_rest.py
Requiere:  pip install PySide6      (el HTTP sale de la libreria estandar)
"""

import base64
import json
import sys
import urllib.error
import urllib.request
import uuid
from datetime import datetime

from PySide6.QtCore import Qt, QBuffer, QByteArray, QIODevice
from PySide6.QtGui import QImage, QPixmap
from PySide6.QtWidgets import (
    QApplication, QComboBox, QDoubleSpinBox, QFileDialog, QFormLayout,
    QGroupBox, QHBoxLayout, QHeaderView, QLabel, QLineEdit, QMainWindow,
    QMessageBox, QPushButton, QTableWidget, QTableWidgetItem, QVBoxLayout,
    QWidget
)

SERVIDOR_POR_DEFECTO = "http://localhost:7000"

NIVELES = ["BASICO", "MEDIO", "GRADO_UNIVERSITARIO", "POSTGRADO", "DOCTORADO"]

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
    color: #059669;
}

QLabel { color: #3e4c59; font-size: 13px; }

QLineEdit, QComboBox, QDoubleSpinBox {
    color: #1f2933;
    background: #ffffff;
    border: 1px solid #c2cbd6;
    border-radius: 5px;
    padding: 6px 9px;
    font-size: 13px;
    selection-background-color: #059669;
    selection-color: #ffffff;
}
QLineEdit:focus, QComboBox:focus, QDoubleSpinBox:focus { border: 1px solid #059669; }

QComboBox QAbstractItemView {
    color: #1f2933;
    background: #ffffff;
    selection-background-color: #059669;
    selection-color: #ffffff;
}

QPushButton {
    background: #059669;
    color: #ffffff;
    border: none;
    border-radius: 5px;
    padding: 8px 18px;
    font-weight: 600;
    font-size: 13px;
}
QPushButton:hover   { background: #047857; }
QPushButton:pressed { background: #065f46; }
QPushButton:disabled{ background: #b6c0cb; color: #eef1f5; }

QPushButton#secundario {
    background: #ffffff;
    color: #059669;
    border: 1px solid #059669;
}
QPushButton#secundario:hover { background: #ecfdf5; }

QTableWidget {
    color: #1f2933;
    background: #ffffff;
    border: 1px solid #d3dae2;
    border-radius: 6px;
    gridline-color: #e6ebf0;
    font-size: 13px;
}
QTableWidget::item:selected { background: #d1fae5; color: #1f2933; }

QHeaderView::section {
    background: #f3f6f9;
    color: #3e4c59;
    border: none;
    border-bottom: 1px solid #d3dae2;
    padding: 8px 6px;
    font-weight: 600;
}
"""


class ErrorApi(Exception):
    def __init__(self, codigo, mensaje):
        super().__init__(mensaje)
        self.codigo = codigo
        self.mensaje = mensaje


class ClienteRest(QMainWindow):

    def __init__(self):
        super().__init__()
        self.setWindowTitle("Cliente REST — Encuestas OP")
        self.resize(1020, 700)
        self.setStyleSheet(ESTILO)

        self.token = ""
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

        raiz.addWidget(self._grupo_sesion())

        cuerpo = QHBoxLayout()
        cuerpo.setSpacing(12)
        cuerpo.addWidget(self._grupo_crear(), 1)
        cuerpo.addWidget(self._grupo_listar(), 1)
        raiz.addLayout(cuerpo)

        self.estado = QLabel("Sin sesion")
        self.estado.setStyleSheet("color:#6b7885; padding:4px 2px;")
        raiz.addWidget(self.estado)

        self.setCentralWidget(central)

    def _grupo_sesion(self):
        caja = QGroupBox("Sesion  ·  POST /api/login")
        fila = QHBoxLayout(caja)

        self.txt_servidor = QLineEdit(SERVIDOR_POR_DEFECTO)
        self.txt_usuario = QLineEdit("admin")
        self.txt_clave = QLineEdit("admin")
        self.txt_clave.setEchoMode(QLineEdit.Password)

        btn_entrar = QPushButton("Entrar")
        btn_entrar.clicked.connect(self.entrar)

        btn_salir = QPushButton("Salir")
        btn_salir.setObjectName("secundario")
        btn_salir.clicked.connect(self.salir)

        fila.addWidget(QLabel("Servidor:"))
        fila.addWidget(self.txt_servidor, 3)
        fila.addWidget(QLabel("Usuario:"))
        fila.addWidget(self.txt_usuario, 2)
        fila.addWidget(self.txt_clave, 2)
        fila.addWidget(btn_entrar)
        fila.addWidget(btn_salir)

        return caja

    def _grupo_crear(self):
        caja = QGroupBox("Crear encuesta  ·  POST /api/encuesta")
        form = QFormLayout(caja)
        form.setSpacing(8)

        self.txt_nombre = QLineEdit()
        self.txt_sector = QLineEdit()

        self.cbo_nivel = QComboBox()
        self.cbo_nivel.addItems(NIVELES)

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

        self.btn_crear = QPushButton("Enviar encuesta")
        self.btn_crear.clicked.connect(self.crear_encuesta)

        form.addRow("Nombre:", self.txt_nombre)
        form.addRow("Sector:", self.txt_sector)
        form.addRow("Nivel escolar:", self.cbo_nivel)
        form.addRow("Latitud:", self.spn_lat)
        form.addRow("Longitud:", self.spn_lng)
        form.addRow(btn_foto)
        form.addRow(self.lbl_foto)
        form.addRow(self.btn_crear)

        return caja

    def _grupo_listar(self):
        caja = QGroupBox("Encuestas del usuario  ·  GET /api/encuesta/usuario/{id}")
        col = QVBoxLayout(caja)
        col.setSpacing(8)

        fila = QHBoxLayout()
        self.txt_usuario_id = QLineEdit()
        self.txt_usuario_id.setPlaceholderText("Se rellena al entrar")

        btn_listar = QPushButton("Listar")
        btn_listar.clicked.connect(self.listar)

        fila.addWidget(self.txt_usuario_id)
        fila.addWidget(btn_listar)
        col.addLayout(fila)

        self.tabla = QTableWidget(0, 4)
        self.tabla.setHorizontalHeaderLabels(["Nombre", "Sector", "Nivel", "Fecha"])
        self.tabla.horizontalHeader().setSectionResizeMode(QHeaderView.Stretch)
        self.tabla.verticalHeader().setVisible(False)
        self.tabla.setEditTriggers(QTableWidget.NoEditTriggers)
        col.addWidget(self.tabla)

        return caja

    # ==============================================================
    # HTTP: una sola puerta para todas las llamadas
    # ==============================================================
    def peticion(self, ruta, metodo="GET", cuerpo=None):
        url = self.txt_servidor.text().strip().rstrip("/") + ruta

        cabeceras = {"Accept": "application/json"}
        if cuerpo is not None:
            cabeceras["Content-Type"] = "application/json"

        # el token va en el header en cada llamada, igual que en la web
        if self.token:
            cabeceras["Authorization"] = f"Bearer {self.token}"

        datos = json.dumps(cuerpo).encode() if cuerpo is not None else None
        req = urllib.request.Request(url, data=datos, headers=cabeceras, method=metodo)

        try:
            with urllib.request.urlopen(req, timeout=20) as res:
                texto = res.read().decode()
                return json.loads(texto) if texto.strip() else None

        except urllib.error.HTTPError as ex:
            detalle = ex.read().decode(errors="ignore")

            if ex.code == 401:
                self.token = ""
                self.informar("Sesion expirada o token invalido")
                raise ErrorApi(401, "La sesion expiro. Vuelva a entrar.")

            if ex.code == 403:
                raise ErrorApi(403, "No tiene permisos para esta operacion.")

            raise ErrorApi(ex.code, detalle or f"Error {ex.code}")

        except urllib.error.URLError as ex:
            raise ErrorApi(0, f"No hay conexion con el servidor.\n{ex.reason}")

    # ==============================================================
    # Sesion
    # ==============================================================
    def entrar(self):
        try:
            datos = self.peticion("/api/login", "POST", {
                "usuario": self.txt_usuario.text().strip(),
                "password": self.txt_clave.text()
            })

            self.token = datos.get("token", "")
            usuario = datos.get("usuario", {})

            if usuario.get("id"):
                self.txt_usuario_id.setText(usuario["id"])

            roles = ", ".join(usuario.get("roles", []))
            self.informar(f"Sesion activa: {usuario.get('usuario', '?')} · {roles}")

        except ErrorApi as ex:
            if ex.codigo == 401:
                self.error("Login rechazado", "Usuario o contrasena incorrectos.")
            else:
                self.error("No se pudo entrar", ex.mensaje)

    def salir(self):
        self.token = ""
        self.txt_usuario_id.clear()
        self.tabla.setRowCount(0)
        self.informar("Sin sesion")

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
    # Operaciones
    # ==============================================================
    def crear_encuesta(self):
        if not self.token:
            self.error("Sin sesion", "Debe entrar primero.")
            return

        if not self.txt_nombre.text().strip():
            self.error("Falta el nombre", "El nombre es obligatorio.")
            return

        encuesta = {
            "uuid": str(uuid.uuid4()),          # idempotencia
            "nombre": self.txt_nombre.text().strip(),
            "sector": self.txt_sector.text().strip(),
            "nivelEscolar": self.cbo_nivel.currentText(),
            "latitud": self.spn_lat.value(),
            "longitud": self.spn_lng.value(),
            "fotoBase64": self.foto_base64,
        }

        self.btn_crear.setEnabled(False)
        try:
            datos = self.peticion("/api/encuesta", "POST", encuesta) or {}

            QMessageBox.information(
                self, "Encuesta enviada",
                f"Estado: {datos.get('estado', 'creada')}\nUUID: {encuesta['uuid']}")

            self.limpiar_formulario()
            self.listar()

        except ErrorApi as ex:
            self.error(f"Error {ex.codigo}", ex.mensaje)
        finally:
            self.btn_crear.setEnabled(True)

    def listar(self):
        identificador = self.txt_usuario_id.text().strip()
        if not identificador:
            return

        try:
            encuestas = self.peticion(f"/api/encuesta/usuario/{identificador}") or []

            self.tabla.setRowCount(0)
            for e in encuestas:
                fila = self.tabla.rowCount()
                self.tabla.insertRow(fila)
                self.tabla.setItem(fila, 0, QTableWidgetItem(e.get("nombre") or "—"))
                self.tabla.setItem(fila, 1, QTableWidgetItem(e.get("sector") or "—"))
                self.tabla.setItem(fila, 2, QTableWidgetItem(e.get("nivelEscolar") or "—"))
                self.tabla.setItem(fila, 3, QTableWidgetItem(self.fecha(e.get("fechaRegistro"))))

            self.informar(f"{len(encuestas)} encuestas recibidas")

        except ErrorApi as ex:
            self.error(f"Error {ex.codigo}", ex.mensaje)

    # ==============================================================
    # Utilidades
    # ==============================================================
    @staticmethod
    def fecha(iso):
        if not iso:
            return "—"
        try:
            return datetime.fromisoformat(iso.replace("Z", "+00:00")).strftime("%d/%m/%Y %H:%M")
        except ValueError:
            return iso

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


if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    ventana = ClienteRest()
    ventana.show()
    sys.exit(app.exec())