package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


public class BlackList extends DBItem {

    private static final Logger LOG = Logger.getLogger(BlackList.class.getName());

    // ── Static — consultas ────────────────────────────────────────
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminBlackList.getBlackList(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idUser) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminBlackList.insertBlackList( ?) }");
            stmt.setInt(1, idUser);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }
    
    public static int getBlackListId( int idUser){
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminBlackList.getBlackListId(?); END;");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setInt(2, idUser);
            st.execute();
            return  st.getInt(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return 0;
    }
    
    public static ArrayList<ArrayList<Object>> getBannedUsers(int idUser){
        ArrayList<ArrayList<Object>> filas = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall(" BEGIN ? :=  adminBlackList.getUsersFromBlackList(?); END; ")) { 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, idUser);
            st.execute();
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                while (rs.next()) {
                    ArrayList<Object> fila = new ArrayList<>();
                    for (int i = 1; i <= cols; i++) {
                        fila.add(rs.getObject(i));
                    }
                    filas.add(fila);
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getPetFilters", ex);
        }

        return filas;
        
    }

    // ── DBItem — no aplica para tablas intermedias ────────────────
    @Override public ResultSet getItem()  { throw new UnsupportedOperationException("Junction table — use getAll()."); }
    @Override public void deleteItem()    { throw new UnsupportedOperationException("Junction table — use delete(...)."); }
    @Override public void updateItem()    { throw new UnsupportedOperationException("Junction table — no update SP."); }
}
