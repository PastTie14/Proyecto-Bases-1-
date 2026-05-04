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
 * Modelo de registro de auditoría.
 * Columnas (índice en data):
 *   0  id_log
 *   1  changedate
 *   2  changeby
 *   3  tablename
 *   4  fieldname
 *   5  previousvalue
 *   6  currentvalue
 */
public class Log extends DBItem {

    private static final Logger LOG = Logger.getLogger(Log.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Log(int id) { this.id = id; }

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
            LOG.log(Level.SEVERE, "Error loading Log id=" + id, ex);
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public String getChangedate() { loadData(); return get(1); }
    public String getChangeby() { loadData(); return get(2); }
    public String getTablename() { loadData(); return get(3); }
    public String getFieldname() { loadData(); return get(4); }
    public String getPreviousvalue() { loadData(); return get(5); }
    public String getCurrentvalue() { loadData(); return get(6); }

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
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminUser.getLog(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idLog, String changeDate, String changeBy, String tableName, String fieldName, String previousValue, String currentValue) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminUser.insertLog(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, idLog);
            stmt.setString(2, changeDate);
            stmt.setString(3, changeBy);
            stmt.setString(4, tableName);
            stmt.setString(5, fieldName);
            stmt.setString(6, previousValue);
            stmt.setString(7, currentValue);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── Instance — BD ─────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminUser.getLog(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    @Override
    public void deleteItem() { throw new UnsupportedOperationException("No delete SP for Log."); }

    @Override
    public void updateItem() { throw new UnsupportedOperationException("Use updateItem(...) with parameters."); }
}
