package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersManager {
    // Dotenv dotenv = Dotenv.load(); // TODO no encuentra el archivo .env

    // String url = dotenv.get("DB_URL");
    // String user = dotenv.get("DB_USER");
    // String password = dotenv.get("DB_PASSWORD");

    // Base de datos alojada en Supabase tengo dos host mas por si falla el
    // principal
    String url = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    String user = "postgres.mqborjmvfvlemhewhscw";
    String password = "jsrMYBg6aAz-V9d4FfGwxw";

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
            return Response.ok().build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/editName")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editName(Users user) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "UPDATE users SET name=? WHERE username=?";

                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getUsername());
                    pstmt.executeUpdate();
                }
            }
            return Response.ok().build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/editUserName/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserName(Users user, @PathParam("username") String username) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "UPDATE users SET username=? WHERE username=?";

                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, user.getUsername());
                    pstmt.executeUpdate();
                }
            }
            return Response.ok().build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        ArrayList<Users> usersList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM users";
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String name = rs.getString("name");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String password_hash = rs.getString("password_hash");
                    Users user = new Users(name, username, email, password_hash);
                    usersList.add(user);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.ok(usersList).build();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIdFromUsername(@PathParam("username") String username) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                Statement stmt = conn.createStatement();
                String query = String.format("SELECT * FROM users WHERE username = '%s'", username);
                ResultSet rs = stmt.executeQuery(query);
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt("id");
                }
                return Response.ok(id).build();
            }
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}/{password_hash}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserMatchPassword(@PathParam("username") String username, @PathParam("password_hash") String password_hash) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement stm = connection.createStatement();
            String query = String.format("SELECT * FROM users WHERE username = '%s' AND password_hash = '%s'", username,
                    password_hash);
            ResultSet rs = stm.executeQuery(query);
            return Response.ok(String.format("Usuario encontrado: %s", username)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/{description}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postDescription(@PathParam("id") int id, @PathParam("description") String description) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement stm = connection.createStatement();
            String query = String.format("UPDATE users SET description = '%s' WHERE id = %d", description, id);
            int complete = stm.executeUpdate(query);
            return Response.ok(complete).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

