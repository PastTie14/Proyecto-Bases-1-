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
 * Modelo de chip de identificación de mascota.
 * Columnas (índice en data):
 *   0  id_chip
 *   1  chip_number
 *   2  registration_date
 *   3  id_pet
 */
public class IdentificationChip extends DBItem {

    private static final Logger LOG = Logger.getLogger(IdentificationChip.class.getName());
    private final int id;
    private ArrayList<String> data;

    public IdentificationChip(int id) { this.id = id; }

    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getAll();
            while (rs != null && rs.next()) {
                if (rs.getInt(1) == id) {
                    int cols = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= cols; i++) data.add(rs.getString(i));
                    break;
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error loading IdentificationChip id=" + id, ex);
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public String getChipNumber() { loadData(); return get(1); }
    public String getRegistrationDate() { loadData(); return get(2); }
    public int    getIdPet() { loadData(); return getInt(3); }

    private String get(int index) {
        return (data != null && index < data.size()) ? data.get(index) : null;
    }
    private int getInt(int index) {
        String v = get(index);
        if (v == null) return 0;
        try { return Integer.parseInt(v); } catch (NumberFormatException e) { return 0; }
    }

    // ── Static — consultas ────────────────────────────────────────
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getIdentificationChip(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idChip, String chipNumber, String registrationDate, int idPet) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminPet.insertIdentificationChip(?, ?, ?, ?) }");
            stmt.setInt(1, idChip);
            stmt.setString(2, chipNumber);
            stmt.setString(3, registrationDate);
            stmt.setInt(4, idPet);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── Instance — BD ─────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getIdentificationChip(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    @Override
    public void deleteItem() { throw new UnsupportedOperationException("No delete SP for IdentificationChip."); }

    @Override
    public void updateItem() { throw new UnsupportedOperationException("Use updateItem(...) with parameters."); }
}
