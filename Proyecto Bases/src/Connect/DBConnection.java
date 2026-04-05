package Connect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Allan
 */
public class DBConnection {
    public static void insertUser(int id_user, String email, String password,
                                    String createdBy, String createdAt,
                                    String modifiedBy, String modifiedAt) throws SQLException {
        
        String host = "jdbc:oracle:thin:@localhost:1521:Pr1";
        String uName = "TS";
        String uPass = "TS";
        
        try {
            // creates the connection
            Connection con = DriverManager.getConnection(host, uName, uPass);

            // creates the call and call the procedure
            // the ? are where the variables go
            CallableStatement stmt = con.prepareCall("{ CALL insertUser(?, ?, ?, ?, ?, ?, ?)}");

            stmt.setInt(1, id_user);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, createdBy);
            stmt.setString(5, createdAt);
            stmt.setString(6, modifiedBy);
            stmt.setString(7, modifiedAt);

            stmt.execute();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
