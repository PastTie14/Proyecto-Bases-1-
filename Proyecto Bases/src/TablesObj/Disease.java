package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;

/** 0 id_disease | 1 name */
public class Disease extends DBItem {

    private static final Logger LOG = Logger.getLogger(Disease.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Disease(int id) { this.id = id; }

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

    private String get(int i) { return (data != null && i < data.size()) ? data.get(i) : null; }

    public int    getId()   { return id; }
    public String getName() { loadData(); return get(1); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getDisease(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, String name) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.insertDisease(?,?) }")) {
            st.setInt(1, id); st.setString(2, name); st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(String name) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.updateDisease(?,?) }")) {
            con.setAutoCommit(false); st.setInt(1, id); st.setString(2, name);
            st.execute(); con.commit(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
