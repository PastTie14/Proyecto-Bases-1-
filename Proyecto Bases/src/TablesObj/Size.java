package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

public class Size extends DBItem {

    private static final Logger LOG = Logger.getLogger(Size.class.getName());

    private final int id;
    private ArrayList<String> data;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public Size(int id) {
        this.id = id;
    }

    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    data.add(rs.getString(i));
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar datos de Size id=" + id, ex);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS
    //  getSize() devuelve: col1=id_size, col2=name
    // ─────────────────────────────────────────────────────────────

    public int    getId()   { return id; }
    public String getName() { loadData(); return get(1); }   // col2 → índice 1 en la lista (base 0)

    // ─────────────────────────────────────────────────────────────
    //  HELPERS INTERNOS
    // ─────────────────────────────────────────────────────────────

    private String get(int index) {
        return (data != null && index < data.size()) ? data.get(index) : null;
    }

    private int getInt(int index) {
        String val = get(index);
        if (val == null) return 0;
        try { return Integer.parseInt(val); }
        catch (NumberFormatException e) { return 0; }
    }

    // ─────────────────────────────────────────────────────────────
    //  OPERACIONES DE BD — ESTÁTICAS
    // ─────────────────────────────────────────────────────────────

    /**
     * Devuelve todas las filas de la tabla "size".
     * Columnas: 1=id_size, 2=name
     */
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getSize(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

   
    public static void insert(String name) {
        // insertSize es PROCEDURE, no FUNCTION → no retorna cursor ni id
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ call adminCatalogs.insertSize(?, ?) }")) {

            st.setInt   (1, 0);    // p_id_size — ignorado; BD usa s_size.nextVal
            st.setString(2, name); // p_name
            st.execute();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar Size name=" + name, ex);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  OPERACIONES DE BD — INSTANCIA
    // ─────────────────────────────────────────────────────────────


    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminCatalogs.getSizeById(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al obtener Size id=" + id, ex);
        }
        return null;
    }

    // adminCatalogs no expone updateSize ni deleteSize en el paquete actual
    @Override
    public void deleteItem() {
        throw new UnsupportedOperationException("deleteSize no está implementado en adminCatalogs.");
    }

    @Override
    public void updateItem() {
        throw new UnsupportedOperationException("updateSize no está implementado en adminCatalogs.");
    }
}