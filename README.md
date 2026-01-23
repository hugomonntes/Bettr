# SEGUIMIENTO DESARROLLO DEL PROYECTO
## 1ª SEMANA DE DESARROLLO ( 25 DIC - 31 DIC 2025 )

Creación de la base de datos de Bettr, a partir del diseño realizado en el anteproyecto he desarrollado la base de datos MySQL, cuenta con 9 tablas relacionadas entre sí, actualmente tengo la base de datos alojada en un servidor gratuito que cuenta con 25MB de almacenamiento (FreeDB), es poco pero suficiente de momento.

Realizar la versión web de Bettr en concreto, el diseño del **Login** y el **Registro**.

## 2ª SEMANA DE DESARROLLO ( 1 ENE - 8 ENE 2026 )

Crear el Backend del Login y el Registro de la versión web, lo he diseñado con **PHP** mediante un patrón de diseño como es el **Modelo DAO** (Data Access Object) este es un patrón de diseño para separar las iteracciones de la aplicación y la base de datos, lo que significa que trabaja como una capa intermedia entre ambos.

Crear la validación tanto del Registro como del Login, es decir que si un usuario ya está registrado con un username o con un correo ya existente no pueda volver a registrarse, hacer comprobación con doble contraseña al registrarse, en cuento a la validación del Login solamente he comprobado que el username exista en la base de datos y que la contraseña coincida con ese username del usuario guardado previamente en la base de datos.

## 3ª SEMANA DE DESARROLLO ( 10 ENE - 19 ENE 2026 )

Crear el Registro y Login de la aplicación movil tanto el frontend (aún provisional sin acabar) y el backend creado con la lógica y la conexión con la base de datos, además de crear las clases y el sistema de archivos necesario (Modelo DAO).

## 4ª SEMANA DE DESARROLLO ( 19 ENE -  ACTUALIDAD )

Crear frontend de los layouts de la aplicación movil sin funcionalidad actualmente pero con el diseño ya casi realizado de algunas de las views.

Crear diseños layouts de la app movil, intro, login, registro y funcionalidad en los intents para pasar de un layout a otro.
