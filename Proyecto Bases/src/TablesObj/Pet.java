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
 * Modelo de mascota.
 *
 * Los datos de la fila se almacenan en {@code data} (un ArrayList&lt;String&gt;)
 * que se carga desde la BD la primera vez que se necesita.
 * Cada getter extrae su valor por índice de posición.
 *
 * Índices de columna (según SELECT del stored procedure getPetById / getPet):
 *   0  id_pet
 *   1  picture
 *   2  first_name
 *   3  birth_date
 *   4  date_lost
 *   5  date_found
 *   6  email
 *   7  id_status
 *   8  id_pet_type
 *   9  id_rescuer
 */
public class Pet extends DBItem {
 
    private static final Logger LOG = Logger.getLogger(Pet.class.getName());

    
 
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
 
    public Pet(int id) {
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
            LOG.log(Level.SEVERE, "Error al cargar datos de Pet id=" + id, ex);
        }
    }
 
    // ─────────────────────────────────────────────────────────────
    //  GETTERS — cada uno accede a su índice en data
    // ─────────────────────────────────────────────────────────────
 
    public int    getId()        { return id; }
 
    public String getPicture()   { loadData(); return get(1); }
    public String getFirstName() { loadData(); return get(2); }
    public String getBirthdate() { loadData(); return get(3); }
    public String getDateLost()  { loadData(); return get(4); }
    public String getDateFound() { loadData(); return get(5); }
    public String getEmail()     { loadData(); return get(6); }
    public int    getIdStatus()  { loadData(); return getInt(7); }
    public int    getIdPetType() { loadData(); return getInt(8); }
    public int    getIdRescuer() { loadData(); return getInt(9); }
 
    /** Alias para mantener compatibilidad con los componentes existentes. */
    public String getPetType() { loadData(); return get(2); }
    public String getStatus()  { loadData(); return get(7); }
 
    /** Info extra (tabla aparte) — implementar cuando esté disponible. */
    public PetExtraInfo getExtraInfo() { return new PetExtraInfo(id); }
 
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
    
    public static ArrayList<String> getPopupItem(int id) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPopUpInfo(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            ArrayList<String> arr = new ArrayList();
            if ((ResultSet) stmt != null && ((ResultSet) stmt).next()) {
                int cols = ((ResultSet) stmt).getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    arr.add(((ResultSet) stmt).getString(i)); // null se guarda como null
                }
            }
            return arr;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
 
    public static ResultSet getAllPets() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPet(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static ResultSet getAllPetsByStatus(int p_IdStatus){
         try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPetByStatus("+p_IdStatus+"); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
 
    // ─────────────────────────────────────────────────────────────
    //  OPERACIONES DE BD — INSTANCIA
    // ─────────────────────────────────────────────────────────────
 
    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPetById(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /*Recibe el id_pet, devuelve los siguientes datos:
    @return: 0.imagen, 1.statusType, 2.nombre, 3.idExtraInfo, 4.energyLevel, 
    5. email 6.size, 7.TrainingEase, 8. PetType
    */
    public static ArrayList getCardItem(int id){
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getCardInfo(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            ArrayList<String> arr = new ArrayList();
            if ((ResultSet) stmt != null && ((ResultSet) stmt).next()) {
                int cols = ((ResultSet) stmt).getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    arr.add(((ResultSet) stmt).getString(i)); // null se guarda como null
                }
            }
            return arr;
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
 
    public void updateItem(String pPicture, String pFirstName, String pBirthDate,
                           String pDateLost, String pDateFound, String pEmail, int pIdStatus) {
        Connection con = null;
        CallableStatement stmt = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
 
            stmt = con.prepareCall("{ CALL adminPet.updatePet(?, ?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, id);
            stmt.setString(2, pPicture);
            stmt.setString(3, pFirstName);
            stmt.setString(4, pBirthDate);
            stmt.setString(5, pDateLost);
            stmt.setString(6, pDateFound);
            stmt.setString(7, pEmail);
            stmt.setInt(8, pIdStatus);
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
    
    public static int insert(String picture, String firstName, String birthdate,
                         String dateLost, String dateFound, String email,
                         int idStatus, int idPetType, int idRescuer) {
    try (Connection con = DriverManager.getConnection(host, uName, uPass);
         CallableStatement st = con.prepareCall("{ ? = CALL adminPet.insertPet(?,?,?,?,?,?,?,?,?) }")) {
        st.registerOutParameter(1, OracleTypes.NUMERIC);
        st.setString(2, picture);
        st.setString(3, firstName);
        st.setString(4, birthdate);
        st.setString(5, dateLost);
        st.setString(6, dateFound);
        st.setString(7, email);
        st.setInt(8, idStatus);
        st.setInt(9,idPetType);
        st.setInt(10,idRescuer);
        st.execute();
        return st.getInt(1); // id_pet generado por secuencia
    } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    return -1;
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
 