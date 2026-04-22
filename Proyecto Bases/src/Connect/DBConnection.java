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
    public static void insertUser(String email, String password,
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
            CallableStatement stmt = con.prepareCall("{ CALL insertUser(s_user.nextVal, ?, ?, ?, ?, ?, ?)}");

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, createdBy);
            
            java.sql.Date sqlCreatedAt = convertToDate(createdAt);
            stmt.setDate(4, sqlCreatedAt);
            
            stmt.setString(5, modifiedBy);
            
            java.sql.Date sqlModifiedAt = convertToDate(modifiedAt);
            stmt.setDate(6, sqlModifiedAt);
            
            stmt.execute();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    
    // method for converting a string to Date
    private static java.sql.Date convertToDate(String dateString) {
        // DD/MM/YYYY format
        String[] parts = dateString.split("/");
        String formatedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
        return java.sql.Date.valueOf(formatedDate);
    }
}
