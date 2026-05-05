package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


public class Rescuer extends DBItem {

    private static final Logger LOG = Logger.getLogger(Rescuer.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Rescuer(int id) { this.id = id; }

    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) data.add(rs.getString(i));
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public int    getId()            { return id; }
    public String getFirstName()     { loadData(); return get(1); }
    public String getSecondName()    { loadData(); return get(2); }
    public String getFirstSurname()  { loadData(); return get(3); }
    public String getSecondSurname() { loadData(); return get(4); }

    private String get(int i) { return (data != null && i < data.size()) ? data.get(i) : null; }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.getRescuer(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idUser, String firstName, String secondName,
                               String firstSurname, String secondSurname) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.insertRescuer(?,?,?,?,?) }")) {
            st.setInt(1, idUser);
            st.setString(2, firstName);
            st.setString(3, secondName);
            st.setString(4, firstSurname);
            st.setString(5, secondSurname);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(String firstName, String secondName,
                       String firstSurname, String secondSurname) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.updateRescuer(?,?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id);
            st.setString(2, firstName);
            st.setString(3, secondName);
            st.setString(4, firstSurname);
            st.setString(5, secondSurname);
            st.execute();
            con.commit();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.deleteRescuer(?) }")) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
