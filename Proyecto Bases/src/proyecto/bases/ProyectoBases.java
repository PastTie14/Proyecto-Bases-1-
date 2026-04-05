package proyecto.bases;

import Connect.DBConnection;
import java.sql.SQLException;

/**
 *
 * @author Ian
 */
public class ProyectoBases {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException{
        
        DBConnection.insertUser(2, "allan@gmail.com", "1234", "Allan", "5/4/2026", "Allan", "5/4/2026");
        
    }
    
}
