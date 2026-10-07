# Aplicación de Gestión de Contactos (Spring Boot)

¡Bienvenido/a al proyecto de Gestión de Contactos! Si es tu primera vez trabajando con **Spring Boot** o con una aplicación de este tipo, esta guía está diseñada para explicarte paso a paso qué herramientas estamos utilizando, cómo se estructuran y cómo interactúan entre sí.

---

## 1. ¿Qué es Spring Boot?

**Spring Boot** es un framework de Java diseñado para simplificar la creación de aplicaciones web. Tradicionalmente, configurar una aplicación Java requería escribir muchos archivos XML o clases de configuración extensas. Spring Boot soluciona esto mediante su filosofía de **"Autoconfiguración"** (configura por ti la mayoría de cosas habituales) y proporcionando un servidor web integrado (normalmente Tomcat), lo que significa que puedes ejecutar la aplicación directamente sin tener que instalar un servidor web externo.

### Conceptos clave:
*   **Inversión de Control (IoC) e Inyección de Dependencias (DI):** En lugar de que tú crees los objetos manualmente (ej. `new MiClase()`), Spring Boot se encarga de instanciarlos y "pasarlos" (inyectarlos) donde se necesitan. A estos objetos gestionados por Spring se les llama **Beans**.

---

## 2. Tecnologías Utilizadas en este Proyecto

Esta aplicación utiliza varias partes del ecosistema Spring y otras herramientas complementarias:

*   **Spring Web (MVC):** Se encarga de recibir las peticiones web (HTTP) de los usuarios, procesarlas y devolver una respuesta (una página web HTML, por ejemplo).
*   **Spring Data JPA:** Facilita enormemente la comunicación con la base de datos. En lugar de escribir complejas sentencias SQL manuales, definimos "Entidades" (clases Java) y "Repositorios" (interfaces), y Spring hace el trabajo pesado.
*   **MySQL:** Es el sistema de gestión de base de datos relacional donde se guarda la información permanentemente (usuarios, contactos, etc.).
*   **Thymeleaf:** Es un motor de plantillas. Permite escribir archivos HTML normales pero añadirles variables y lógica básica (como bucles o condiciones) que el servidor rellena con los datos reales antes de enviar la página final al navegador.
*   **Spring Security:** Protege la aplicación. Asegura que ciertas páginas solo puedan ser vistas por usuarios que han iniciado sesión y proporciona herramientas robustas para el registro y el encriptado de contraseñas.
*   **Bootstrap:** Un framework de CSS (usado en las plantillas Thymeleaf) para que la aplicación se vea moderna, estructurada y responsiva (adaptable a móviles) sin tener que escribir casi nada de CSS manual.

---

## 3. Estructura del Proyecto

Los proyectos Spring Boot siguen una arquitectura de capas bien definida. Generalmente encontrarás este orden de carpetas dentro de `src/main/java/com/example/contactos/`:

*   📁 **`controller` (Controladores):** Son los "recepcionistas". Reciben las peticiones del navegador (ej. "quiero ver la lista de contactos" o "quiero enviar este formulario de registro"), piden los datos necesarios a los servicios o repositorios y le dicen a Thymeleaf qué plantilla HTML debe pintar.
*   📁 **`service` (Servicios):** Contienen la "lógica de negocio". Son intermediarios entre los controladores y la base de datos. Aquí se hacen las comprobaciones, validaciones o transformaciones de datos.
*   📁 **`repository` (Repositorios):** Se comunican directamente con la base de datos. Con solo declarar una interfaz que extienda de `JpaRepository`, ya obtienes métodos para guardar, borrar y buscar datos sin escribir SQL.
*   📁 **`entity` o `model` (Entidades):** Son clases Java normales (POJOs) que representan las tablas de la base de datos. Cada propiedad o atributo de la clase corresponde a una columna en la tabla.
*   📁 **`config` (Configuraciones):** Contiene clases especiales de configuración técnica, como `SecurityConfig.java`, donde se define a qué rutas se puede acceder sin iniciar sesión y cuáles están protegidas.

