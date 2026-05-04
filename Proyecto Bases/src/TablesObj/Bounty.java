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
 *   0 id_bounty | 1 amount | 2 id_pet_extra_info | 3 id_currency
 */
public class Bounty extends DBItem {

    private static final Logger LOG = Logger.getLogger(Bounty.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Bounty(int id) { this.id = id; }

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

    public int getId()               { return id; }
    public int getAmount()           { loadData(); return getInt(1); }
    public int getIdPetExtraInfo()   { loadData(); return getInt(2); }
    public int getIdCurrency()       { loadData(); return getInt(3); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminPetExtraInfo.getBounty(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, int amount, int idPetExtraInfo, int idCurrency) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminPetExtraInfo.insertBounty(?,?,?,?) }")) {
            st.setInt(1, id); st.setInt(2, amount); st.setInt(3, idPetExtraInfo); st.setInt(4, idCurrency);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(int amount, int idCurrency) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminPetExtraInfo.updateBounty(?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id); st.setInt(2, amount); st.setInt(3, idCurrency);
            st.execute(); con.commit(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminPetExtraInfo.deleteBounty(?) }")) {
            st.setInt(1, id); st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
