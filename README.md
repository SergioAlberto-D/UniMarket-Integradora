# UniMarket MUA
Sergio Alberto-D-20253ds104 
dulcecanseco-20253ds119
Aioros2806-20243ds092
luisfernandorodriguezrayo-20253ds118

> Marketplace universitario para la publicación, búsqueda y gestión de artículos entre usuarios de la comunidad universitaria.

##  Descripción

**UniMarket MUA** es una aplicación web desarrollada como proyecto integrador. La plataforma permite a los usuarios registrarse, iniciar sesión, publicar artículos, consultar publicaciones, realizar ofertas, comprar artículos y gestionar su perfil.

El sistema también incluye funciones administrativas para la gestión de usuarios, categorías, publicaciones, actividad y reportes.

La aplicación está construida como una aplicación web Java con **Maven**, empaquetada como **WAR**, y utiliza **JSP, Servlets, JSTL, Oracle Database y HikariCP**. El proyecto utiliza Java 21 y Jakarta Servlet 6.1.0.

##  Funcionalidades principales

###  Usuarios
- Registro de nuevos usuarios.
- Inicio y cierre de sesión.
- Verificación de cuenta.
- Recuperación y cambio de contraseña.
- Gestión del perfil.
- Actualización de teléfono y fotografía de perfil.
- Consulta del historial de actividad.
- Sistema de notificaciones.

###  Marketplace
- Visualización de artículos publicados.
- Búsqueda de artículos.
- Consulta de detalles de productos.
- Publicación de artículos.
- Edición y eliminación de publicaciones.
- Carga de imágenes de artículos.
- Sistema de ofertas.
- Compra de artículos.
- Registro de transacciones.
- Comentarios y respuestas.

###  Administración
- Gestión de usuarios.
- Gestión de categorías.
- Gestión de publicaciones.
- Consulta de actividad.
- Generación y consulta de reportes.
- Filtros de acceso para proteger las funciones administrativas.

##  Arquitectura

El proyecto está organizado siguiendo una separación por capas:

```text
src/
└── main/
    ├── java/
    │   └── com/unimarket/unimarketintegradora/
    │       ├── controller/     # Servlets y filtros
    │       ├── model/          # Entidades y DTOs
    │       │   └── dao/        # Objetos de acceso a datos
    │       └── utils/          # Utilidades y conexión a BD
    │
    └── webapp/
        ├── admin/              # Vistas del administrador
        ├── assets/             # CSS y JavaScript
        ├── components/         # Componentes JSP
        ├── error/              # Páginas de error
        ├── includes/           # Inclusiones JSP
        ├── static/             # Recursos estáticos
        └── *.jsp               # Vistas principales
```

Los Servlets gestionan las peticiones HTTP y la lógica de cada funcionalidad, mientras que los DAO encapsulan las operaciones con la base de datos. La conexión se administra mediante un pool de conexiones HikariCP.

##  Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Maven | Gestión y construcción del proyecto |
| Jakarta Servlet | Controladores web |
| JSP | Vistas |
| JSTL | Lógica y etiquetas en JSP |
| Oracle Database | Persistencia de datos |
| JDBC | Acceso a la base de datos |
| HikariCP | Pool de conexiones |
| Jakarta Mail | Envío de correos |
| HTML / CSS | Interfaz web |
| JavaScript | Interactividad del frontend |
| Bootstrap 5 | Componentes y estilos responsive |

El `pom.xml` define Java 21, empaquetado `WAR`, Jakarta Servlet 6.1.0, Oracle JDBC 23.4.0.24.05, HikariCP 6.0.0 y Jakarta Mail 2.1.3/2.0.4.

##  Requisitos

Antes de ejecutar el proyecto se recomienda contar con:

- **JDK 21**
- **Maven** o utilizar el Maven Wrapper incluido (`mvnw` / `mvnw.cmd`)
- Una instancia de **Oracle Database** configurada para el proyecto.
- El **Oracle Wallet** requerido para la conexión.
- Un servidor compatible con Jakarta Servlet para desplegar el archivo WAR.
- Credenciales de base de datos configuradas mediante variables de entorno o `credentials.properties`.
- Credenciales SMTP si se utilizan las funciones de correo.

