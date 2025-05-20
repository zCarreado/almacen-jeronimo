from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy

# Crear la app Flask
app = Flask(__name__)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///productos.db'  # Usamos SQLite como base de datos local
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False  # Desactivar el seguimiento de modificaciones para evitar advertencias

# Crear instancia de SQLAlchemy
db = SQLAlchemy(app)

# Definir el modelo de Producto
class Producto(db.Model):
    cedula = db.Column(db.String(80), primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    precio = db.Column(db.Float, nullable=False)
    stock = db.Column(db.Integer, nullable=False)

    def __repr__(self):
        return f'<Producto {self.nombre}>'

# Crear las tablas en la base de datos
with app.app_context():
    db.create_all()

# Ruta para obtener todos los productos
@app.route('/api/productos/obtenerProductos', methods=['GET'])
def obtener_productos():
    productos = Producto.query.all()  # Obtener todos los productos de la base de datos
    return jsonify([{
        "cedula": p.cedula,
        "nombre": p.nombre,
        "precio": p.precio,
        "stock": p.stock
    } for p in productos])

# Ruta para obtener un producto por cédula
@app.route('/api/productos/obtenerProducto/<cedula>', methods=['GET'])
def obtener_producto(cedula):
    producto = Producto.query.filter_by(cedula=cedula).first()  # Buscar por cédula
    if producto:
        return jsonify({
            "cedula": producto.cedula,
            "nombre": producto.nombre,
            "precio": producto.precio,
            "stock": producto.stock
        })
    return jsonify({"error": "Producto no encontrado"}), 404

# Ruta para actualizar el stock del producto
@app.route('/api/productos/actualizarStock/<cedula>', methods=['PUT'])
def actualizar_stock(cedula):
    producto = Producto.query.filter_by(cedula=cedula).first()  # Buscar por cédula
    if producto:
        data = request.get_json()
        producto.stock -= data.get("cantidad_vendida", 0)  # Restar la cantidad vendida del stock
        db.session.commit()  # Confirmar los cambios en la base de datos
        return jsonify({
            "cedula": producto.cedula,
            "nombre": producto.nombre,
            "stock": producto.stock
        })
    return jsonify({"error": "Producto no encontrado"}), 404

# Ejecutar la app
if __name__ == "__main__":
    app.run(debug=True, port=5001)
