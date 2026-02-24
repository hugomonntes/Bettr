package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersManager {

    private final String url = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private final String user = "postgres.mqborjmvfvlemhewhscw";
    private final String password = "jsrMYBg6aAz-V9d4FfGwxw";

    // 1. REGISTRO DE USUARIO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Users user) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO users (name, username, email, password_hash) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getUsername());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getPassword_hash());
                    pstmt.executeUpdate();
                }
            }
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 2. LOGIN
    @GET
    @Path("/{username}/{password_hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserMatchPassword(@PathParam("username") String username, @PathParam("password_hash") String password_hash) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT id, name, username, email FROM users WHERE username = ? AND password_hash = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, password_hash);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            Users found = new Users();
                            found.setId(rs.getInt("id"));
                            found.setName(rs.getString("name"));
                            found.setUsername(rs.getString("username"));
                            found.setEmail(rs.getString("email"));
                            return Response.ok(found).build();
                        } else {
                            return Response.status(Response.Status.UNAUTHORIZED).build();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 2.1. OBTENER USUARIO POR ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT id, name, username, email, description, avatar FROM users WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            Users u = new Users();
                            u.setId(rs.getInt("id"));
                            u.setName(rs.getString("name"));
                            u.setUsername(rs.getString("username"));
                            u.setEmail(rs.getString("email"));
                            u.setDescription(rs.getString("description"));
                            u.setAvatar(rs.getString("avatar"));
                            return Response.ok(u).build();
                        } else {
                            return Response.status(Response.Status.NOT_FOUND).build();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 3. BUSCAR USUARIOS
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("search") String search) {
        List<Users> usersList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT id, name, username, email, description, avatar FROM users";
                if (search != null && !search.isEmpty()) {
                    query += " WHERE username ILIKE ? OR name ILIKE ?";
                }
                query += " ORDER BY username ASC";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    if (search != null && !search.isEmpty()) {
                        pstmt.setString(1, "%" + search + "%");
                        pstmt.setString(2, "%" + search + "%");
                    }
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Users u = new Users();
                            u.setId(rs.getInt("id"));
                            u.setName(rs.getString("name"));
                            u.setUsername(rs.getString("username"));
                            u.setEmail(rs.getString("email"));
                            u.setDescription(rs.getString("description"));
                            u.setAvatar(rs.getString("avatar"));
                            usersList.add(u);
                        }
                    }
                }
            }
            return Response.ok(usersList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 4. SEGUIR A UN USUARIO
    @POST
    @Path("/follow/{followerId}/{followingId}")
    public Response followUser(@PathParam("followerId") int fId, @PathParam("followingId") int flId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO Followers (follower_id, following_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, fId);
                    pstmt.setInt(2, flId);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 5. DEJAR DE SEGUIR A UN USUARIO
    @DELETE
    @Path("/unfollow/{followerId}/{followingId}")
    public Response unfollowUser(@PathParam("followerId") int fId, @PathParam("followingId") int flId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "DELETE FROM Followers WHERE follower_id = ? AND following_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, fId);
                    pstmt.setInt(2, flId);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 6. OBTENER LISTA DE SEGUIDORES
    @GET
    @Path("/{userId}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowers(@PathParam("userId") int userId) {
        List<Users> followers = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT u.id, u.name, u.username, u.email, u.description, u.avatar " +
                              "FROM Users u " +
                              "JOIN Followers f ON u.id = f.follower_id " +
                              "WHERE f.following_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Users u = new Users();
                            u.setId(rs.getInt("id"));
                            u.setName(rs.getString("name"));
                            u.setUsername(rs.getString("username"));
                            u.setEmail(rs.getString("email"));
                            u.setDescription(rs.getString("description"));
                            u.setAvatar(rs.getString("avatar"));
                            followers.add(u);
                        }
                    }
                }
            }
            return Response.ok(followers).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 7. OBTENER LISTA DE USUARIOS QUE SIGUE
    @GET
    @Path("/{userId}/following")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowing(@PathParam("userId") int userId) {
        List<Users> following = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT u.id, u.name, u.username, u.email, u.description, u.avatar " +
                              "FROM Users u " +
                              "JOIN Followers f ON u.id = f.following_id " +
                              "WHERE f.follower_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            Users u = new Users();
                            u.setId(rs.getInt("id"));
                            u.setName(rs.getString("name"));
                            u.setUsername(rs.getString("username"));
                            u.setEmail(rs.getString("email"));
                            u.setDescription(rs.getString("description"));
                            u.setAvatar(rs.getString("avatar"));
                            following.add(u);
                        }
                    }
                }
            }
            return Response.ok(following).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 8. OBTENER ESTADÍSTICAS DEL USUARIO
    @GET
    @Path("/{userId}/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserStats(@PathParam("userId") int userId) {
        Map<String, Integer> stats = new HashMap<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                
                // Contar seguidores
                String followersQuery = "SELECT COUNT(*) FROM Followers WHERE following_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(followersQuery)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            stats.put("followers", rs.getInt(1));
                        }
                    }
                }
                
                // Contar siguiendo
                String followingQuery = "SELECT COUNT(*) FROM Followers WHERE follower_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(followingQuery)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            stats.put("following", rs.getInt(1));
                        }
                    }
                }
                
                // Contar hábitos
                String habitsQuery = "SELECT COUNT(*) FROM Habits WHERE user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(habitsQuery)) {
                    pstmt.setInt(1, userId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            stats.put("habits", rs.getInt(1));
                        }
                    }
                }
            }
            return Response.ok(stats).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 9. VERIFICAR SI UN USUARIO SIGUE A OTRO
    @GET
    @Path("/isfollowing/{followerId}/{followingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isFollowing(@PathParam("followerId") int fId, @PathParam("followingId") int flId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT COUNT(*) FROM Followers WHERE follower_id = ? AND following_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, fId);
                    pstmt.setInt(2, flId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            boolean isFollowing = rs.getInt(1) > 0;
                            return Response.ok("{\"following\": " + isFollowing + "}").build();
                        }
                    }
                }
            }
            return Response.ok("{\"following\": false}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 10. ACTUALIZAR PERFIL
    @POST
    @Path("/{id}/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("id") int id, Users userBody) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                // Actualizamos la descripción y el campo avatar (que recibe el Base64)
                String query = "UPDATE users SET description = ?, avatar = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, userBody.getDescription());
                    pstmt.setString(2, userBody.getAvatar()); // Guardamos el Base64 como texto
                    pstmt.setInt(3, id);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // 11. EDITAR DESCRIPCIÓN SIMPLE
    @POST
    @Path("/{id}/description")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDescription(@PathParam("id") int id, Users userBody) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "UPDATE users SET description = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, userBody.getDescription());
                    pstmt.setInt(2, id);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
