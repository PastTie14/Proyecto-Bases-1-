package Connect;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Allan
 */
public class DBConnection {
    
    public static ResultSet getUsers() throws SQLException {

        String host = "jdbc:oracle:thin:@localhost:1521:Pr1";
        String uName = "db_user";
        String uPass = "1414";

        Connection con = DriverManager.getConnection(host, uName, uPass);

        // ✅ Sintaxis correcta para FUNCIONES Oracle
        CallableStatement stmt = con.prepareCall("BEGIN ? := adminUser.getUsers(); END;");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) stmt.getObject(1);
    }
    
    public static void insertUser(String email, String password,
                                    String createdBy, String createdAt,
                                    String modifiedBy, String modifiedAt) throws SQLException {

        String host = "jdbc:oracle:thin:@localhost:1521:Pr1";
        String uName = "db_user";
        String uPass = "1414";

        Connection con = null;
        CallableStatement stmt = null;

        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false); // explícito, aunque ya es false por defecto en Oracle

            stmt = con.prepareCall("{ CALL adminUser.insertUser(s_user.nextVal, ?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, createdBy);
            stmt.setDate(4, convertToDate(createdAt));
            stmt.setString(5, modifiedBy);
            stmt.setDate(6, convertToDate(modifiedAt));

            stmt.execute();
            con.commit();

        } catch (Exception e) {
            // Revertir si algo falla
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace(); // ✅ Mejor que getStackTrace() para ver el error real

        } finally {
            // Cerrar recursos siempre
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private static java.sql.Date convertToDate(String dateString) {
        String[] parts = dateString.split("/");
        String formatedDate = parts[2] + "-" + parts[1] + "-" + parts[0];
        return java.sql.Date.valueOf(formatedDate);
    }
}