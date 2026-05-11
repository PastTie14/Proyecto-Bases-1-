package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;

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

public class stats {

    private static final Logger LOG = Logger.getLogger(stats.class.getName());

    // ─────────────────────────────────────────────────────────────
    //  HELPERS PRIVADOS
    // ─────────────────────────────────────────────────────────────

    private static void setDateOrNull(CallableStatement st, int idx, String dateStr)
            throws SQLException {
        if (dateStr != null && !dateStr.isBlank())
            st.setDate(idx, java.sql.Date.valueOf(dateStr.trim()));
        else
            st.setNull(idx, Types.DATE);
    }

    private static void setIntOrNull(CallableStatement st, int idx, int val)
            throws SQLException {
        if (val > 0) st.setInt(idx, val);
        else         st.setNull(idx, Types.NUMERIC);
    }

    private static ArrayList<ArrayList<Object>> toList(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<Object>> filas = new ArrayList<>();
        if (rs == null) return filas;
        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            ArrayList<Object> fila = new ArrayList<>();
            for (int i = 1; i <= cols; i++) fila.add(rs.getObject(i));
            filas.add(fila);
        }
        return filas;
    }

    public static ArrayList<ArrayList<Object>> getPetsByTypeAndStatus(
            int idType, int idStatus, String startDate, String endDate) {

        final String sql = "BEGIN ? := adminStats.getPetsByTypeAndStatus(?,?,?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {

            st.registerOutParameter(1, OracleTypes.CURSOR);
            setIntOrNull(st, 2, idType);
            setIntOrNull(st, 3, idStatus);
            setDateOrNull(st, 4, startDate);
            setDateOrNull(st, 5, endDate);
            st.execute();

            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getPetsByTypeAndStatus", ex);
        }
        return new ArrayList<>();
    }

    public static ArrayList<ArrayList<Object>> getDonationsByAssociation(
            String startDate, String endDate) {

        final String sql = "BEGIN ? := adminStats.getDonationsByAssociation(?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {

            st.registerOutParameter(1, OracleTypes.CURSOR);
            setDateOrNull(st, 2, startDate);
            setDateOrNull(st, 3, endDate);
            st.execute();

            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getDonationsByAssociation", ex);
        }
        return new ArrayList<>();
    }

    public static ArrayList<ArrayList<Object>> getDonationsByCribHouse(
            String startDate, String endDate) {

        final String sql = "BEGIN ? := adminStats.getDonationsByCribHouse(?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {

            st.registerOutParameter(1, OracleTypes.CURSOR);
            setDateOrNull(st, 2, startDate);
            setDateOrNull(st, 3, endDate);
            st.execute();

            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getDonationsByCribHouse", ex);
        }
        return new ArrayList<>();
    }


    public static ArrayList<ArrayList<Object>> getAdoptedVSUnadopted(
            int idType, int idRace) {

        final String sql = "BEGIN ? := adminStats.getAdoptedVSUnadopted(?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {

            st.registerOutParameter(1, OracleTypes.CURSOR);
            setIntOrNull(st, 2, idType);
            setIntOrNull(st, 3, idRace);
            st.execute();

            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getAdoptedVSUnadopted", ex);
        }
        return new ArrayList<>();
    }

    public static ArrayList<ArrayList<Object>> getUnadoptedPetsByAgeRange() {

        final String sql = "BEGIN ? := adminStats.getUnadoptedPetsByAgeRange(); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {

            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();

            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getUnadoptedPetsByAgeRange", ex);
        }
        return new ArrayList<>();
    }

}