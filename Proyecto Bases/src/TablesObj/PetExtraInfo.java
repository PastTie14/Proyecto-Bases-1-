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
 *   0 id_pet_extra_info | 1 size | 2 before_picture | 3 after_picture
 *   4 id_pet | 5 id_current_status | 6 id_energy_level | 7 id_training_ease
 */
public class PetExtraInfo extends DBItem {

    private static final Logger LOG = Logger.getLogger(PetExtraInfo.class.getName());
    private final int id;
    private ArrayList<String> data;

    public PetExtraInfo(int id) { this.id = id; }

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

    public int    getId()              { return id; }
    public String getSize()            { loadData(); return get(1); }
    public String getBeforePicture()   { loadData(); return get(2); }
    public String getAfterPicture()    { loadData(); return get(3); }
    public int    getIdPet()           { loadData(); return getInt(4); }
    public int    getIdCurrentStatus() { loadData(); return getInt(5); }
    public int    getIdEnergyLevel()   { loadData(); return getInt(6); }
    public int    getIdTrainingEase()  { loadData(); return getInt(7); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminPetExtraInfo.getPetExtraInfo(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }
    
    public static ResultSet getByID(int petId){
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminPetExtraInfo.getPetExtraInfoById(" + petId +"); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    /** Retorna el id generado por la secuencia s_petExtraInfo. */
    public static int insert(int id, String size, String beforePic, String afterPic,
                             int idPet, int idCurrentStatus, int idEnergyLevel, int idTrainingEase) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ ? = CALL adminPetExtraInfo.insertPetExtraInfo(?,?,?,?,?,?,?,?) }")) {
            st.registerOutParameter(1, Types.NUMERIC);
            st.setInt(2, id);
            st.setString(3, size);
            st.setString(4, beforePic);
            st.setString(5, afterPic);
            st.setInt(6, idPet);
            st.setInt(7, idCurrentStatus);
            st.setInt(8, idEnergyLevel);
            st.setInt(9, idTrainingEase);
            st.execute();
            return st.getInt(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return -1;
    }

    public void update(String size, String beforePic, String afterPic,
                       int idCurrentStatus, int idEnergyLevel, int idTrainingEase) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminPetExtraInfo.updatePetExtraInfo(?,?,?,?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id);
            st.setString(2, size);
            st.setString(3, beforePic);
            st.setString(4, afterPic);
            st.setInt(5, idCurrentStatus);
            st.setInt(6, idEnergyLevel);
            st.setInt(7, idTrainingEase);
            st.execute();
            con.commit();
            data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
