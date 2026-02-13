package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/habits")
public class HabitManager {
    // Conexión a la base de datos
    String url = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:5432/postgres?sslmode=require";
    String user = "postgres.mqborjmvfvlemhewhscw";
    String password = "jsrMYBg6aAz-V9d4FfGwxw";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addHabit(Habit habit) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO habits (user_id, description, image_url, habit_type) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, habit.getUser_id());
                    pstmt.setString(2, habit.getDescription());
                    pstmt.setString(3, habit.getImage_url());
                    pstmt.setString(4, habit.getHabit_type());
                    pstmt.executeUpdate();
                }
            }
            return Response.ok().build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
