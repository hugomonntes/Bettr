# Bettr Web App - Mejoras y Correcciones

## Tareas completadas:

### 1. API REST - Añadir endpoints necesarios
- [x] GET /users/{id}/followers - Obtener seguidores
- [x] GET /users/{id}/following - Obtener siguiendo
- [x] DELETE /users/unfollow/{followerId}/{followingId} - Dejar de seguir
- [x] POST /habits/{id}/like - Dar like
- [x] DELETE /habits/{id}/like - Quitar like
- [x] GET /users/{id}/stats - Obtener estadísticas del usuario
- [x] GET /users/isfollowing/{followerId}/{followingId} - Verificar si sigue
- [x] GET /habits/{habitId}/isliked/{userId} - Verificar si dio like
- [x] GET /habits/user/{userId} - Obtener hábitos de un usuario

### 2. Web App - Mejoras
- [x] Actualizar dashboard.js con funcionalidad de dejar de seguir
- [x] Mostrar estadísticas reales en el perfil
- [x] Añadir lista de seguidores/siguiendo
- [x] Implementar sistema de likes
- [x] Actualizar api_proxy.php para soportar DELETE con body
- [x] Actualizar ApiClient.php para soportar DELETE con body

### 3. Pendiente - Base de datos
- [ ] Crear tabla Habit_Likes si no existe:
  ```sql
  CREATE TABLE IF NOT EXISTS Habit_Likes (
      id SERIAL PRIMARY KEY,
      habit_id INTEGER REFERENCES Habits(id) ON DELETE CASCADE,
      user_id INTEGER REFERENCES Users(id) ON DELETE CASCADE,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      UNIQUE(habit_id, user_id)
  );
  ```

### 4. Pendiente - Compilar API REST
- [ ] Compilar y desplegar la API REST actualizada a Render

