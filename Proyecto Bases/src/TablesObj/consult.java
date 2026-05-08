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
 
public class consult {
 
    private static final Logger LOG = Logger.getLogger(consult.class.getName());
 
    private static void setDateOrNull(CallableStatement st, int idx, String dateStr)
            throws SQLException {
        if (dateStr != null && !dateStr.isBlank())
            st.setDate(idx, java.sql.Date.valueOf(dateStr.trim()));
        else
            st.setNull(idx, Types.DATE);
    }
 
    /** Bindea un NUMBER si val > 0, o NULL si no. */
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
 
    //     Devuelve : donor_email, association_email, earliest_donation,total_donations, total_amount, currency,total_records, grand_total_amount
    public static ArrayList<ArrayList<Object>> getDonations(
            String startDate, String endDate, int idDonor, int idAssociation) {
 
        final String sql = "BEGIN ? := adminConsult.getDonations(?,?,?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            setDateOrNull(st, 2, startDate);
            setDateOrNull(st, 3, endDate);
            setIntOrNull(st, 4, idDonor);
            setIntOrNull(st, 5, idAssociation);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getDonations", ex);
        }
        return new ArrayList<>();
    }
 
    //     Devuelve : id_user, user_email, full_name, total_reports, avg_rating, latest_report_date, total_records
    public static ArrayList<ArrayList<Object>> getBlackListReport() {
 
        final String sql = "BEGIN ? := adminConsult.getBlackListReport(); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getBlackListReport", ex);
        }
        return new ArrayList<>();
    }
 
    //     Devuelve : id_report, reason, reported_date, reporter_email,reporter_name, user_rating, total_reports
    public static ArrayList<ArrayList<Object>> getBlackListReportDetails(int idUser) {
 
        final String sql = "BEGIN ? := adminConsult.getBlackListReportDetails(?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            setIntOrNull(st, 2, idUser);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getBlackListReportDetails", ex);
        }
        return new ArrayList<>();
    }
 

    //     Devuelve : id_match, match_date, similarity_percentage,id_parameter, parameter_type, parameter_value,created_date, total_records
    public static ArrayList<ArrayList<Object>> getMatches(int idType, int idRace) {
 
        final String sql = "BEGIN ? := adminConsult.getMatches(?,?); END;";
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
            LOG.log(Level.SEVERE, "Error en getMatches", ex);
        }
        return new ArrayList<>();
    }
 
    //     Devuelve : id_pet, pet_name, pet_type, pet_status, disease_count, treatment_count, total_records
    public static ArrayList<ArrayList<Object>> getPetNecessaryTreatments(int min, int max) {
 
        final String sql = "BEGIN ? := adminConsult.getPetNecessaryTreatments(?,?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            setIntOrNull(st, 2, min);
            setIntOrNull(st, 3, max);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getPetNecessaryTreatments", ex);
        }
        return new ArrayList<>();
    }
 
    // Devuelve : id_crib_house, crib_house_name, email,requires_donations, accepted_size,sizes_accepted_count, total_records
    public static ArrayList<ArrayList<Object>> getCompatibleCribHouses(int idPetType) {
 
        final String sql = "BEGIN ? := adminConsult.getCompatibleCribHouses(?); END;";
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall(sql)) {
 
            st.registerOutParameter(1, OracleTypes.CURSOR);
            setIntOrNull(st, 2, idPetType);
            st.execute();
 
            try (ResultSet rs = (ResultSet) st.getObject(1)) {
                return toList(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error en getCompatibleCribHouses", ex);
        }
        return new ArrayList<>();
    }
 
    // Devuelve : user_type, id_user, email, full_name, activity_count, total_records
    public static ArrayList<ArrayList<Object>> getBestRescuersAndAdopters(
            String startDate, String endDate) {
 
        final String sql = "BEGIN ? := adminConsult.getBestRescuersAndAdopters(?,?); END;";
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
            LOG.log(Level.SEVERE, "Error en getBestRescuersAndAdopters", ex);
        }
        return new ArrayList<>();
    }
}