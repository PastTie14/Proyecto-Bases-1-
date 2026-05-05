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
 *   0 id_user | 1 email | 2 password
 */
public class User extends DBItem {

    private static final Logger LOG = Logger.getLogger(User.class.getName());
    private final int id;
    private ArrayList<String> data;

    public User(int id) { this.id = id; }

    // ── Carga lazy ────────────────────────────────────────────────
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

    // ── Getters ───────────────────────────────────────────────────
    public int    getId()       { return id; }
    public String getEmail()    { loadData(); return get(1); }
    public String getPassword() { loadData(); return get(2); }

    private String get(int i) { return (data != null && i < data.size()) ? data.get(i) : null; }

    // ── BD estática ───────────────────────────────────────────────
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.getUser(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    /** Devuelve cursor con la fila si email+password coinciden, vacío si no. */
    public static ResultSet login(String email, String password) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminUser.login(?,?); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setString(2, email);
            st.setString(3, password);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idUser, String email, String password) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.insertUser(?,?,?) }")) {
            st.setInt(1, idUser);
            st.setString(2, email);
            st.setString(3, password);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── BD instancia ──────────────────────────────────────────────
    @Override
    public ResultSet getItem() {
        // No existe getPetById equivalente para user; se reutiliza getAll y filtra
        // Si el stored procedure expone getUserById, reemplazar aquí.
        return getAll();
    }

    public void update(String email, String password) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.updateUser(?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id);
            st.setString(2, email);
            st.setString(3, password);
            st.execute();
            con.commit();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminUser.deleteUser(?) }")) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public void updateItem() { throw new UnsupportedOperationException(); }
}
