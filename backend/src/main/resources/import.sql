INSERT INTO rol (id, nombre) VALUES (1, 'Administrador');
INSERT INTO rol (id, nombre) VALUES (2, 'Cliente');
INSERT INTO rol (id, nombre) VALUES (3, 'Vendedor');
INSERT INTO rol (id, nombre) VALUES (4, 'Almacenista');

INSERT INTO area (id, nombre) VALUES (1, 'Administración');
INSERT INTO area (id, nombre) VALUES (2, 'Ventas');
INSERT INTO area (id, nombre) VALUES (3, 'Almacén');
INSERT INTO area (id, nombre) VALUES (4, 'Soporte Técnico');

INSERT INTO cargo (id, nombre) VALUES (1, 'Gerente General');
INSERT INTO cargo (id, nombre) VALUES (2, 'Supervisor de Ventas');
INSERT INTO cargo (id, nombre) VALUES (3, 'Asistente de Almacén');
INSERT INTO cargo (id, nombre) VALUES (4, 'Técnico Especializado');

INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (1, 'admin@tecnofix.com', 'Carlos Mendoza', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, false, 1);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (2, 'cliente1@gmail.com', 'Ana López', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, true, 2);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (3, 'cliente2@outlook.com', 'Roberto Sánchez', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, false, 2);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (4, 'vendedor@tecnofix.com', 'María García', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, false, 3);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (5, 'almacen@tecnofix.com', 'José Ramírez', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, false, 4);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (6, 'cliente3@gmail.com', 'Lucía Fernández', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, true, 2);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (7, 'cliente4@yahoo.com', 'Pedro Castillo', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, false, 2);
INSERT INTO usuario (id, correo, nombre, contrasena, activo, doble_factor_activo, rol_id) VALUES (8, 'cliente5@hotmail.com', 'Sofía Vargas', '$2a$12$LgrxVz9n0QZyDKC.KzhAe.zeB1BAXwGpj8X7LOqeoG5Qvbv./2thq', true, true, 2);

INSERT INTO cliente (id, telefono, direccion, usuario_id) VALUES (1, '555-1234', 'Av. Principal 123, Lima', 2);
INSERT INTO cliente (id, telefono, direccion, usuario_id) VALUES (2, '555-5678', 'Calle Los Pinos 456, Lima', 3);
INSERT INTO cliente (id, telefono, direccion, usuario_id) VALUES (3, '555-9012', 'Jr. Libertad 789, Lima', 6);
INSERT INTO cliente (id, telefono, direccion, usuario_id) VALUES (4, '555-3456', 'Av. Universitaria 321, Lima', 7);
INSERT INTO cliente (id, telefono, direccion, usuario_id) VALUES (5, '555-7890', 'Calle Las Flores 654, Lima', 8);

INSERT INTO empleado (id, area_id, cargo_id, usuario_id) VALUES (1, 1, 1, 1);
INSERT INTO empleado (id, area_id, cargo_id, usuario_id) VALUES (2, 2, 2, 4);
INSERT INTO empleado (id, area_id, cargo_id, usuario_id) VALUES (3, 3, 3, 5);

INSERT INTO codigo_verificacion (id, codigo, fecha_generacion, fecha_expiracion, intentos_maximos, intentos_realizados, estado, usuario_id, fecha_uso) VALUES (1, 'ABC123', '2026-01-14 10:00:00', '2026-01-14 10:10:00', 3, 0, 'PENDIENTE', 2, NULL);
INSERT INTO codigo_verificacion (id, codigo, fecha_generacion, fecha_expiracion, intentos_maximos, intentos_realizados, estado, usuario_id, fecha_uso) VALUES (2, 'DEF456', '2026-01-14 11:30:00', '2026-01-14 11:40:00', 3, 1, 'PENDIENTE', 6, NULL);
INSERT INTO codigo_verificacion (id, codigo, fecha_generacion, fecha_expiracion, intentos_maximos, intentos_realizados, estado, usuario_id, fecha_uso) VALUES (3, 'GHI789', '2026-01-14 09:15:00', '2026-01-14 09:25:00', 3, 3, 'BLOQUEADO', 8, NULL);