## Configuración

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd UniMarket-Integradora
```

### 2. Configurar las credenciales

El proyecto puede obtener las credenciales de la base de datos mediante variables de entorno:

```text
DB_USER=usuario
DB_PASS=contraseña
DB_NAME=nombre_o_alias_de_la_bd
```

También contempla un archivo `credentials.properties` como alternativa:

```properties
db.user=usuario
db.pass=contraseña
db.name=nombre_o_alias_de_la_bd
```

**Importante:** no subas credenciales reales, contraseñas, wallets ni secretos al repositorio público.

### 3. Configurar el correo

Para el envío de correos se utilizan las variables:

```text
SMTP_USER=correo@ejemplo.com
SMTP_PASS=contraseña_o_credencial_smtp
```

Como alternativa, el sistema puede leer:

```properties
smtp.user=correo@ejemplo.com
smtp.pass=contraseña_o_credencial_smtp
```

El envío utiliza el servidor SMTP de Gmail mediante TLS.

### 4. Configurar Oracle Wallet

La conexión a Oracle utiliza un Wallet ubicado como recurso de la aplicación. Verifica que el Wallet necesario para tu entorno esté configurado correctamente antes de ejecutar el proyecto.

### 5. Construir el proyecto

Con Maven:

```bash
mvn clean package
```

En Linux/macOS utilizando el wrapper:

```bash
./mvnw clean package
```

En Windows:

```bat
mvnw.cmd clean package
```

El proyecto genera un archivo **WAR** para su despliegue.

##  Ejecución

Después de construir el proyecto, despliega el WAR generado en un servidor compatible con Jakarta Servlet.

Una vez iniciado el servidor, accede a la aplicación mediante la URL correspondiente a tu entorno, por ejemplo:

```text
http://localhost:8080/UniMarket-Integradora/
```

> La URL exacta puede cambiar dependiendo del nombre con el que el servidor despliegue el archivo WAR.

##  Seguridad

El proyecto incorpora varias medidas relacionadas con autenticación y protección de información:

- Las contraseñas de usuarios se almacenan mediante **SHA-256**.
- Se utilizan sesiones HTTP para mantener la autenticación.
- Existen filtros para controlar el acceso de usuarios y administradores.
- Las credenciales de base de datos y SMTP pueden mantenerse fuera del código mediante variables de entorno.
- Las operaciones con base de datos utilizan `PreparedStatement`.
- Las contraseñas nuevas cuentan con validaciones de complejidad.

##  Módulos principales

### Controladores

Los Servlets principales incluyen funcionalidades como:

- `LoginServlet`
- `LogoutServlet`
- `RegisterServlet`
- `PublicarArticuloServlet`
- `EditarArticuloServlet`
- `EliminarArticuloServlet`
- `ComprarArticuloServlet`
- `OfertarServlet`
- `ComentarServlet`
- `MiPerfilServlet`
- `MisArticulosServlet`
- `HistorialActividadServlet`
- `NotificacionesServlet`
- `SolicitarRecuperacionServlet`
- `VerificarCuentaServlet`

Además, existen controladores específicos para administración.

### DAO

La capa DAO contiene clases para trabajar con entidades como:

- Usuarios
- Administradores
- Artículos
- Categorías
- Comentarios
- Ofertas
- Notificaciones
- Transacciones
- Roles
- Divisiones académicas
- Imágenes de artículos
- Reportes

##  Base de datos

La aplicación utiliza **Oracle Database** y una conexión JDBC administrada por **HikariCP**.

La clase `SQLConnector` obtiene las credenciales desde variables de entorno o `credentials.properties`, configura el controlador Oracle y crea un pool de conexiones.

##  Sistema de correo

`EmailSender` utiliza Jakarta Mail para enviar correos mediante:

```text
Servidor SMTP: smtp.gmail.com
Puerto: 587
TLS: habilitado
```

Las credenciales se obtienen desde variables de entorno o desde `credentials.properties`.

##  Pruebas y construcción

Para limpiar y construir el proyecto:

```bash
mvn clean package
```

Para omitir pruebas durante una construcción:

```bash
mvn clean package -DskipTests
```

##  Consideraciones

- No publicar `credentials.properties` con credenciales reales.
- No publicar contraseñas, tokens ni información sensible.
- Verificar que el Oracle Wallet corresponda al entorno configurado.
- La aplicación requiere una base de datos Oracle correctamente preparada.
- La URL de despliegue depende del servidor y del nombre final del WAR.

##  Documentación

La documentación técnica del proyecto incluye documentación JavaDoc, documentación de las vistas JSP y JavaScript, además del mapa técnico del sitio.

##  Proyecto

**UniMarket MUA — Proyecto Integrador**

Aplicación web orientada a facilitar el intercambio y comercialización de artículos dentro de una comunidad universitaria.

---

###  Licencia

Este proyecto fue desarrollado con fines académicos.
