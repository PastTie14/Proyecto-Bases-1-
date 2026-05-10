package TablesObj;
 
import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;
 
/** 0 id_donation | 1 amount | 2 id_association | 3 id_currency | 4 id_crib_house */
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
 
    private String get(int i)   { return (data != null && i < data.size()) ? data.get(i) : null; }
    private int    getInt(int i) { String v = get(i); if (v == null) return 0; try { return Integer.parseInt(v); } catch (NumberFormatException e) { return 0; } }
 
    public int getId()            { return id; }
    public int getAmount()        { loadData(); return getInt(1); }
    public int getIdAssociation() { loadData(); return getInt(2); }
    public int getIdCurrency()    { loadData(); return getInt(3); }
    public int getIdCribHouse()   { loadData(); return getInt(4); }
 
    // ═══════════════════════════════════════════════════════════════
    //  CONSULTAS SIMPLES
    // ═══════════════════════════════════════════════════════════════
 
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
 
    /**
     * Devuelve todas las Asociaciones y Casas Cuna disponibles para recibir donaciones.
     * Columnas: [0] ID (Number), [1] NOMBRE (String), [2] TIPO ('Asociación' | 'Casa Cuna')
     */
    public static ArrayList<ArrayList<Object>> getRecipients() {
        ArrayList<ArrayList<Object>> rows = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("BEGIN ? := adminFinancial.getRecipients(); END;")) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                while (rs != null && rs.next()) {
                    ArrayList<Object> row = new ArrayList<>();
                    row.add(rs.getObject(1)); // ID
                    row.add(rs.getString(2)); // NOMBRE
                    row.add(rs.getString(3)); // TIPO
                    rows.add(row);
                }
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error en getRecipients", ex); }
        return rows;
    }
 
    // ═══════════════════════════════════════════════════════════════
    //  TRANSACCIÓN COMPLETA DE DONACIÓN + CAMBIO DE ESTADO
    // ═══════════════════════════════════════════════════════════════
 

    public static boolean insertDonationTransaction(
            int    amount,
            int    idAssociation,
            int    idCribHouse,
            int    idCurrency,
            int    idAdopter,
            Pet    pet) {
 
        Connection con = null;
        try {
            con = DriverManager.getConnection(host, uName, uPass);
            con.setAutoCommit(false);
 
            // ── 1. Insertar donación ──────────────────────────────
            try (CallableStatement st = con.prepareCall(
                    "{ CALL adminFinancial.insertDonation(?,?,?,?,?) }")) {
                st.setInt(1, 0); 
                st.setInt(2, amount);
                if (idAssociation > 0) st.setInt (3, idAssociation);
                else                   st.setNull(3, Types.NUMERIC);
                st.setInt(4, idCurrency);
                if (idCribHouse > 0) st.setInt (5, idCribHouse);
                else                 st.setNull(5, Types.NUMERIC);
                st.execute();
            }
 
            // ── 2. Obtener id de la donación recién creada (CURRVAL) ──
            int idDonation = -1;
            try (CallableStatement st = con.prepareCall(
                    "BEGIN ? := adminFinancial.getLastDonationId(); END;")) {
                st.registerOutParameter(1, Types.NUMERIC);
                st.execute();
                idDonation = st.getInt(1);
            }
 
            if (idDonation <= 0)
                throw new SQLException("getLastDonationId devolvió un id inválido: " + idDonation);
 
            // ── 3. Vincular donación al adoptante ─────────────────
            try (CallableStatement st = con.prepareCall(
                    "{ CALL adminFinancial.insertDonationXUser(?,?) }")) {
                st.setInt(1, idAdopter);
                st.setInt(2, idDonation);
                st.execute();
            }
            // ── 4. Cambiar estado de la mascota ───────────────────
            pet.petFound(pet.getId());
            
 
            con.commit();
            return true;
 
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error en insertDonationTransaction — rollback", ex);
            if (con != null) try { con.rollback(); } catch (SQLException e) { LOG.log(Level.SEVERE, "Rollback fallido", e); }
            return false;
        } finally {
            if (con != null) try { con.close(); } catch (SQLException e) { LOG.log(Level.SEVERE, "Close fallido", e); }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════
    //  DBItem
    // ═══════════════════════════════════════════════════════════════
 
    @Override public ResultSet getItem() { return getAll(); }
    @Override public void deleteItem()   { throw new UnsupportedOperationException(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}