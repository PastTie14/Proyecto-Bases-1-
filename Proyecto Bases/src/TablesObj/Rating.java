package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;

/** 0 id_rating | 1 score | 2 id_user | 3 id_adopter */
public class Rating extends DBItem {

    private static final Logger LOG = Logger.getLogger(Rating.class.getName());
    private final int id;
    private ArrayList<String> data;

    public Rating(int id) { this.id = id; }

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

    public int getId()        { return id; }
    public int getScore()     { loadData(); return getInt(1); }
    public int getIdUser()    { loadData(); return getInt(2); }
    public int getIdAdopter() { loadData(); return getInt(3); }

    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement st = con.prepareCall("BEGIN ? := adminAdoptionMatch.getRating(); END;");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.execute();
            return (ResultSet) st.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int id, int score, int idUser, int idAdopter) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminAdoptionMatch.insertRating(?,?,?,?) }")) {
            st.setInt(1, id); st.setInt(2, score); st.setInt(3, idUser); st.setInt(4, idAdopter);
            st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    public void update(int score, int idUser, int idAdopter) {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminAdoptionMatch.updateRating(?,?,?,?) }")) {
            con.setAutoCommit(false);
            st.setInt(1, id); st.setInt(2, score); st.setInt(3, idUser); st.setInt(4, idAdopter);
            st.execute(); con.commit(); data = null;
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override
    public void deleteItem() {
        try (Connection con = DriverManager.getConnection(host, uName, uPass);
             CallableStatement st = con.prepareCall("{ CALL adminAdoptionMatch.deleteRating(?) }")) {
            st.setInt(1, id); st.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    @Override public ResultSet getItem() { return getAll(); }
    @Override public void updateItem()   { throw new UnsupportedOperationException(); }
}
