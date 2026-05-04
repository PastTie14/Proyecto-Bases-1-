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
 * Modelo de moneda / divisa.
 * Columnas (índice en data):
 *   0  id_currency
 *   1  name
 *   2  acronym
 */
public class Currency extends DBItem {

    private static final Logger LOG = Logger.getLogger(Currency.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Currency(int id) { this.id = id; }

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
            LOG.log(Level.SEVERE, "Error loading Currency id=" + id, ex);
        }
    }

    // ── Getters ───────────────────────────────────────────────────
    public int getId() { return id; }
    public String getName() { loadData(); return get(1); }
    public String getAcronym() { loadData(); return get(2); }

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
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getCurrency(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idCurrency, String name, String acronym) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.insertCurrency(?, ?, ?) }");
            stmt.setInt(1, idCurrency);
            stmt.setString(2, name);
            stmt.setString(3, acronym);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── Instance — BD ─────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getCurrency(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public void updateItem(int idCurrency, String name, String acronym) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminCatalogs.updateCurrency(?, ?, ?) }");
            stmt.setInt(1, idCurrency);
            stmt.setString(2, name);
            stmt.setString(3, acronym);
            stmt.execute();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() { throw new UnsupportedOperationException("No delete SP for Currency."); }

    @Override
    public void updateItem() { throw new UnsupportedOperationException("Use updateItem(...) with parameters."); }
}
