-- Crear y seleccionar la base de datos 
CREATE DATABASE IF NOT EXISTS SistemaCotizaciones;
USE SistemaCotizaciones;

-- Tabla USUARIO
CREATE TABLE USUARIO (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL
);

-- Tabla CLIENTE
CREATE TABLE CLIENTE (
    ID_Cliente INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Cliente VARCHAR(255),
    Documento_Identificacion VARCHAR(255),
    Tipo_Persona VARCHAR(50),
    Telefono_Contacto VARCHAR(50),
    Correo_Contacto VARCHAR(255),
    Direccion VARCHAR(255),
    Estado_Cliente VARCHAR(50),
    ID_Usuario_Creacion INT,
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Fecha_Inactivacion DATETIME,
    FOREIGN KEY (ID_Usuario_Creacion) REFERENCES USUARIO(ID_Usuario)
);

-- Tabla EMPLEADO
CREATE TABLE EMPLEADO (
    ID_Empleado INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Empleado VARCHAR(255),
    Documento_Identificacion VARCHAR(255),
    Tipo_Persona VARCHAR(50),
    Tipo_Contratacion VARCHAR(50),
    Telefono VARCHAR(50),
    Correo_Electronico VARCHAR(255),
    Direccion VARCHAR(255),
    Estado_Empleado VARCHAR(50),
    ID_Usuario_Creacion INT,
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Fecha_Inactivacion DATETIME,
    FOREIGN KEY (ID_Usuario_Creacion) REFERENCES USUARIO(ID_Usuario)
);

-- Tabla COTIZACION
CREATE TABLE COTIZACION (
    ID_Cotizacion INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cliente INT,
    ID_Empleado_Elabora INT,
    Titulo_Cotizacion VARCHAR(255), -- Campo agregado para el título
    Fecha_Creacion_Cotizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Tentativa_Inicio DATETIME,
    Fecha_Tentativa_Fin DATETIME,
    Cantidad_Horas_Del_Proyecto DECIMAL(10,2),
    Costo_De_Asignaciones DECIMAL(10,2),
    Costos_Adicionales DECIMAL(10,2),
    Total_Cotizacion DECIMAL(10,2),
    Estado_Cotizacion VARCHAR(50),
    FOREIGN KEY (ID_Cliente) REFERENCES CLIENTE(ID_Cliente),
    FOREIGN KEY (ID_Empleado_Elabora) REFERENCES USUARIO(ID_Usuario)
);

-- Tabla ASIGNACION_ACTIVIDAD
CREATE TABLE ASIGNACION_ACTIVIDAD (
    ID_Asignacion INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cotizacion INT,
    ID_Empleado_Asignado INT,
    Titulo_Actividad VARCHAR(255),
    Area_Asignada VARCHAR(255),
    Costo_Por_Hora DECIMAL(10,2),
    Fecha_Hora_Inicio DATETIME,
    Fecha_Hora_Fin DATETIME,
    Cantidad_Horas_Aproximadas DECIMAL(10,2),
    Incremento_Extra_Porcentaje DECIMAL(5,2),
    Costo_Base DECIMAL(10,2),
    Total_Asignacion DECIMAL(10,2),
    FOREIGN KEY (ID_Cotizacion) REFERENCES COTIZACION(ID_Cotizacion),
    FOREIGN KEY (ID_Empleado_Asignado) REFERENCES EMPLEADO(ID_Empleado)
);

-- Tabla SUBTAREA
CREATE TABLE SUBTAREA (
    ID_Subtarea INT AUTO_INCREMENT PRIMARY KEY,
    ID_Asignacion INT,
    Titulo_Subtarea VARCHAR(255),
    Descripcion_Subtarea TEXT,
    FOREIGN KEY (ID_Asignacion) REFERENCES ASIGNACION_ACTIVIDAD(ID_Asignacion)
);

-- Tabla COSTO_ADICIONAL
CREATE TABLE COSTO_ADICIONAL (
    ID_Costo_Adicional INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cotizacion INT,
    Descripcion VARCHAR(255),
    Monto DECIMAL(10,2),
    FOREIGN KEY (ID_Cotizacion) REFERENCES COTIZACION(ID_Cotizacion)
);

