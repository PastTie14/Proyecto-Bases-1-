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
        
        DBConnection.insertUser(1, "allan@gmail.com", "1234", "Allan", "5/4/2026", "Allan", "5/4/2026");
        //DBConnection.insertUser(1, "allan@gmail.com", "1234", "Allan", "5/4/2026", "Allan", "5/4/2026");
        /*
        ImageIcon image = new ImageIcon("user.png");
        
        JLabel label = new JLabel();
        label.setText("ORAORAORAORAORAORAORA");
        label.setIcon(image);
        label.setHorizontalTextPosition(JLabel.CENTER); // set text relative to imageIcon
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setForeground(Color.green); // text color
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20)); // text font
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, 475, 475);
        */

        
        
        /*
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBounds(frame.getWidth()/6, 0, 500, 720);
        panel.setLayout(null);
        
        panel.add(label);
        frame.add(panel);
        */
    }
    
}
