package TablesObj;
 
import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;
 
public class Treatment extends DBItem {
 
    private static final Logger LOG = Logger.getLogger(Treatment.class.getName());
    private final int id;
    private ArrayList<String> data;
 
    public Treatment(int id) { this.id = id; }
 
    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int c = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= c; i++) data.add(rs.getString(i));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }
 
    private String get(int i) { return (data != null && i < data.size()) ? data.get(i) : null; }
 
    public int    getId()   { return id; }
    public String getName() { loadData(); return get(1); }
    public String getDose() { loadData(); return get(2); }
 
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getTreatment(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }
 
    public static int insertAndGetId(String name, String dose) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(
                     "BEGIN ? := adminMedical.insertTreatment(?,?); END;")) {
            st.registerOutParameter(1, OracleTypes.NUMERIC);
            st.setString(2, name);
            st.setString(3, dose != null ? dose : "");
            st.execute();
            return st.getInt(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en insertTreatment", ex);
        }
        return -1;
    }
 
    public static void insert(int id, String name, String dose) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.insertTreatment(?,?,?) }")) {
            st.setInt(1, id); st.setString(2, name); st.setString(3, dose); st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }
 
    public void update(String name, String dose) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.updateTreatment(?,?,?) }")) {
            st.setInt(1, id); st.setString(2, name); st.setString(3, dose);
            st.execute(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }
 
    @Override public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getTreatmentById(?); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, id);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }
    @Override public void deleteItem() { throw new UnsupportedOperationException(); }
    @Override public void updateItem() { throw new UnsupportedOperationException(); }
}
 