package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User login(String name, String password, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "select * from \"user\" where name = ? and password = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("id");
                    return new User(id, name, password);
                }else{
                    throw new DatabaseException("Error - could not login. Try again");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void createuser(String name, String password, ConnectionPool connectionPool) throws DatabaseException{

        String sql = "insert into \"user\" (name, password) values(?,?)";

        try (Connection connection = connectionPool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1, name);
                ps.setString(2, password);

                //her benyttes update fordi en tabel skal opdateres. i dette tilfælde opdateres tabellen med en ny bruger
                // ps.executeUpdate();  <-- der retuneres et integer

                // fremfor at køre ps.executeUpdate(); så gør vi følgende for at sikre at der reelt er sket en opdatering

                int rowsAffected = ps.executeUpdate();

                if(rowsAffected !=1){
                    throw new DatabaseException("Error, could not create an account.");
                }


            }
        } catch (SQLException e) {
            String msg = "Error, could not create an account. Try again";

            if(e.getMessage().startsWith("ERROR: duplicate key value")){
                msg = "The username is already used. Try another username";
            }
            throw new DatabaseException(msg);
        }
    }
}
