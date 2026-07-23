# ECommerce-SW

Repositorio para el sistema de comercio electrónico. Este proyecto es una solución integral que incluye frontend y backend desacoplados, permitiendo un desarrollo y mantenimiento ágil.

---

## Integrantes

|   | Usuario         |
|:-:|-----------------|
| <img src="https://avatars.githubusercontent.com/u/186851935?v=4" width="50"> | [Pardos06](https://github.com/Pardos06) 
| <img src="https://avatars.githubusercontent.com/u/150357479?v=4" width="50"> | [Roddorr2](https://github.com/Roddorr2) 
| <img src="https://avatars.githubusercontent.com/u/118624556?v=4" width="50"> | [Paulx09](https://github.com/Paulx09)  

---

## Arquitectura General

El sistema está basado en una arquitectura de **aplicación dividida**:

- **Backend:** Responsable de la lógica de negocio, gestión de datos y exposición de API RESTful.
- **Frontend:** Interfaz de usuario dinámica que consume los servicios del backend.

Ambos módulos están organizados en los siguientes directorios:

```
ECommerce-SW/
│
├── backend/   # Lógica del servidor y negocio
├── frontend/  # Aplicación cliente
└── .vscode/   # Configuración de entorno de desarrollo
```

---

## Backend

**Tecnologías usadas:**

<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white&style=for-the-badge" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-007396?logo=java&logoColor=white&style=for-the-badge" alt="Java" />
  <img src="https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white&style=for-the-badge" alt="JWT" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black&style=for-the-badge" alt="Swagger" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white&style=for-the-badge" alt="MySQL" />
</p>

- **Lenguaje principal:** Java
- **Framework principal:** Spring Boot
- **Seguridad:** JWT (Json Web Tokens) para autenticación y manejo de sesiones seguras.
- **Documentación API:** Swagger
- **Base de datos:** MySQL (gestiona persistencia de entidades y relaciones de la tienda)
- **Servicios expuestos:** API RESTful para operaciones CRUD de productos, usuarios, pedidos, etc.

### Características:

- Gestión de usuarios con autenticación y autorización JWT
- Manejo de productos, inventario y pedidos
- Documentación interactiva y pruebas via Swagger
- Interacción con base de datos MySQL utilizando ORM o JDBC

---

## Frontend

**Tecnologías usadas:**

<p>
  <img src="https://img.shields.io/badge/Angular-DD0031?logo=angular&logoColor=white&style=for-the-badge" alt="Angular" />
  <img src="https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white&style=for-the-badge" alt="TypeScript" />
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?logo=bootstrap&logoColor=white&style=for-the-badge" alt="Bootstrap" />
  <img src="https://img.shields.io/badge/PrimeNG-005CB9?logo=prime&logoColor=white&style=for-the-badge" alt="PrimeNG" />
  <img src="https://img.shields.io/badge/SCSS-CC6699?logo=sass&logoColor=white&style=for-the-badge" alt="SCSS" />
</p>

- **Framework principal:** Angular
- **Lenguaje:** TypeScript
- **Estilos:** Bootstrap, SCSS, PrimeNG
- **Consumo de API:** Interfaz que se comunica con el backend para mostrar productos, gestionar carrito y procesar compras.

### Características:

- Experiencia de usuario enriquecida y responsiva
- Gestión de catálogo, carrito de compras y órdenes
- Autenticación y registros de usuarios

---

## Base de datos

<p>
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white&style=for-the-badge" alt="MySQL" />
</p>

Este proyecto utiliza **MySQL** como sistema gestor de base de datos relacional, clave para la persistencia de la información transaccional, usuarios y productos.

---

## Tecnologías principales

| Layer      | Tecnologías                                                                              |
|------------|-----------------------------------------------------------------------------------------|
| Backend    | <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Java-007396?logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white"/> <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black"/> <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white"/> |
| Frontend   | <img src="https://img.shields.io/badge/Angular-DD0031?logo=angular&logoColor=white"/> <img src="https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white"/> <img src="https://img.shields.io/badge/Bootstrap-7952B3?logo=bootstrap&logoColor=white"/> <img src="https://img.shields.io/badge/PrimeNG-005CB9?logo=prime&logoColor=white"/> <img src="https://img.shields.io/badge/SCSS-CC6699?logo=sass&logoColor=white"/> |
| DB         | <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white"/>        |

---

## Enlaces de interés

- [Repositorio en GitHub](https://github.com/Pardos06/ECommerce-SW)

---

> Sugerencia: Completar detalles técnicos específicos de frameworks/librerías usados al explorar los archivos internos del repositorio.
