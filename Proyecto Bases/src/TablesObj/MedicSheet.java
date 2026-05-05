package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


public class MedicSheet extends DBItem {

    private static final Logger LOG = Logger.getLogger(MedicSheet.class.getName());
    private final int id;
    private ArrayList<String> data;

    public MedicSheet(int id) { this.id = id; }

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

    public int    getId()                      { return id; }
    public String getAbandonmentDescription()  { loadData(); return get(1); }
    public int    getIdVeterinarian()          { loadData(); return getInt(2); }
    public int    getIdPetExtraInfo()          { loadData(); return getInt(3); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminMedical.getMedicSheet(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }
    
   
   public static ArrayList<ArrayList<String>> getMedicalData(int idPet) {
        ArrayList<String> diseases   = new ArrayList<>();
        ArrayList<String> treatments = new ArrayList<>();
        ArrayList<String> doses      = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            // ✅ (?) añadido para recibir el parámetro idPet
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminMedical.getDiseasesAndTreatments(?); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, idPet);   // ahora sí existe el índice 2
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs != null && rs.next()) {
                String disease   = rs.getString(1);
                String treatment = rs.getString(2);
                String dose      = rs.getString(3);

                if (disease != null && !diseases.contains(disease)) {
                    diseases.add(disease);
                }
                treatments.add(treatment);
                doses.add(dose);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error cargando datos médicos para pet id=" + idPet, ex);
        }

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        result.add(diseases);    
        result.add(treatments);  
        result.add(doses);       
        return result;
    }


    public static void insert(int id, String abandonDesc, int idVet, int idPetExtraInfo) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.insertMedicSheet(?,?,?,?) }")) {
            st.setInt(1, id); st.setString(2, abandonDesc);
            st.setInt(3, idVet); st.setInt(4, idPetExtraInfo);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(String abandonDesc, int idVet, int idPetExtraInfo) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminMedical.updateMedicSheet(?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id); st.setString(2, abandonDesc);
            st.setInt(3, idVet); st.setInt(4, idPetExtraInfo);
            st.execute(); con.commit(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