Además, en la carpeta `src/main/resources/`:
*   📁 **`templates/`**: Aquí están guardados todos los archivos HTML diseñados con Thymeleaf (vistas).
*   📄 **`application.properties`**: Es el archivo principal de configuración. Aquí se introducen las credenciales de la base de datos, el puerto en el que arrancará el servidor web, etc.

---

## 4. ¿Cómo funciona? (El Flujo de Vida de una Petición)

Para entender cómo se conectan las piezas, imagina que un usuario entra en la página principal para ver sus contactos. Esto es lo que ocurre internamente paso a paso:

1.  **El Navegador hace la petición:** El usuario introduce `http://localhost:8080/` y pulsa Enter.
2.  **Seguridad interviene:** El filtro de Spring Security recibe la petición primero y verifica si el usuario ha iniciado sesión. Si no lo está, intercepta la petición y lo redirige forzosamente a la página de login (`/login`). Si lo está, la deja pasar.
3.  **El Controlador la recibe:** El `ContactoController` tiene un método preparado (con la anotación `@GetMapping("/")`) que es el encargado de atender esa ruta.
4.  **Búsqueda en Base de Datos:** El Controlador le pide al Repositorio o Servicio: "dame todos los contactos que pertenezcan a este usuario". El Repositorio hace la consulta a MySQL de forma automática, recoge los datos y los convierte en objetos de Java (la entidad `Contacto`).
5.  **Se prepara la vista (Modelo):** El Controlador coge esa lista de objetos `Contacto` y la guarda en una "caja" especial llamada `Model`, dándole un nombre (por ejemplo, `contactos`).
6.  **Renderizado en Thymeleaf:** El Controlador termina su función diciendo "ahora usa la plantilla `inicio.html`". El motor Thymeleaf abre ese archivo HTML, busca dónde hemos puesto instrucciones (como `th:each` para hacer bucles), pinta un bloque HTML por cada contacto que había en el `Model` y genera una página HTML final totalmente limpia.
7.  **Respuesta al usuario:** Ese HTML final se envía de vuelta al navegador del usuario, que lo dibuja en pantalla.

---

## 5. Gestión de Usuarios y Seguridad

La aplicación cuenta con un flujo completo de autenticación:

*   **Registro (`/register`)**: Permite guardar un usuario nuevo. La contraseña nunca se guarda en texto plano, sino que se encripta (normalmente con un algoritmo llamado BCrypt) antes de insertarse en la base de datos.
*   **Inicio de sesión (`/login`)**: Al intentar entrar, entra en juego la clase `UsuarioDetailsService`. Spring Security utiliza esta clase para buscar al usuario por su email en la base de datos. Si lo encuentra, coge su contraseña encriptada y comprueba si coincide con la que acaba de escribir el usuario.
*   **Permisos**: En `SecurityConfig.java` hemos delimitado con reglas muy claras que todo el mundo pueda registrarse o intentar loguearse (rutas públicas), pero nadie pueda ver los contactos ni modificarlos si no ha pasado el control de login (rutas privadas).

---

## 6. Cómo ejecutar la aplicación localmente

**Requisitos previos:**
1.  Tener instalado el entorno de desarrollo de Java (**JDK 21**).
2.  Tener instalado **Maven** (el gestor de paquetes de Java).
3.  Tener instalado **MySQL** y ejecutándose en tu ordenador. Deberás crear una base de datos vacía y asegurarte de que el usuario, contraseña y nombre de la base de datos en el archivo `application.properties` coincidan con los de tu instalación de MySQL local.

**Pasos para arrancar:**
1.  Abre una terminal en la raíz del proyecto (la carpeta donde se encuentra el archivo `pom.xml`).
2.  Ejecuta el siguiente comando:
    ```bash
    mvn spring-boot:run
    ```
    *(Si usas un IDE moderno como IntelliJ IDEA, Eclipse o VS Code, simplemente puedes darle al botón de "Run" o "Play" sobre la clase principal `SpringbootContactosApplication.java`).*
3.  Verás que la terminal empieza a escupir mucho texto (logs). Cuando veas una línea que dice algo parecido a `Started SpringbootContactosApplication in X.X seconds`, significa que el servidor ya está funcionando.
4.  Abre tu navegador de internet favorito y dirígete a: `http://localhost:8080`.
5.  ¡Crea una cuenta, inicia sesión y empieza a probar la aplicación!
