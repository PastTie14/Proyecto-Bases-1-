package proyecto.bases;

import Connect.DBConnection;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
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
    JButton buttonTest = new JButton("test"); 
    JTextField textField1 = new JTextField();
    JTextField textField2 = new JTextField();
    JTextField textField3 = new JTextField();
    
    RegisterPage() {
        
        textField1.setBounds(240, 100, 175, 30);
        textField2.setBounds(240, 150, 175, 30);
        textField3.setBounds(240, 200, 175, 30);
        
        // Botón Register
        button = new JButton();
        button.setBounds(280, 400, 100, 50);
        button.addActionListener(this);
        button.setText("Register");
        button.setFocusable(false);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        
        buttonTest.setBounds(280, 470, 100, 50);
        buttonTest.addActionListener(this);
        buttonTest.setText("Test");
        buttonTest.setFocusable(false);
        buttonTest.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        
        frame.setTitle("Mondongo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(720, 720);
        //this.setResizable(false);
        frame.getContentPane().setBackground(new Color(0x35383D));
        frame.add(button);
        frame.add(buttonTest); 
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
        
        // Botón Register
        if (e.getSource() == button) {
            try {
                DBConnection.insertUser(
                    textField1.getText(), textField2.getText(), textField3.getText(),
                    formattedDate, "Admin", formattedDate
                );
                System.out.println("Insertado Correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR al insertar.");
            }
        }
        
        if (e.getSource() == buttonTest) {
            try {
                ResultSet rs = DBConnection.getUsers();
                System.out.println("=== Lista de Usuarios ===");

                // ✅ Primero imprimí los nombres reales de las columnas
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getMetaData().getColumnName(i) + " | \t ");
                }
                System.out.println();

                // ✅ Luego imprimí los datos por índice, no por nombre
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs.getString(i) + " | ");
                    }
                    System.out.println();
                }

                System.out.println("=========================");

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR al obtener usuarios.");
            }
        }
    }
}