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
    
    public static String host = "jdbc:oracle:thin:@localhost:1521:Pr1";
    public static String uName = "db_user";
    public static String  uPass = "1414";
    
    public static ResultSet getTuplas(String FunctionName) throws SQLException {
        System.out.println("BEGIN ? := " + FunctionName + "; END;");
        Connection con = DriverManager.getConnection(host, uName, uPass);

        CallableStatement stmt = con.prepareCall("BEGIN ? := " + FunctionName + "; END;");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) stmt.getObject(1);
    }
    
    
    public static ResultSet getUsers() throws SQLException {
        Connection con = DriverManager.getConnection(host, uName, uPass);

        CallableStatement stmt = con.prepareCall("BEGIN ? := adminUser.getUser(); END;");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) stmt.getObject(1);
    }
    
    public static ResultSet getPets() throws SQLException {

        Connection con = DriverManager.getConnection(host, uName, uPass);
        CallableStatement stmt = con.prepareCall("BEGIN ? := adminUser.getUsers(); END;");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) stmt.getObject(1);
    }
    
    public static void insertCurrency(String name, String acronym) throws SQLException {

        Connection con = null;
        CallableStatement stmt = null;

        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);

            stmt = con.prepareCall("{ CALL admincatalogs.insertCurrency(s_currency.nextVal, ?, ?)}");
            stmt.setString(1, name);
            stmt.setString(2, acronym);

            stmt.execute();
            con.commit();

        } catch (Exception e) {
            // Revertir si algo falla
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace(); 

        } finally {
            // Cerrar recursos siempre
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    // Insert Province
    public static void insertProvince(String name) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertProvince(s_province.nextVal, ?)}");
            stmt.setString(1, name);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Canton
    public static void insertCanton(String name, int idProvince) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertCanton(s_canton.nextVal, ?, ?)}");
            stmt.setString(1, name);
            stmt.setInt(2, idProvince);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert District
    public static void insertDistrict(String name, int idCanton) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertDistrict(s_district.nextVal, ?, ?)}");
            stmt.setString(1, name);
            stmt.setInt(2, idCanton);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Pet Type
    public static void insertPetType(String name) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertPetType(s_pet_type.nextVal, ?)}");
            stmt.setString(1, name);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Race
    public static void insertRace(String name, int idPetType) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertRace(s_race.nextVal, ?, ?)}");
            stmt.setString(1, name);
            stmt.setInt(2, idPetType);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Status
    public static void insertStatus(String statusType) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertStatus(s_status.nextVal, ?)}");
            stmt.setString(1, statusType);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Color
    public static void insertColor(String name) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertColor(s_color.nextVal, ?)}");
            stmt.setString(1, name);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Insert Value Type
    public static void insertValueType(String type) throws SQLException {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{ CALL admincatalogs.insertValueType(s_value_type.nextVal, ?)}");
            stmt.setString(1, type);
            stmt.execute();
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public static ResultSet login(String email, String password) throws SQLException{
        System.out.println("BEGIN ? := " + "; END;");
        Connection con = DriverManager.getConnection(host, uName, uPass);

        CallableStatement stmt = con.prepareCall("BEGIN ? :=  adminUser.login(" +"'"+email+"'"+","+"'"+ password+"'"+"); END;");
        stmt.registerOutParameter(1, OracleTypes.CURSOR);
        stmt.execute();

        return (ResultSet) stmt.getObject(1);
    }
    public static void insertUser(String email, String password,
                                    String createdBy, String createdAt,
                                    String modifiedBy, String modifiedAt) throws SQLException {

        Connection con = null;
        CallableStatement stmt = null;

        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);

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
            e.printStackTrace(); 

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