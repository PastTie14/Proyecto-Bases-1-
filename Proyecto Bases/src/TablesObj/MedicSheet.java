package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;

/**
 * Columnas (índice base 0):
 *   0 id_medic_sheet | 1 abandonment_description | 2 id_veterinarian | 3 id_pet_extra_info
 */
public class MedicSheet extends DBItem {

    private static final Logger LOG = Logger.getLogger(MedicSheet.class.getName());
    private final int id;
    private ArrayList<String> data;

    public MedicSheet(int id) { this.id = id; }

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

    private String get(int i)  { return (data != null && i < data.size()) ? data.get(i) : null; }
    private int getInt(int i)  { String v = get(i); if (v == null) return 0; try { return Integer.parseInt(v); } catch (NumberFormatException e) { return 0; } }

    public int    getId()                      { return id; }
    public String getAbandonmentDescription()  { loadData(); return get(1); }
    public int    getIdVeterinarian()          { loadData(); return getInt(2); }
    public int    getIdPetExtraInfo()          { loadData(); return getInt(3); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getMedicSheet(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, String abandonDesc, int idVet, int idPetExtraInfo) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.insertMedicSheet(?,?,?,?) }")) {
            st.setInt(1, id); st.setString(2, abandonDesc);
            st.setInt(3, idVet); st.setInt(4, idPetExtraInfo);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(String abandonDesc, int idVet, int idPetExtraInfo) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.updateMedicSheet(?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id); st.setString(2, abandonDesc);
            st.setInt(3, idVet); st.setInt(4, idPetExtraInfo);
            st.execute(); con.commit(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
