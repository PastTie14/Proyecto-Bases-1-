package TablesObj;


import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


/**
 * Modelo de provincia.
 * Columnas (índice en data):
 *   0  id_province
 *   1  name
 */
public class Province extends DBItem {

    private static final Logger LOG = Logger.getLogger(Province.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Province(int id) { this.id = id; }

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
            LOG.log(Level.SEVERE, "Error loading Province id=" + id, ex);
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public String getName() { loadData(); return get(1); }

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
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getProvince(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idProvince, String name) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.insertProvince(?, ?) }");
            stmt.setInt(1, idProvince);
            stmt.setString(2, name);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── Instance — BD ─────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getProvince(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public void updateItem(int idProvince, String name) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.updateProvince(?, ?) }");
            stmt.setInt(1, idProvince);
            stmt.setString(2, name);
            stmt.execute();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() { throw new UnsupportedOperationException("No delete SP for Province."); }

    @Override
    public void updateItem() { throw new UnsupportedOperationException("Use updateItem(...) with parameters."); }
}
