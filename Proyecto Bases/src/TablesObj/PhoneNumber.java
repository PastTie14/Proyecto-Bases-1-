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
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleTypes;

/**
 * Columnas de phone_number:
 *   id_phone | number | id_user | id_pet | id_veterinarian
 */
public class PhoneNumber extends DBItem {

    private static final Logger LOG = Logger.getLogger(PhoneNumber.class.getName());

    private final int id;
    private ArrayList<String> data;

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public PhoneNumber(int id) {
        this.id = id;
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA LAZY
    // ─────────────────────────────────────────────────────────────

    private void loadData() {
        if (data != null) return;
        data = new ArrayList<>();
        try {
            ResultSet rs = getItem();
            if (rs != null && rs.next()) {
                int cols = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= cols; i++) data.add(rs.getString(i));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar PhoneNumber id=" + id, ex);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  GETTERS
    // ─────────────────────────────────────────────────────────────

    public int    getId()     { return id; }
    /** Devuelve el número de teléfono como String (columna 0). */
    public String getNumber() { loadData(); return get(0); }

    private String get(int index) {
        return (data != null && index < data.size()) ? data.get(index) : null;
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS PRIVADOS
    // ─────────────────────────────────────────────────────────────

    private static void setIntOrNull(CallableStatement st, int idx, int val) throws SQLException {
        if (val > 0) st.setInt(idx, val);
        else         st.setNull(idx, Types.NUMERIC);
    }

    // ─────────────────────────────────────────────────────────────
    //  BD — ESTÁTICAS
    // ─────────────────────────────────────────────────────────────

    /** Todos los teléfonos de la tabla. */
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminPhoneNumber.getPhoneNumber(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    /** Teléfonos asociados a un usuario. */
    public static ArrayList<String> getByUser(int idUser) {
        return fetchNumbers("BEGIN ? := adminPhoneNumber.getUserPhones(?); END;", idUser);
    }

    /** Teléfonos asociados a una mascota. */
    public static ArrayList<String> getByPet(int idPet) {
        return fetchNumbers("BEGIN ? := adminPhoneNumber.getPetPhones(?); END;", idPet);
    }

    /** Teléfonos asociados a un veterinario. */
    public static ArrayList<String> getByVeterinarian(int idVet) {
        return fetchNumbers("BEGIN ? := adminPhoneNumber.getVeterinarianPhones(?); END;", idVet);
    }

    /** Helper compartido para las tres consultas anteriores. */
    private static ArrayList<String> fetchNumbers(String sql, int paramId) {
        ArrayList<String> list = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, paramId);
            st.execute();
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                while (rs != null && rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error en fetchNumbers: " + sql, ex); }
        return list;
    }
    public static void insert(int idPhone, long number,
                              int idUser, int idPet, int idVeterinarian) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(
                     "{ CALL adminPhoneNumber.insertPhoneNumber(?,?,?,?,?) }")) {
            setIntOrNull(st, 1, idPhone);
            st.setLong  (2, number);
            setIntOrNull(st, 3, idUser);
            setIntOrNull(st, 4, idPet);
            setIntOrNull(st, 5, idVeterinarian);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error en insertPhoneNumber", ex); }
    }

    /** Inserta varios teléfonos de usuario de una sola vez. */
    public static void insertForUser(int idUser, ArrayList<String> numbers) {
        for (String raw : numbers) {
            if (raw == null || raw.isBlank()) continue;
            try {
                long num = Long.parseLong(raw.trim());
                insert(0, num, idUser, 0, 0);
            } catch (NumberFormatException e) {
                LOG.log(Level.WARNING, "Número de teléfono no válido: " + raw, e);
            }
        }
    }

    /** Inserta varios teléfonos de mascota de una sola vez. */
    public static void insertForPet(int idPet, ArrayList<String> numbers) {
        for (String raw : numbers) {
            if (raw == null || raw.isBlank()) continue;
            try {
                long num = Long.parseLong(raw.trim());
                insert(0, num, 0, idPet, 0);
            } catch (NumberFormatException e) {
                LOG.log(Level.WARNING, "Número de teléfono no válido: " + raw, e);
            }
        }
    }
    
    public static void insertForVeterinarian(int idVet, ArrayList<String> numbers) {
        for (String raw : numbers) {
            if (raw == null || raw.isBlank()) continue;
            try {
                long num = Long.parseLong(raw.trim());
                insert(0, num, 0, 0, idVet);
            } catch (NumberFormatException e) {
                LOG.log(Level.WARNING, "Número de teléfono no válido: " + raw, e);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  BD — INSTANCIA
    // ─────────────────────────────────────────────────────────────

    @Override
    public ResultSet getItem() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall(
                    "BEGIN ? := adminPhoneNumber.getPhoneNumberById(?); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setInt(2, id);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(
                     "{ CALL adminPhoneNumber.deletePhoneNumber(?) }")) {
            st.setInt(1, id);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, "Error en deletePhoneNumber", ex); }
    }

    @Override public void updateItem() { throw new UnsupportedOperationException("No soportado."); }
}
