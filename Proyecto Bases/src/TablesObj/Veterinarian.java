package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import Connect.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 * Modelo de veterinario.
 *
 * Los datos de la fila se almacenan en {@code data} (un ArrayList&lt;String&gt;)
 * que se carga desde la BD la primera vez que se necesita.
 * Cada getter extrae su valor por índice de posición.
 *
 * Índices de columna (según SELECT del stored procedure getVeterinarianById / getVeterinarian):
 *   0  id_veterinarian
 *   1  first_name
 *   2  last_name
 *   3  email
 *   4  phone
 *   5  specialty
 *   6  id_status
 */
public class Veterinarian extends DBItem {

    private static final Logger LOG = Logger.getLogger(Veterinarian.class.getName());

    // ── Clave primaria ────────────────────────────────────────────
    private final int id;

    /**
     * Almacena todos los campos de la fila como Strings.
     * Se llena la primera vez que se llama a cualquier getter.
     */
    private ArrayList<String> data;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public Veterinarian(int id) {
        this.id = id;
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA LAZY
    // ─────────────────────────────────────────────────────────────

    /**
     * Carga {@code data} desde la BD si todavía no se ha hecho.
     * Se invoca automáticamente desde cada getter.
     */
    private void loadData() {
        if (data != null) return;

        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    data.add(rs.getString(i)); // null se guarda como null
                }
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar datos de Veterinarian id=" + id, ex);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS — cada uno accede a su índice en data
    // ─────────────────────────────────────────────────────────────

    public int    getId()        { return id; }

    public String getFirstName() { loadData(); return get(1); }
    public String getLastName()  { loadData(); return get(2); }
    public String getEmail()     { loadData(); return get(3); }
    public String getPhone()     { loadData(); return get(4); }
    public String getSpecialty() { loadData(); return get(5); }
    public int    getIdStatus()  { loadData(); return getInt(6); }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS INTERNOS
    // ─────────────────────────────────────────────────────────────

    /** Retorna el valor en el índice dado, o null si está fuera de rango. */
    private String get(int index) {
        return (data != null && index < data.size()) ? data.get(index) : null;
    }

    /** Retorna el valor como int; 0 si es null o no parseable. */
    private int getInt(int index) {
        String val = get(index);
        if (val == null) return 0;
        try { return Integer.parseInt(val); }
        catch (NumberFormatException e) { return 0; }
    }

    // ─────────────────────────────────────────────────────────────
    //  OPERACIONES DE BD — ESTÁTICAS
    // ─────────────────────────────────────────────────────────────

    public static ResultSet getAllVeterinarians() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminMedical.getVeterinarian(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
    * Inserta un nuevo veterinario y retorna el id generado por la secuencia.
    * @return id_veterinarian generado, o -1 si falla.
    */
   public static int insert(String firstName, String lastName, String email,
                            String phone, String specialty, int idStatus) {
       try (Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("{ ? = CALL adminMedical.insertVeterinarian(?,?,?,?,?,?) }")) {
           st.registerOutParameter(1, OracleTypes.NUMERIC);
           st.setString(2, firstName);
           st.setString(3, lastName);
           st.setString(4, email);
           st.setString(5, phone);
           st.setString(6, specialty);
           st.setInt(7, idStatus);
           st.execute();
           return st.getInt(1);
       } catch (SQLException ex) {
           LOG.log(Level.SEVERE, "Error insertando Veterinarian", ex);
       }
       return -1;
   }
    

    // ─────────────────────────────────────────────────────────────
    //  OPERACIONES DE BD — INSTANCIA
    // ─────────────────────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminMedical.getVeterinarianById(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateItem(String pFirstName, String pLastName, String pEmail,
                           String pPhone, String pSpecialty, int pIdStatus) {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);

            stmt = con.prepareCall("{ CALL adminMedical.updateVeterinarian(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, id);
            stmt.setString(2, pFirstName);
            stmt.setString(3, pLastName);
            stmt.setString(4, pEmail);
            stmt.setString(5, pPhone);
            stmt.setString(6, pSpecialty);
            stmt.setInt(7, pIdStatus);
            stmt.execute();
            con.commit();

            data = null; // invalida caché para refrescar en el próximo getter

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con  != null) try { con.close();  } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void deleteItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