INSERT INTO categoria (id, nombre) VALUES (1, 'Laptops');
INSERT INTO categoria (id, nombre) VALUES (2, 'Componentes PC');
INSERT INTO categoria (id, nombre) VALUES (3, 'Periféricos');
INSERT INTO categoria (id, nombre) VALUES (4, 'Accesorios');
INSERT INTO categoria (id, nombre) VALUES (5, 'Software');

INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (1, 'Laptop Gamer ASUS ROG', 'Laptop gaming con RTX 4060, 16GB RAM, SSD 1TB', 3499.99, 15, 'LAP-ASUS-001', true, 1, 'asus-rog.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (2, 'Laptop Dell XPS 15', 'Laptop profesional con pantalla 4K, i7 13va gen', 2899.99, 8, 'LAP-DELL-002', true, 1, 'dell-xps.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (3, 'Procesador Intel Core i9', 'Procesador i9-13900K, 24 núcleos, 5.8GHz', 699.99, 25, 'CPU-INTEL-003', true, 2, 'i9-13900k.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (4, 'Tarjeta Gráfica NVIDIA RTX 4080', '16GB GDDR6X, DLSS 3, Ray Tracing', 1299.99, 12, 'GPU-NVIDIA-004', true, 2, 'rtx-4080.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (5, 'Memoria RAM Corsair 32GB', 'DDR5 6000MHz, CL36, RGB', 199.99, 50, 'RAM-CORSAIR-005', true, 2, 'ram-corsair.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (6, 'SSD Samsung 2TB', 'NVMe PCIe 4.0, 7000MB/s lectura', 249.99, 40, 'SSD-SAMSUNG-006', true, 2, 'ssd-samsung.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (7, 'Monitor Gaming 27"', '144Hz, 1ms, QHD, FreeSync', 499.99, 20, 'MON-ASUS-007', true, 3, 'monitor-gaming.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (8, 'Teclado Mecánico Redragon', 'Switches Blue, RGB, anti-ghosting', 89.99, 35, 'TEC-REDRAGON-008', true, 3, 'teclado-redragon.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (9, 'Mouse Logitech G Pro', 'Sensor HERO 25K, 6 botones, inalámbrico', 129.99, 30, 'MOU-LOGITECH-009', true, 3, 'mouse-logitech.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (10, 'Windows 11 Pro', 'Licencia original, 64-bit', 199.99, 100, 'SOFT-MICROSOFT-010', true, 5, 'windows-11.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (11, 'Office 2021', 'Word, Excel, PowerPoint, Outlook', 149.99, 80, 'SOFT-OFFICE-011', true, 5, 'office-2021.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (12, 'Fuente de Poder 850W', '80 Plus Gold, modular, certificada', 159.99, 25, 'PSU-CORSAIR-012', true, 2, 'fuente-poder.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (13, 'Cooler CPU Liquid Cooler', '240mm, RGB, doble ventilador', 119.99, 18, 'COOL-CORSAIR-013', true, 2, 'cooler-liquid.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (14, 'Disco Duro 4TB', '7200RPM, SATA 6Gb/s, 256MB cache', 129.99, 22, 'HDD-WD-014', true, 2, 'hdd-4tb.jpg');
INSERT INTO producto (id, nombre, descripcion, precio, stock, sku, activo, categoria_id, imagen_nombre) VALUES (15, 'Webcam Logitech C920', '1080p Full HD, micrófono integrado', 79.99, 45, 'CAM-LOGITECH-015', true, 4, 'webcam-logitech.jpg');

INSERT INTO tipo_proveedor (id, nombre) VALUES (1, 'Fabricante Directo');
INSERT INTO tipo_proveedor (id, nombre) VALUES (2, 'Distribuidor Autorizado');
INSERT INTO tipo_proveedor (id, nombre) VALUES (3, 'Importador');
INSERT INTO tipo_proveedor (id, nombre) VALUES (4, 'Mayorista');

INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (1, 'ASUS Perú', 'ventas@asus.com.pe', '01-123-4567', 'Av. Javier Prado 1234, Lima', 2);
INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (2, 'Dell Technologies', 'compras@dell.com.pe', '01-234-5678', 'Calle Las Begonias 567, Lima', 1);
INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (3, 'Intel Corporation', 'distribucion@intel.com.pe', '01-345-6789', 'Av. Elmer Faucett 2345, Callao', 2);
INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (4, 'NVIDIA Perú', 'ventas@nvidia.com.pe', '01-456-7890', 'Costa Verde 123, San Miguel', 2);
INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (5, 'Corsair Perú', 'compras@corsair.com.pe', '01-567-8901', 'Av. La Marina 3456, San Miguel', 3);
INSERT INTO proveedor (id, nombre, correo, telefono, direccion, tipo_proveedor_id) VALUES (6, 'Samsung Electronics', 'ventas@samsung.com.pe', '01-678-9012', 'Centro Empresarial Real, San Isidro', 1);

INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (1, '2026-01-05', 1, 3, 'RECIBIDA');
INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (2, '2026-01-08', 3, 3, 'RECIBIDA');
INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (3, '2026-01-10', 4, 3, 'RECIBIDA');
INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (4, '2026-01-12', 5, 3, 'PENDIENTE');
INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (5, '2026-01-14', 6, 3, 'RECIBIDA');
INSERT INTO compra (id, fecha_compra, proveedor_id, empleado_id, estado_compra) VALUES (6, '2026-01-13', 2, 3, 'RECIBIDA');

INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (1, 20, 3200.00, 1, 1);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (2, 15, 2600.00, 6, 2);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (3, 30, 650.00, 2, 3);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (4, 15, 1150.00, 3, 4);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (5, 60, 180.00, 4, 5);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (6, 50, 220.00, 5, 6);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (7, 25, 450.00, 1, 7);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (8, 40, 75.00, 4, 8);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (9, 35, 110.00, 4, 9);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (10, 120, 180.00, 5, 10);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (11, 90, 130.00, 5, 11);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (12, 30, 140.00, 4, 12);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (13, 20, 100.00, 4, 13);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (14, 25, 110.00, 5, 14);
INSERT INTO compra_detalle (id, cantidad, precio_unitario, compra_id, producto_id) VALUES (15, 50, 65.00, 5, 15);

INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (1, '2026-01-05 14:30:00', 0, 20, 1, 'ENTRADA', 'COMPRA', 5, 'COMP-001', 'Ingreso de laptops ASUS ROG');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (2, '2026-01-08 11:20:00', 0, 30, 3, 'ENTRADA', 'COMPRA', 5, 'COMP-002', 'Ingreso de procesadores Intel i9');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (3, '2026-01-10 09:45:00', 0, 15, 4, 'ENTRADA', 'COMPRA', 5, 'COMP-003', 'Ingreso de tarjetas gráficas RTX 4080');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (4, '2026-01-05 15:10:00', 0, 25, 7, 'ENTRADA', 'COMPRA', 5, 'COMP-001', 'Ingreso de monitores gaming');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (5, '2026-01-14 10:30:00', 0, 50, 6, 'ENTRADA', 'COMPRA', 5, 'COMP-005', 'Ingreso de SSDs Samsung');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (6, '2026-01-13 13:15:00', 0, 15, 2, 'ENTRADA', 'COMPRA', 5, 'COMP-006', 'Ingreso de laptops Dell XPS');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (7, '2026-01-12 15:00:00', 20, 19, 1, 'SALIDA', 'ORDEN', 2, 'ORD-001', 'Venta de laptop ASUS ROG');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (8, '2026-01-12 15:00:00', 60, 58, 5, 'SALIDA', 'ORDEN', 2, 'ORD-001', 'Venta de memoria RAM');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (9, '2026-01-13 11:00:00', 15, 14, 2, 'SALIDA', 'ORDEN', 2, 'ORD-002', 'Venta de laptop Dell XPS');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (10, '2026-01-13 11:00:00', 35, 34, 9, 'SALIDA', 'ORDEN', 2, 'ORD-002', 'Venta de mouse Logitech');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (11, '2026-01-13 16:30:00', 30, 29, 3, 'SALIDA', 'ORDEN', 2, 'ORD-003', 'Venta de procesador Intel i9');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (12, '2026-01-14 09:30:00', 15, 14, 4, 'SALIDA', 'ORDEN', 2, 'ORD-004', 'Venta de tarjeta gráfica RTX 4080');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (13, '2026-01-14 09:30:00', 30, 29, 12, 'SALIDA', 'ORDEN', 2, 'ORD-004', 'Venta de fuente de poder');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (14, '2026-01-11 12:00:00', 40, 38, 8, 'SALIDA', 'ORDEN', 2, 'ORD-005', 'Venta de teclados Redragon');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (15, '2026-01-11 12:00:00', 120, 119, 10, 'SALIDA', 'ORDEN', 2, 'ORD-005', 'Venta de Windows 11 Pro');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (17, '2026-01-10 16:00:00', 50, 49, 6, 'SALIDA', 'ORDEN', 2, 'ORD-006', 'Venta de SSD Samsung');
INSERT INTO movimiento_stock (id, fecha_movimiento, cantidad_anterior, cantidad_nueva, producto_id, tipo_movimiento, tipo_referencia, usuario_id, codigo_referencia, observacion) VALUES (16, '2026-01-10 16:00:00', 25, 24, 7, 'SALIDA', 'ORDEN', 2, 'ORD-006', 'Venta de monitor gaming');

INSERT INTO metodo_pago (id, nombre) VALUES (1, 'Tarjeta de Crédito');
INSERT INTO metodo_pago (id, nombre) VALUES (2, 'Tarjeta de Débito');
INSERT INTO metodo_pago (id, nombre) VALUES (3, 'PayPal');
INSERT INTO metodo_pago (id, nombre) VALUES (4, 'Transferencia Bancaria');
INSERT INTO metodo_pago (id, nombre) VALUES (5, 'Efectivo en Tienda');

INSERT INTO carrito (id, fecha_creacion, fecha_actualizacion, cliente_id, estado_carrito) VALUES (1, '2026-01-14 09:00:00', '2026-01-14 09:30:00', 1, 'ACTIVO');
INSERT INTO carrito (id, fecha_creacion, fecha_actualizacion, cliente_id, estado_carrito) VALUES (2, '2026-01-14 10:15:00', '2026-01-14 10:45:00', 2, 'ACTIVO');
INSERT INTO carrito (id, fecha_creacion, fecha_actualizacion, cliente_id, estado_carrito) VALUES (3, '2026-01-13 14:20:00', '2026-01-14 08:00:00', 3, 'ABANDONADO');
INSERT INTO carrito (id, fecha_creacion, fecha_actualizacion, cliente_id, estado_carrito) VALUES (4, '2026-01-14 11:30:00', '2026-01-14 11:45:00', 4, 'ACTIVO');
INSERT INTO carrito (id, fecha_creacion, fecha_actualizacion, cliente_id, estado_carrito) VALUES (5, '2026-01-14 12:00:00', '2026-01-14 12:30:00', 5, 'ACTIVO');

INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (1, 1, 3499.99, '2026-01-14 09:05:00', 1, 1);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (2, 2, 199.99, '2026-01-14 09:15:00', 1, 5);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (3, 1, 499.99, '2026-01-14 09:25:00', 1, 7);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (4, 1, 2899.99, '2026-01-14 10:20:00', 2, 2);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (5, 1, 129.99, '2026-01-14 10:35:00', 2, 9);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (6, 1, 699.99, '2026-01-13 14:25:00', 3, 3);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (7, 1, 249.99, '2026-01-13 14:35:00', 3, 6);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (8, 1, 1299.99, '2026-01-14 11:35:00', 4, 4);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (9, 1, 159.99, '2026-01-14 11:40:00', 4, 12);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (10, 2, 89.99, '2026-01-14 12:05:00', 5, 8);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (11, 1, 199.99, '2026-01-14 12:15:00', 5, 10);
INSERT INTO carrito_item (id, cantidad, precio_unitario, fecha_agregado, carrito_id, producto_id) VALUES (12, 1, 119.99, '2026-01-14 12:25:00', 5, 13);

INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (1, '2026-01-12 14:30:00', 'Av. Principal 123, Lima', 1, 'ENTREGADO', 1);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (2, '2026-01-13 10:45:00', 'Calle Los Pinos 456, Lima', 2, 'ENVIADO', 3);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (3, '2026-01-13 16:20:00', 'Jr. Libertad 789, Lima', 3, 'PAGADO', 4);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (4, '2026-01-14 09:15:00', 'Av. Universitaria 321, Lima', 4, 'PENDIENTE', 2);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (5, '2026-01-11 11:30:00', 'Calle Las Flores 654, Lima', 5, 'ENTREGADO', 5);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (6, '2026-01-10 15:45:00', 'Av. Principal 123, Lima', 1, 'ENTREGADO', 1);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (7, '2026-01-09 13:20:00', 'Calle Los Pinos 456, Lima', 2, 'CANCELADO', 3);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (8, '2026-01-08 17:10:00', 'Jr. Libertad 789, Lima', 3, 'ENTREGADO', 4);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (9, '2026-01-07 12:05:00', 'Av. Universitaria 321, Lima', 4, 'ENTREGADO', 2);
INSERT INTO orden (id, fecha_orden, direccion_envio, cliente_id, estado_orden, metodo_pago_id) VALUES (10, '2026-01-06 10:30:00', 'Calle Las Flores 654, Lima', 5, 'ENTREGADO', 5);

INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (1, 1, 3499.99, 1, 1);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (2, 2, 199.99, 1, 5);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (3, 1, 2899.99, 2, 2);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (4, 1, 129.99, 2, 9);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (5, 1, 699.99, 3, 3);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (6, 1, 1299.99, 4, 4);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (7, 1, 159.99, 4, 12);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (8, 2, 89.99, 5, 8);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (9, 1, 199.99, 5, 10);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (10, 1, 499.99, 6, 7);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (11, 1, 249.99, 6, 6);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (12, 1, 3499.99, 7, 1);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (13, 1, 499.99, 8, 7);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (14, 1, 129.99, 8, 9);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (15, 1, 79.99, 9, 15);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (16, 1, 149.99, 9, 11);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (17, 1, 119.99, 10, 13);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (18, 1, 129.99, 10, 14);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (19, 1, 199.99, 1, 5);
INSERT INTO orden_detalle (id, cantidad, precio_unitario, orden_id, producto_id) VALUES (20, 1, 89.99, 2, 8);

INSERT INTO tickets_soporte (id, codigo_ticket, nombre_cliente, email_cliente, tipo_solicitud, asunto, descripcion, estado, fecha_creacion, fecha_ultima_actualizacion) VALUES (1, 'TICK-2026-1001', 'Juan Pérez', 'juan.perez@example.com', 'GARANTIA', 'Falla en tarjeta de vídeo RTX 4080', 'La tarjeta presenta artifacts en pantalla tras 15 minutos de juego.', 'PENDIENTE', '2026-07-20 10:30:00', '2026-07-20 10:30:00');
INSERT INTO tickets_soporte (id, codigo_ticket, nombre_cliente, email_cliente, tipo_solicitud, asunto, descripcion, estado, fecha_creacion, fecha_ultima_actualizacion) VALUES (2, 'TICK-2026-1002', 'María Gómez', 'maria.gomez@example.com', 'PEDIDO', 'Retraso en envío de pedido #ORD-2026-004', 'Mi pedido lleva 3 días en estado de procesamiento sin número de guía.', 'EN_PROCESO', '2026-07-21 14:15:00', '2026-07-22 09:00:00');
INSERT INTO tickets_soporte (id, codigo_ticket, nombre_cliente, email_cliente, tipo_solicitud, asunto, descripcion, estado, fecha_creacion, fecha_ultima_actualizacion) VALUES (3, 'TICK-2026-1003', 'Carlos Mendoza', 'carlos.mendoza@example.com', 'DUDA', 'Compatibilidad de Memoria RAM DDR5', 'Quisiera saber si el kit Corsair Vengeance es compatible con mi placa B650.', 'RESUELTO', '2026-07-18 11:00:00', '2026-07-19 16:45:00');
INSERT INTO tickets_soporte (id, codigo_ticket, nombre_cliente, email_cliente, tipo_solicitud, asunto, descripcion, estado, fecha_creacion, fecha_ultima_actualizacion) VALUES (4, 'TICK-2026-0900', 'Lucía Torres', 'lucia.torres@example.com', 'CUENTA', 'Consulta de facturación electrónica', 'Requiero la factura enviada a mi RUC pero no respondieron a tiempo.', 'EXPIRADO', '2026-07-01 08:00:00', '2026-07-08 02:00:00');