package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
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

    // 3. BUSCAR USUARIOS (Con filtro opcional por nombre/username)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("search") String search) {
        List<Users> usersList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT id, name, username, email FROM users";
                if (search != null && !search.isEmpty()) {
                    query += " WHERE username ILIKE ? OR name ILIKE ?";
                }
                
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
                // ON CONFLICT DO NOTHING evita errores si ya lo sigue
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

    // 5. EDITAR DESCRIPCIÓN
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