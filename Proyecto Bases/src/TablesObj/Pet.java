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

public class Pet extends DBItem {

    private static final Logger LOG = Logger.getLogger(Pet.class.getName());

    private final int id;
    private ArrayList<String> data;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public Pet(int id) {
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
            LOG.log(Level.SEVERE, "Error al cargar datos de Pet id=" + id, ex);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS
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
    public String getPetType()   { loadData(); return get(2); }
    public String getStatus()    { loadData(); return get(7); }

    public PetExtraInfo getExtraInfo() { return new PetExtraInfo(id); }

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

    public static ArrayList<String> getPopupItem(int id) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPopUpInfo(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            ArrayList<String> arr = new ArrayList<>();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    arr.add(rs.getString(i));
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

    public static ResultSet getAllPetsByStatus(int p_IdStatus) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPetByStatus(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, p_IdStatus);   // ✅ bind en lugar de concatenar en el string
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

    /*
     * @return: 0.imagen, 1.statusType, 2.nombre, 3.idExtraInfo, 4.energyLevel,
     *          5.email, 6.size, 7.TrainingEase, 8.PetType
     */
    public static ArrayList<String> getCardItem(int id) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getCardInfo(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);
            stmt.execute();

            // ✅ extraer el cursor del parámetro de salida
            ResultSet rs = (ResultSet) stmt.getObject(1);
            ArrayList<String> arr = new ArrayList<>();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) {
                    arr.add(rs.getString(i));
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
            data = null;
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
                             int idStatus, int idPetType, int idRescuer, int idSize) {
        final String sql =
            "BEGIN ? := adminPet.insertPet(?,?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
            st.registerOutParameter(1, OracleTypes.NUMERIC);
            st.setInt   (2,  0);
            st.setString(3,  picture);
            st.setString(4,  firstName);
            st.setString(5,  birthdate);
            if (dateLost != null && !dateLost.isBlank())
                st.setString(6, dateLost);
            else
                st.setNull(6, OracleTypes.VARCHAR);
            st.setString(7,  dateFound);
            st.setString(8,  email);
            st.setInt   (9,  idStatus);
            st.setInt   (10, idPetType);
            st.setInt   (11, idRescuer);
            st.setInt   (12, idSize);
            st.execute();
            return st.getInt(1);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override public void deleteItem() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override public void updateItem() { throw new UnsupportedOperationException("Not supported yet."); }
}