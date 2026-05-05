package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


public class Status extends DBItem {

    private static final Logger LOG = Logger.getLogger(Status.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Status(int id) { this.id = id; }

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
            LOG.log(Level.SEVERE, "Error loading Status id=" + id, ex);
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public String getStatusType() { loadData(); return get(1); }

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
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getStatus(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idStatus, String statusType) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.insertStatus(?, ?) }");
            stmt.setInt(1, idStatus);
            stmt.setString(2, statusType);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── Instance — BD ─────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getStatus(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public void updateItem(int idStatus, String statusType) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.updateStatus(?, ?) }");
            stmt.setInt(1, idStatus);
            stmt.setString(2, statusType);
            stmt.execute();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() { throw new UnsupportedOperationException("No delete SP for Status."); }

    @Override
    public void updateItem() { throw new UnsupportedOperationException("Use updateItem(...) with parameters."); }
}
