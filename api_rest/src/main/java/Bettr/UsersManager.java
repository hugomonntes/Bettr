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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersManager {
    String url = "jdbc:mysql://sql.freedb.tech:3306/freedb_BettrDB";
    String user = "freedb_hmontes";
    String password = "pMEn7Hq3e9nYb$$";

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Users user) { // TODO hashear password
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO usuarios (name, username, email, password_hash) VALUES (?, ?, ?, ?)";
                
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

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        ArrayList<Users> usersList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM usuarios";
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
}
