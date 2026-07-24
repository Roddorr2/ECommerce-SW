-- 001-initial-schema.sql
-- Definición del esquema inicial de la base de datos

CREATE TABLE IF NOT EXISTS rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS area (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cargo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    doble_factor_activo BOOLEAN NOT NULL DEFAULT FALSE,
    rol_id INT NOT NULL,
    CONSTRAINT uk_usuario_correo UNIQUE (correo),
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES rol (id)
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    CONSTRAINT uk_cliente_usuario UNIQUE (usuario_id),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS empleado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    area_id INT NOT NULL,
    cargo_id INT NOT NULL,
    CONSTRAINT uk_empleado_usuario UNIQUE (usuario_id),
    CONSTRAINT fk_empleado_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_empleado_area FOREIGN KEY (area_id) REFERENCES area (id),
    CONSTRAINT fk_empleado_cargo FOREIGN KEY (cargo_id) REFERENCES cargo (id)
);

CREATE TABLE IF NOT EXISTS codigo_verificacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    codigo VARCHAR(6) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_generacion DATETIME NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    intentos_realizados INT NOT NULL DEFAULT 0,
    intentos_maximos INT NOT NULL DEFAULT 3,
    fecha_uso DATETIME DEFAULT NULL,
    CONSTRAINT fk_codigo_verificacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(7, 2) NOT NULL,
    stock INT NOT NULL,
    imagen_nombre VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    categoria_id INT NOT NULL,
    CONSTRAINT uk_producto_sku UNIQUE (sku),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

CREATE TABLE IF NOT EXISTS tipo_proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    telefono VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    tipo_proveedor_id INT NOT NULL,
    CONSTRAINT uk_proveedor_correo UNIQUE (correo),
    CONSTRAINT fk_proveedor_tipo_proveedor FOREIGN KEY (tipo_proveedor_id) REFERENCES tipo_proveedor (id)
);

CREATE TABLE IF NOT EXISTS compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_compra DATE NOT NULL,
    estado_compra VARCHAR(20) NOT NULL,
    proveedor_id INT NOT NULL,
    empleado_id INT NOT NULL,
    CONSTRAINT fk_compra_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedor (id),
    CONSTRAINT fk_compra_empleado FOREIGN KEY (empleado_id) REFERENCES empleado (id)
);

CREATE TABLE IF NOT EXISTS compra_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(7, 2) NOT NULL,
    compra_id INT NOT NULL,
    producto_id INT NOT NULL,
    CONSTRAINT fk_compra_detalle_compra FOREIGN KEY (compra_id) REFERENCES compra (id),
    CONSTRAINT fk_compra_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);

CREATE TABLE IF NOT EXISTS movimiento_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad_anterior INT NOT NULL,
    cantidad_nueva INT NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    tipo_referencia VARCHAR(50) NOT NULL,
    codigo_referencia VARCHAR(255),
    fecha_movimiento DATETIME NOT NULL,
    observacion TEXT,
    producto_id INT NOT NULL,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_movimiento_stock_producto FOREIGN KEY (producto_id) REFERENCES producto (id),
    CONSTRAINT fk_movimiento_stock_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS metodo_pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    CONSTRAINT uk_metodo_pago_nombre UNIQUE (nombre)
);

CREATE TABLE IF NOT EXISTS carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    estado_carrito VARCHAR(20) NOT NULL,
    CONSTRAINT fk_cliente_carrito FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE IF NOT EXISTS carrito_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrito_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(7, 2) NOT NULL,
    fecha_agregado DATETIME NOT NULL,
    CONSTRAINT fk_carrito_item_carrito FOREIGN KEY (carrito_id) REFERENCES carrito (id),
    CONSTRAINT fk_carrito_item_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);

CREATE TABLE IF NOT EXISTS orden (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_orden DATETIME NOT NULL,
    direccion_envio VARCHAR(255) NOT NULL,
    estado_orden VARCHAR(20) NOT NULL,
    cliente_id INT NOT NULL,
    metodo_pago_id INT NOT NULL,
    CONSTRAINT fk_orden_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_orden_metodo_pago FOREIGN KEY (metodo_pago_id) REFERENCES metodo_pago (id)
);

CREATE TABLE IF NOT EXISTS orden_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(7, 2) NOT NULL,
    orden_id INT NOT NULL,
    producto_id INT NOT NULL,
    CONSTRAINT fk_orden_detalle_orden FOREIGN KEY (orden_id) REFERENCES orden (id),
    CONSTRAINT fk_orden_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);

CREATE TABLE IF NOT EXISTS tickets_soporte (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_ticket VARCHAR(30) NOT NULL UNIQUE,
    nombre_cliente VARCHAR(150) NOT NULL,
    email_cliente VARCHAR(150) NOT NULL,
    tipo_solicitud VARCHAR(50) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_ultima_actualizacion DATETIME NOT NULL
);
