/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author tomanlu2
 */
public class LoginJFrame extends JFrame{
    private JLabel informationJLabel;
    private JLabel nameJLabel;
    private JTextField nameJTextField;
    private JLabel passwordJLabel;
    private JPasswordField passwordJPasswordField;
    private JButton okJButton;
    private JPanel namePanel;
    private JPanel passwordPanel;
    private Box vBox;
    private JPanel buttonPanel;
    
    public LoginJFrame(){
        super("Login");
        init();
    }
    
    private void init(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(720, 200));
        setLayout(new FlowLayout());
        
        Dimension labelsDim = new Dimension(50, 25);
        Dimension fieldsDim = new Dimension(150, 25);
        
        vBox = Box.createVerticalBox();
        namePanel = new JPanel();
        passwordPanel = new JPanel();
        buttonPanel = new JPanel();
        
        informationJLabel = new JLabel();
        informationJLabel.setText("Vitejte v systemu knihovny. Prosim prihalste se.");
           
        nameJLabel = new JLabel("Jmeno: ");
        nameJLabel.setPreferredSize(labelsDim);
        
        nameJTextField = new JTextField();
        nameJTextField.setPreferredSize(fieldsDim);
        nameJTextField.addActionListener(new LoginActionListener());
        
        passwordJLabel = new JLabel("Heslo: ");
        passwordJLabel.setPreferredSize(labelsDim);
        
        passwordJPasswordField = new JPasswordField();
        passwordJPasswordField.setPreferredSize(fieldsDim);
        passwordJPasswordField.addActionListener(new LoginActionListener());
        
        okJButton = new JButton("Login");
        okJButton.addActionListener(new LoginActionListener());
       
        namePanel.add(nameJLabel);
        namePanel.add(nameJTextField);
       
        passwordPanel.add(passwordJLabel);
        passwordPanel.add(passwordJPasswordField);
        
        buttonPanel.add(okJButton);
        
        vBox.add(informationJLabel);
        vBox.add(namePanel);
        vBox.add(passwordPanel);
        vBox.add(buttonPanel);
        add(vBox);

        pack();
    }  
    
    private class LoginActionListener implements ActionListener{
        String name;
        String password;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            name = nameJTextField.getText();
            password = passwordJPasswordField.getText();
            if(name.equals("") || password.equals("")){
                 JOptionPane.showMessageDialog(LoginJFrame.this, "Nevyplneno jmeno nebo heslo", "Upozorneni", JOptionPane.WARNING_MESSAGE);
            }
            else{ 
                User user = getUser(name, password);
                if(  user != null){
                    MainJFrame jm = new MainJFrame(user);
                    jm.setVisible(true);
                    dispose();    
                }
                else{
                    passwordJPasswordField.setText("");
                    JOptionPane.showMessageDialog(LoginJFrame.this, "Spatne jmeno nebo heslo", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        private User getUser(String name, String password){
            return new User("Lukas", "bla@bla.cz", 1);
            
        }
    }
}
