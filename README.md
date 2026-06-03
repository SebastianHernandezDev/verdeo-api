# 🌱 Verdeo API

Backend RESTful para Verdeo, una plataforma enfocada en la gestión y comercialización de productos ecológicos.

## 🚀 Tecnologías

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Maven
- Hibernate
- Neon Database

---

## 📂 Arquitectura

```text
src/main/java
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── exception
├── model
├── repository
├── security
├── service
└── VerdeoApiApplication
```

---

## 🔐 Autenticación

La API utiliza:

- JWT Access Token
- Refresh Token
- Roles (USER y ADMIN)

### Login

```http
POST /api/auth/login
```

Respuesta:

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "tokenType": "Bearer"
}
```

---

## 👤 Roles

### USER

Puede:

- Ver productos
- Ver categorías
- Gestionar carrito
- Realizar compras

### ADMIN

Puede:

- Crear categorías
- Editar categorías
- Eliminar categorías
- Gestionar productos
- Gestionar inventario

---

## 📦 Endpoints Principales

### Auth

| Método | Endpoint |
|----------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |
| POST | /api/auth/refresh |

---

### Usuarios

| Método | Endpoint |
|----------|----------|
| GET | /api/users |
| GET | /api/users/{id} |
| PUT | /api/users/{id} |
| DELETE | /api/users/{id} |

---

### Categorías

| Método | Endpoint |
|----------|----------|
| GET | /api/categories |
| GET | /api/categories/{id} |
| POST | /api/categories |
| PUT | /api/categories/{id} |
| DELETE | /api/categories/{id} |

---

### Productos

| Método | Endpoint |
|----------|----------|
| GET | /api/products |
| GET | /api/products/{id} |
| POST | /api/products |
| PUT | /api/products/{id} |
| DELETE | /api/products/{id} |

---

### Carrito

| Método | Endpoint |
|----------|----------|
| GET | /api/cart |
| POST | /api/cart/add |
| POST | /api/cart/decrease |
| DELETE | /api/cart/remove |
| DELETE | /api/cart/clear |

---

## ⚙️ Variables de entorno

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET=
JWT_EXPIRATION_MS=
```

---

## 🛠️ Ejecución local

Clonar repositorio:

```bash
git clone https://github.com/usuario/verdeo-api.git
```

Entrar al proyecto:

```bash
cd verdeo-api
```

Configurar variables de entorno.

Ejecutar:

```bash
mvn spring-boot:run
```

La API estará disponible en:

```text
http://localhost:8080
```

---

## 🗄️ Base de datos

La aplicación utiliza PostgreSQL alojado en Neon.

Hibernate se encarga de la generación y actualización automática del esquema.

---

## 📈 Estado del proyecto

### Implementado

- [x] JWT Authentication
- [x] Refresh Tokens
- [x] Spring Security
- [x] Gestión de usuarios
- [x] Gestión de categorías
- [x] Gestión de productos
- [x] Carrito de compras
- [x] DTOs
- [x] Manejo global de excepciones

### Próximamente

- [ ] Órdenes de compra
- [ ] Pasarela de pagos
- [ ] Swagger/OpenAPI
- [ ] Docker
- [ ] Deploy automático

---

## 👨‍💻 Autor

Sebastian David Hernandez Perez

Backend Developer Java | Spring Boot
