package app.persistence;

import app.entities.Task;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskMapper {

    public static List<Task> getAllTasksPerUser(int userId, ConnectionPool connectionPool) throws DatabaseException {

        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE tasks.user_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    boolean done = rs.getBoolean("done");

                    taskList.add(new Task(id, name, done));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting task data");
        }

        return taskList;
    }


    public static void addTask(int userId, String name, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "insert into tasks (name, user_id) values(?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, userId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Error, could not add a task.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void deleteTask(int taskId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "delete from tasks where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, taskId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Error, could not delete the task.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    public static void taskDone(int taskId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "update tasks set done = true where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, taskId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Error, could not move task to Done.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void taskUndo(int taskId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "update tasks set done = false where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, taskId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Error, could not move task to TO DO.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    public static void editTask(int taskId, String title, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE public.tasks SET name=? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, title);
                ps.setInt(2, taskId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Error, could edit the task.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    public static Task getTask(int taskId, ConnectionPool connectionPool) throws DatabaseException{

        Task task = null;

        String sql = "select * from public.tasks where id=?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, taskId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        boolean done = rs.getBoolean("done");

                        task = new Task(id, name, done);
                    }
                }
            }
            return task;
        }
        catch (SQLException e) {
            throw new DatabaseException("Error getting task data");
        }
    }
}
