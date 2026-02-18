package Bettr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/habits")
public class HabitManager {
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

    @GET
    @Path("/feed/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeedForUser(@PathParam("userId") int userId) {
        List<Habit> feedHabits = new ArrayList<>();
        String sql = "SELECT h.*, u.username, " +
                "(SELECT COUNT(*) FROM Habit_Likes WHERE habit_id = h.id) as likes_count " +
                "FROM Habits h " +
                "JOIN Users u ON h.user_id = u.id " +
                "WHERE h.user_id IN (SELECT following_id FROM Followers WHERE follower_id = ?) " +
                "   OR h.user_id = ? " +
                "ORDER BY h.created_at DESC";

        try (Connection conn = DriverManager.getConnection(url, this.user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Habit habit = new Habit();
                habit.setId(rs.getInt("id"));
                habit.setUser_id(rs.getInt("user_id"));
                habit.setDescription(rs.getString("description"));
                habit.setImage_url(rs.getString("image_url"));
                habit.setHabit_type(rs.getString("habit_type"));
                habit.setUsername(rs.getString("username")); 
                habit.setLikes_count(rs.getInt("likes_count"));
                habit.setCreated_at(rs.getString("created_at"));
                feedHabits.add(habit);
            }
            return Response.ok(feedHabits).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Like a habit
    @POST
    @Path("/{habitId}/like/{userId}")
    public Response likeHabit(@PathParam("habitId") int habitId, @PathParam("userId") int userId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "INSERT INTO Habit_Likes (habit_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, habitId);
                    pstmt.setInt(2, userId);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Unlike a habit
    @DELETE
    @Path("/{habitId}/like/{userId}")
    public Response unlikeHabit(@PathParam("habitId") int habitId, @PathParam("userId") int userId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "DELETE FROM Habit_Likes WHERE habit_id = ? AND user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, habitId);
                    pstmt.setInt(2, userId);
                    pstmt.executeUpdate();
                    return Response.ok().build();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Check if user liked a habit
    @GET
    @Path("/{habitId}/isliked/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isHabitLiked(@PathParam("habitId") int habitId, @PathParam("userId") int userId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
                String query = "SELECT COUNT(*) FROM Habit_Likes WHERE habit_id = ? AND user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, habitId);
                    pstmt.setInt(2, userId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        boolean isLiked = rs.getInt(1) > 0;
                        return Response.ok("{\"liked\": " + isLiked + "}").build();
                    }
                }
            }
            return Response.ok("{\"liked\": false}").build();
        } catch (ClassNotFoundException | SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Get user's habits
    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserHabits(@PathParam("userId") int userId) {
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT h.*, u.username, " +
                "(SELECT COUNT(*) FROM Habit_Likes WHERE habit_id = h.id) as likes_count " +
                "FROM Habits h " +
                "JOIN Users u ON h.user_id = u.id " +
                "WHERE h.user_id = ? " +
                "ORDER BY h.created_at DESC";

        try (Connection conn = DriverManager.getConnection(url, this.user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Habit habit = new Habit();
                habit.setId(rs.getInt("id"));
                habit.setUser_id(rs.getInt("user_id"));
                habit.setDescription(rs.getString("description"));
                habit.setImage_url(rs.getString("image_url"));
                habit.setHabit_type(rs.getString("habit_type"));
                habit.setUsername(rs.getString("username"));
                habit.setLikes_count(rs.getInt("likes_count"));
                habit.setCreated_at(rs.getString("created_at"));
                habits.add(habit);
            }
            return Response.ok(habits).build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
