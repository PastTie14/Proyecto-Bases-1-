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
 *   0 id_adoption | 1 notes | 2 adoption_date | 3 reference | 4 id_adopter | 5 id_pet
 */
public class AdoptionForm extends DBItem {

    private static final Logger LOG = Logger.getLogger(AdoptionForm.class.getName());
    private final int id;
    private ArrayList<String> data;

    public AdoptionForm(int id) { this.id = id; }

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

    public int    getId()             { return id; }
    public String getNotes()          { loadData(); return get(1); }
    public String getAdoptionDate()   { loadData(); return get(2); }
    public String getReference()      { loadData(); return get(3); }
    public int    getIdAdopter()      { loadData(); return getInt(4); }
    public int    getIdPet()          { loadData(); return getInt(5); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminAdoptionMatch.getAdoptionForm(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, String notes, String adoptionDate,
                               String reference, int idAdopter, int idPet) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminAdoptionMatch.insertAdoptionForm(?,?,?,?,?,?) }")) {
            st.setInt(1, id); st.setString(2, notes); st.setString(3, adoptionDate);
            st.setString(4, reference); st.setInt(5, idAdopter); st.setInt(6, idPet);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
