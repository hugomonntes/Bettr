# SEGUIMIENTO DESARROLLO DEL PROYECTO
## 1ª SEMANA DE DESARROLLO ( 25 DIC - 31 DIC 2025 )

Creación de la base de datos de Bettr, a partir del diseño realizado en el anteproyecto he desarrollado la base de datos MySQL, cuenta con 9 tablas relacionadas entre sí, actualmente tengo la base de datos alojada en un servidor gratuito que cuenta con 25MB de almacenamiento (FreeDB), es poco pero suficiente de momento.

Realizar la versión web de Bettr en concreto, el diseño del **Login** y el **Registro**.

## 2ª SEMANA DE DESARROLLO ( 1 ENE - 8 ENE 2026 )

Crear el Backend del Login y el Registro de la versión web, lo he diseñado con **PHP** mediante un patrón de diseño como es el **Modelo DAO** (Data Access Object) este es un patrón de diseño para separar las iteracciones de la aplicación y la base de datos, lo que significa que trabaja como una capa intermedia entre ambos.

Crear la validación tanto del Registro como del Login, es decir que si un usuario ya está registrado con un username o con un correo ya existente no pueda volver a registrarse, hacer comprobación con doble contraseña al registrarse, en cuento a la validación del Login solamente he comprobado que el username exista en la base de datos y que la contraseña coincida con ese username del usuario guardado previamente en la base de datos.

## 3ª SEMANA DE DESARROLLO ( 10 ENE - 19 ENE 2026 )

Crear el Registro y Login de la aplicación movil tanto el frontend (aún provisional sin acabar) y el backend creado con la lógica y la conexión con la base de datos, además de crear las clases y el sistema de archivos necesario (Modelo DAO).

## 4ª SEMANA DE DESARROLLO ( 20 ENE -  27 ENE 2026)

Crear frontend de los layouts de la aplicación movil sin funcionalidad actualmente pero con el diseño ya casi realizado de algunas de las views.

Crear diseños layouts de la app movil, intro, login, registro y funcionalidad en los intents para pasar de un layout a otro.
Comienzo desarrollo layout principal, donde se visualizarán todos los posts (hábitos diarios de cada usuario en mi caso) y las streaks que son las rachas de dias completados de cada usuario.

Realizar API REST para todas las plataformas, gestionando métodos y clases para mantenerlo organizado sin mezclar código.

## 5ª SEMANA DE DESARROLLO ( 28 ENE -  5 FEB 2026)

Crear Layouts aplicación movil añadirle animaciones, elementos y funcionalidad mediante la Api, crear las funciones necesarias para realizar el login y el registro además de la funcionalidad de poder editar cualquier campo de tu perfil (nombre, username, email...).

Añadir funciones a la API para la devolucion de un usuario mediante su username y password, además de comenzar con la creación del feed de la aplicación móvil.

Crear desarrollo de las diferentes vistas de la aplicaión así como la navegación por ellas mediante la barra de navegación, creadas por partes mediante fragmentos de código unificados en la view final por si quiero reutilizarlos en otras views algunos componentes.

Implementación de un servidor gratuito para la API REST (Render), mejorando el acceso a la aplicación desde cualquier plataforma o dispositivo sin depender de un servidor local arrancado en la misma red que el dispositivo desde el que se quiere acceder a la app.

## 6ª SEMANA DE DESARROLLO ( 6 FEB -  12 FEB 2026)

Añadir métodos que me van haciendo falta a la Api como por ejemplo buscar un usuario mediante su id, para poder completar su perfil después de registrarse.

Editar Endpoints de la API REST tomando buenas prácticas a la hora de definirlos.

Añadir nueva funcionalidad a la API REST para la gestión de hábitos diarios, creando las clases `Habit.java` y `HabitManager.java` que permiten a los usuarios publicar sus hábitos con imagen y descripción.

Desarrollar nueva vista en la aplicación móvil para publicar hábitos:
- Creación del `CameraFragment` con funcionalidad para tomar fotos con la cámara o seleccionar imágenes de la galería
- Implementación del layout `fragment_camera.xml` con diseño moderno y oscuro
- Integración con la API para publicar hábitos con descripción e imagen
- Botones para cámara, galería y publicación de hábitos

## 7ª SEMANA DE DESARROLLO ( 13 FEB - ACTUALIDAD 2026)

Migración completa de la base de datos de MySQL a **PostgreSQL** en **Supabase**, aprovechando las ventajas de un backend como servicio con autenticación integrada, almacenamiento de archivos y API automática.

Implementación del **sistema de Feed** en la API REST:
- Nuevo endpoint `/habits/feed/{userId}` que devuelve los hábitos de los usuarios que sigues y los tuyos propios
- Consulta SQL optimizada con JOIN entre tablas para obtener el username del creador del hábito
- Ordenación por fecha de creación descendente

Implementación del **sistema de seguidores**:
- Endpoint para seguir a usuarios `/users/follow/{followerId}/{followingId}`
- Uso de "UPSERT" con `ON CONFLICT DO NOTHING` para evitar duplicados
- Preparado para futura funcionalidad de dejar de seguir

Implementación de **búsqueda de usuarios**:
- Nuevo endpoint `/users?search={termino}` para buscar usuarios por nombre o username
- Uso de `ILIKE` para búsqueda case-insensitive en PostgreSQL

Mejora del **perfil de usuario**:
- Endpoint para actualizar descripción y avatar `POST /users/{id}/profile`
- Endpoint específico para editar solo la descripción `POST /users/{id}/description`
- Almacenamiento de avatar en Base64

Sistema de **tipos de hábitos**:
- Campo `habit_type` en el modelo para categorizar hábitos
- Preparado para futuras estadísticas y filtros por tipo

Implementación de nuevos componentes en la aplicación móvil:
- Mejora del sistema de navegación con fragmentos
- Integración con los nuevos endpoints de la API
- Preparación para el sistema de follows
