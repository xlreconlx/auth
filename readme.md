# Este proyecto es un servicio de autenticación con registro de login y conexión a PostgreSQL.

## Instrucciones de ejecución

1. Clonar el repositorio
   ```bash
   git clone https://github.com/xlreconlx/auth.git
   cd auth
   ```

2. Crear un archivo application.properties en src/main/resources con la configuración necesaria (conexión a la base de datos, URLs externas, etc.):

```properties
# ==========================
# Aplicación
# ==========================
spring.application.name=auth
server.port=8080

# ==========================
# Base de datos (PostgreSQL)
# ==========================
spring.datasource.url=jdbc:postgresql://localhost:5432/dummyauth
spring.datasource.username=postgres
spring.datasource.password=1234

# ==========================
# JPA / Hibernate
# ==========================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# ==========================
# Feign (cliente HTTP)
# ==========================
feign.client.config.default.connectTimeout=5000
feign.client.config.default.readTimeout=10000

# ==========================
# URLs externas
# ==========================
dummyjson.base-url=https://dummyjson.com
```

3. Ejecutar con Maven
   ```bash
   ./mvn spring-boot:run
   ```

4. El servicio estará disponible en `http://localhost:8080/api/auth/login`

---

## Usuario y contraseña de prueba

- Usuario: `michaelw`
- Contraseña: `michaelwpass`

---

## Ejemplo curl de login

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"michaelw", "password":"michaelwpass"}'
```

Si el login es exitoso este es un ejemplo Respuesta esperada:

```json
{
    "accessToken": "accessToken",
    "email": "michael.williams@x.dummyjson.com",
    "firstName": "Michael",
    "id": 2,
    "lastName": "Williams",
    "refreshToken": "refreshToken",
    "username": "michaelw"
}
```

---

## Cómo se guarda el registro de login

Actualmente los registros de login exitoso en la tabla  `login_log` de la base de datos `dummyauth` de Postgresql:

**Campos almacenados:**  
- id (uuid)  
- username  
- login_time  
- access_token  
- refresh_token

**El archivo para la creacion de la base de datos se llama dummyauth.sql y esta dentro de la carpeta db**
