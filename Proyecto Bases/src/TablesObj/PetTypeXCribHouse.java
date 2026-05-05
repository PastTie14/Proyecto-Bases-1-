package TablesObj;

import static Connect.DBConnection.host;
import static Connect.DBConnection.uName;
import static Connect.DBConnection.uPass;
import Connect.DBItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import oracle.jdbc.OracleTypes;


public class PetTypeXCribHouse extends DBItem {

    private static final Logger LOG = Logger.getLogger(PetTypeXCribHouse.class.getName());

    // ── Static — consultas ────────────────────────────────────────
    public static ResultSet getAll() {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("BEGIN ? := adminPet.getPetTypeXCribHouse(); END;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            return (ResultSet) stmt.getObject(1);
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        return null;
    }

    public static void insert(int idPetType, int idCribHouse) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            CallableStatement stmt = con.prepareCall("{ CALL adminPet.insertPetTypeXCribHouse(?, ?) }");
            stmt.setInt(1, idPetType);
            stmt.setInt(2, idCribHouse);
            stmt.execute();
        } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
    }

    // ── DBItem — no aplica para tablas intermedias ────────────────
    @Override public ResultSet getItem()  { throw new UnsupportedOperationException("Junction table — use getAll()."); }
    @Override public void deleteItem()    { throw new UnsupportedOperationException("Junction table — use delete(...)."); }
    @Override public void updateItem()    { throw new UnsupportedOperationException("Junction table — no update SP."); }
}
