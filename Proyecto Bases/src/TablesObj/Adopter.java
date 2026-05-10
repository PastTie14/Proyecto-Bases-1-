package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import static TablesObj.User.getAll;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;

/**
 * Columnas (índice base 0):
 *   0 id_user | 1 first_name | 2 second_name | 3 first_surname | 4 second_surname
 */
public class Adopter extends DBItem {

    private static final Logger LOG = Logger.getLogger(Adopter.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Adopter(int id) { this.id = id; }

    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) data.add(rs.getString(i));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public int    getId()            { return id; }
    public String getFirstName()     { loadData(); return get(0); }
    public String getSecondName()    { loadData(); return get(1); }
    public String getFirstSurname()  { loadData(); return get(2); }
    public String getSecondSurname() { loadData(); return get(3); }

    private String get(int i) { 
        return (data != null && i < data.size()) ? data.get(i) : null; 
    }

    public static ArrayList<ArrayList<Object>> getAll() {
        ArrayList<ArrayList<Object>> filas = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.getAdopter(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
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
            return filas;
        } catch (SQLException ex) { LOG.log(Level.ALL, null, ex); }
        return null;
    }
    
    public ResultSet getAllRS() {
   
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.getAdopterById(?); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, id);
            st.execute();
            return (ResultSet) st.getObject(1);  
        } catch (SQLException ex) { LOG.log(Level.ALL, null, ex); }
        return null;
    }
    
    public static boolean getAdopterByID(int id) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.getAdopterById(?); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, id);
            st.execute();
            ResultSet rs = (ResultSet) st.getObject(1);
            return rs != null && rs.next();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return false;
    }

    public static void insert(int idUser, String firstName, String secondName,
                               String firstSurname, String secondSurname) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.insertAdopter(?,?,?,?,?) }")) {
            st.setInt(1, idUser);
            st.setString(2, firstName);
            st.setString(3, secondName);
            st.setString(4, firstSurname);
            st.setString(5, secondSurname);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(String firstName, String secondName,
                       String firstSurname, String secondSurname) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.updateAdopter(?,?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id);
            st.setString(2, firstName);
            st.setString(3, secondName);
            st.setString(4, firstSurname);
            st.setString(5, secondSurname);
            st.execute();
            con.commit();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public ResultSet getItem() {
        return getAllRS();
    }
    
    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.deleteAdopter(?) }")) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
