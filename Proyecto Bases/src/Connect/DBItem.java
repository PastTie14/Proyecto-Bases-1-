/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Connect;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import oracle.jdbc.OracleTypes;
/**
 *
 * @author Ian
 */
public abstract class DBItem {
    public static String host = DBConnection.host;
    public static String uName = DBConnection.uName;
    public static String uPass = DBConnection.uPass;

    
    public ResultSet getItem(){
        return null;
    }
    
    public void updateItem(){
        
    }
    public void deleteItem(){
        
    }
}