-- Insertar USUARIOS
INSERT INTO USUARIO (Nombre, Password, Email) VALUES
('admin', 'admin123', 'admin@empresa.com'),
('juanperez', 'jp123', 'juan.perez@empresa.com');

-- Insertar CLIENTES
INSERT INTO CLIENTE (Nombre_Cliente, Documento_Identificacion, Tipo_Persona, Telefono_Contacto, Correo_Contacto, Direccion, Estado_Cliente, ID_Usuario_Creacion)
VALUES
('Cliente A', '12345678', 'Natural', '555-1234', 'clientea@mail.com', 'Calle Falsa 123', 'Activo', 1),
('Empresa XYZ S.A.', 'RUC123456789', 'Juridica', '555-5678', 'contacto@xyz.com', 'Av. Empresa 456', 'Activo', 2);

-- Insertar EMPLEADOS
INSERT INTO EMPLEADO (Nombre_Empleado, Documento_Identificacion, Tipo_Persona, Tipo_Contratacion, Telefono, Correo_Electronico, Direccion, Estado_Empleado, ID_Usuario_Creacion)
VALUES
('Luis Gómez', '87654321', 'Natural', 'Tiempo Completo', '555-7890', 'luis.gomez@empresa.com', 'Calle 10 #123', 'Activo', 1),
('Ana Torres', '23456789', 'Natural', 'Medio Tiempo', '555-6543', 'ana.torres@empresa.com', 'Calle 20 #456', 'Activo', 2);

-- Insertar COTIZACIONES (ajustadas para que los totales sean coherentes)
-- Costo_De_Asignaciones = 1730.00, Costos_Adicionales = 270.00 → Total = 2000.00
-- Costo_De_Asignaciones = 360.00, Costos_Adicionales = 140.00 → Total = 500.00
INSERT INTO COTIZACION (
    ID_Cliente, ID_Empleado_Elabora, Titulo_Cotizacion,
    Fecha_Tentativa_Inicio, Fecha_Tentativa_Fin,
    Cantidad_Horas_Del_Proyecto, Costo_De_Asignaciones,
    Costos_Adicionales, Total_Cotizacion, Estado_Cotizacion
)
VALUES
(1, 1, 'Desarrollo de sitio web', DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), 120.00, 1730.00, 270.00, 2000.00, 'En proceso'),
(2, 2, 'Aplicación móvil de inventario', DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), 80.00, 360.00, 140.00, 500.00, 'Finalizada');


-- Insertar ASIGNACIONES (sumas: cotización 1 = 1100 + 630 = 1730)
INSERT INTO ASIGNACION_ACTIVIDAD (ID_Cotizacion, ID_Empleado_Asignado, Titulo_Actividad, Area_Asignada, Costo_Por_Hora, Fecha_Hora_Inicio, Fecha_Hora_Fin, Cantidad_Horas_Aproximadas, Incremento_Extra_Porcentaje, Costo_Base, Total_Asignacion)
VALUES
(1, 1, 'Desarrollo Backend', 'TI', 25.00, NOW(), DATE_ADD(NOW(), INTERVAL 10 DAY), 40.00, 10.00, 1000.00, 1100.00),
(1, 2, 'Diseño UI', 'Diseño', 20.00, NOW(), DATE_ADD(NOW(), INTERVAL 8 DAY), 30.00, 5.00, 600.00, 630.00),
(2, 2, 'Pruebas QA', 'Calidad', 18.00, NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 20.00, 0.00, 360.00, 360.00);

-- Insertar SUBTAREAS
INSERT INTO SUBTAREA (ID_Asignacion, Titulo_Subtarea, Descripcion_Subtarea)
VALUES
(1, 'Crear API REST', 'Desarrollar endpoints para gestión de usuarios y productos.'),
(2, 'Diseñar interfaz de login', 'Diseño responsivo para el formulario de inicio de sesión.'),
(3, 'Realizar pruebas funcionales', 'Ejecutar casos de prueba definidos en QA plan.');

-- Insertar COSTOS ADICIONALES coherentes con el total
INSERT INTO COSTO_ADICIONAL (ID_Cotizacion, Descripcion, Monto) VALUES
(1, 'Compra de licencias de software', 150.00),
(1, 'Gastos administrativos', 120.00),
(2, 'Infraestructura en la nube', 90.00),
(2, 'Material de capacitación', 50.00);
