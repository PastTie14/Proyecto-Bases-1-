package proyecto.bases;

import Connect.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.sql.SQLException;

/**
 *
 * @author Allan
 */
public class RegisterPage implements ActionListener {
    
    JFrame frame = new JFrame();
    JButton button = new JButton("Register");
    JTextField textField1 = new JTextField();
    JTextField textField2 = new JTextField();
    JTextField textField3 = new JTextField();
    
    RegisterPage() {
        
        textField1.setBounds(240, 100, 175, 30);
        textField2.setBounds(240, 150, 175, 30);
        textField3.setBounds(240, 200, 175, 30);
        
        button = new JButton();
        button.setBounds(280, 400, 100, 50);
        button.addActionListener(this);
        button.setText("Register");
        button.setFocusable(false);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        
        
        frame.setTitle("Mondongo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        //this.setResizable(false);
        frame.setSize(720, 720);
        frame.getContentPane().setBackground(new Color(0x35383D)); // set background color
        frame.add(button);
        frame.add(textField1);
        frame.add(textField2);
        frame.add(textField3);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String formattedDate = date.format(formatted);
        
        if (e.getSource() == button) {
            try {
                DBConnection.insertUser(textField1.getText(), textField2.getText(), textField3.getText(),
                        formattedDate, "Admin", formattedDate);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }
}
