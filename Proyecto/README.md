# E-commerce API

## Descripción
Esta API proporciona funcionalidades para gestionar un sistema de comercio electrónico, permitiendo la gestión de clientes, productos, órdenes, detalles de órdenes y categorías. Además, implementa autenticación y autorización utilizando **Spring Security** con **JWT**.

## Tecnologías Utilizadas
- **Java 21**
- **Spring Boot 3**
- **Spring Web** (para la creación de los controladores REST)
- **Spring Data JPA** (para la persistencia de datos)
- **Spring Security** (para la autenticación y autorización)
- **JWT (JSON Web Tokens)** (para la gestión de sesiones seguras)
- **Hibernate** (como implementación de JPA)
- **Maven** (para la gestión de dependencias)
- **MySQL** (base de datos en memoria o persistente según configuración)
- **Lombok** (para reducir código boilerplate)

## Endpoints Disponibles
### **Categorías** (`/categories`)
- `POST /` - Crear una nueva categoría.
- `GET /{id}` - Obtener los productos de una categoría.
- `GET /` - Obtener todas las categorías.

### **Clientes** (`/customers`)
- `POST /` - Registrar un nuevo cliente.
- `GET /name/{id}` - Obtener el nombre completo de un cliente.
- `GET /orders/{id}` - Obtener las órdenes de un cliente.
- `GET /{id}` - Obtener información de un cliente.
- `GET /` - Listar todos los clientes.

### **Productos** (`/products`)
- `POST /` - Crear un nuevo producto.
- `GET /{id}/stock` - Obtener stock de un producto.
- `PATCH /{id}/quantity/{quantity}` - Reducir stock.
- `PATCH /{id}/increases/{quantity}` - Aumentar stock.
- `GET /{id}/price` - Obtener el precio de un producto.
- `PATCH /{id}/price/{newPrice}` - Actualizar precio.
- `GET /{id}` - Obtener detalles de un producto.
- `GET /` - Listar todos los productos.

### **Órdenes** (`/orders`)
- `POST /` - Crear una nueva orden.
- `PATCH /total/{id}` - Calcular el total de una orden.
- `PATCH /{idOrder}/order-details/{idOrderDetails}` - Agregar detalles a una orden.
- `DELETE /{idOrder}/order-details/{idOrderDetails}` - Eliminar detalles de una orden.
- `GET /customer/{id}` - Obtener cliente de una orden.
- `GET /{id}` - Obtener una orden.
- `GET /{id}/order-details` - Obtener detalles de una orden.
- `PATCH /{id}/completeOrder` - Completar una orden.
- `GET /` - Listar todas las órdenes.

### **Detalles de Órdenes** (`/order-details`)
- `POST /` - Crear detalles de una orden.
- `GET /product/{id}` - Obtener información del producto en una orden.
- `GET /quantity/{id}` - Obtener cantidad de productos en una orden.
- `GET /{id}` - Obtener detalles de una orden.

## Seguridad y Autenticación
Esta API utiliza **Spring Security** con **JWT** para la gestión de autenticación y autorización.

### Endpoints de Seguridad (`/auth`)
- `POST /register` - Registrar un nuevo usuario.
- `POST /login` - Autenticar usuario y recibir un JWT.
- `GET /profile` - Obtener información del usuario autenticado (requiere token JWT).

### Flujo de Autenticación
1. Un usuario se registra en `/auth/register`.
2. Luego, inicia sesión en `/auth/login` y recibe un token JWT.
3. Usa el token en la cabecera **Authorization: Bearer <token>** para acceder a los endpoints protegidos.

## Documentación con Swagger
Esta API cuenta con documentación generada con **Swagger** utilizando **Springdoc OpenAPI**. Para acceder a la documentación interactiva:

- Visita: `http://localhost:8080/swagger-ui.html`






