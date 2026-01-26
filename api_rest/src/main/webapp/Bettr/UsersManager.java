package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersManager {
    String url = "http://sql.freedb.tech:3306/freedb_hmontes/freedb_BettrDB";
    String user = "freedb_hmontes";
    String password = "pMEn7Hq3e9nYb$$";

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(Users user) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO usuarios (name, username, email, password_hash) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getUsername());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getPassword());
                    pstmt.executeUpdate();
                }
            }
            return Response.status(Response.Status.CREATED).build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
