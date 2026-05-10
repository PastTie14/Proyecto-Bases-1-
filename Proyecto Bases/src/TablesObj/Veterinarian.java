package TablesObj;
 
import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;


public class Veterinarian extends DBItem {
 
    private static final Logger LOG = Logger.getLogger(Veterinarian.class.getName());
 
    private final int id;
    private ArrayList<String> data;
 
    public Veterinarian(int id) { this.id = id; }
 
    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) data.add(rs.getString(i));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar Veterinarian id=" + id, ex);
        }
    }
 
    // Columns: 0=id, 1=first_name, 2=second_name, 3=first_surname,
    //          4=second_surname, 5=clinic_name
    public int    getId()           { return id; }
    public String getFirstName()    { loadData(); return get(1); }
    public String getSecondName()   { loadData(); return get(2); }
    public String getFirstSurname() { loadData(); return get(3); }
    public String getSecondSurname(){ loadData(); return get(4); }
    public String getClinicName()   { loadData(); return get(5); }
 
    private String get(int index) {
        return (data != null && index < data.size()) ? data.get(index) : null;
    }
 
    // ── Static ────────────────────────────────────────────────────
 
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getVeterinarian(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }
 
    public static int insert(String firstName, String secondName,
                             String firstSurname, String secondSurname,
                             String clinicName) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(
                     "BEGIN ? := adminMedical.insertVeterinarian(?,?,?,?,?); END;")) {
            st.registerOutParameter(1, OracleTypes.NUMERIC);
            st.setString(2, firstName);
            st.setString(3, secondName);
            st.setString(4, firstSurname);
            st.setString(5, secondSurname);  
            st.setString(6, clinicName);       
            st.execute();
            return st.getInt(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error insertando Veterinarian", ex);
        }
        return -1;
    }
 
    // ── Instance ──────────────────────────────────────────────────
 
    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall(
                    "BEGIN ? := adminMedical.getVeterinarianById(?); END;");
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