package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;

/** 0 id_donation | 1 amount | 2 id_association | 3 id_currency */
public class Donation extends DBItem {

    private static final Logger LOG = Logger.getLogger(Donation.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Donation(int id) { this.id = id; }

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

    public int getId()             { return id; }
    public int getAmount()         { loadData(); return getInt(1); }
    public int getIdAssociation()  { loadData(); return getInt(2); }
    public int getIdCurrency()     { loadData(); return getInt(3); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminFinancial.getDonation(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, int amount, int idAssociation, int idCurrency) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminFinancial.insertDonation(?,?,?,?) }")) {
            st.setInt(1, id); st.setInt(2, amount); st.setInt(3, idAssociation); st.setInt(4, idCurrency);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
