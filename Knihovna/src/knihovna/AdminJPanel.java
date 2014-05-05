/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import knihovna.entity.Titul;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwVypujcka;
import knihovna.entity.Vytisk;

/**
 *
 * @author tomanlu2
 */
public class AdminJPanel extends JPanel {
    Uzivatel user;
    MainJFrame mainFrame;
    private JPanel menuJPanel;
    private JButton newBorrowingJButton;
    private JButton reservationsJButton;
    private JButton newUserJButton;
    private JButton borrowsJButton;
    
    public AdminJPanel(Uzivatel user, MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.user = user;
        init();
    }
    
    private void init(){
        setLayout(new BorderLayout());
        Dimension buttonsDim = new Dimension(200, 25);
        
        menuJPanel = new JPanel();
        
        newBorrowingJButton = new JButton("Nova vypujcka");
        reservationsJButton = new JButton("Sprava rezervaci");
        newUserJButton = new JButton("Novy uzivatel");
        borrowsJButton = new JButton("Vypujcky");
        
        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        borrowsJButton.setPreferredSize(buttonsDim);
        
        newBorrowingJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String barcode = JOptionPane.showInputDialog(null, "Zadejte čárový kód výtisku", "Čárový kód", 
                     JOptionPane.QUESTION_MESSAGE);
                if(barcode != null){
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    Vytisk print = dbManager.getPrintByBarcode(barcode);
                    if (print != null) {
                        Titul titul = print.getIdTitul();
                        String email = JOptionPane.showInputDialog(null, "Titul: " + titul.getNazev() + 
                            "\nZadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                        if(email != null){
                            Uzivatel user = dbManager.getUserByEmail(email);
                            if(email != null){
                                dbManager.createBorrowing(print, user);
                                 JOptionPane.showMessageDialog(null, "Vypujcka vytvorena", "Nová výpůjčka", 
                                     JOptionPane.PLAIN_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Výtisk s čárovým kódem: '" + barcode + "' neexistuje", 
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        borrowsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                List<Uzivatel> users = dbManager.getUsers();
                Uzivatel selectedUser = (Uzivatel)AdminJPanel.multipleChoices("Vyberte čtenáře", "Čtenář", users.toArray());
                if(selectedUser != null){
                    List<VwVypujcka> borrows = dbManager.getBorrowsOfUser(selectedUser);
                    AdminJPanel.multipleChoices("Seznam výpůjček", "Výpůjčky", borrows.toArray());
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Ctenar: '" + selectedUser.toString() + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        newUserJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                JPanel namePanel = new JPanel();
                JPanel passwordPanel = new JPanel();
                JPanel emailPanel = new JPanel();
                Box vBox = Box.createVerticalBox();
                
                JLabel nameJLabel = new JLabel("Jmeno: ");
                                
                JTextField nameJTextField = new JTextField(10);
                
                namePanel.add(nameJLabel);
                namePanel.add(nameJTextField);
                
                JLabel passwordJLabel = new JLabel("Heslo: ");
                
                JPasswordField passwordJField = new JPasswordField(10);
                
                passwordPanel.add(passwordJLabel);
                passwordPanel.add(passwordJField);
                
                JLabel emailJLabel = new JLabel("Email: ");
                
                JTextField emailJTextField = new JTextField(10);
                
                emailPanel.add(emailJLabel);
                emailPanel.add(emailJTextField);
                
                vBox.add(namePanel);
                vBox.add(passwordPanel);
                vBox.add(emailPanel);
                
                JOptionPane.showMessageDialog(null,vBox,"Novy uzivatel",JOptionPane.INFORMATION_MESSAGE);
                if(newUser(nameJTextField.getText(), emailJTextField.getText(), passwordJField.getText())){
                    JOptionPane.showMessageDialog(null,"Novy uzivatel vytvoren","Novy uzivatel", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Nepodaril se vytvorit novy uzivatel ","Chyba", JOptionPane.ERROR_MESSAGE);
               
                }
                
                
            }
        });
        
        
        menuJPanel.add(newBorrowingJButton);
        menuJPanel.add(reservationsJButton);
        menuJPanel.add(newUserJButton);
        menuJPanel.add(borrowsJButton);
        
        add(menuJPanel, BorderLayout.CENTER);
        
        
    }
    
    private boolean newUser(String name, String email, String password){
        return true;
        
    }

    private static Object multipleChoices(String message, String title, Object[] choices) {
        return JOptionPane.showInputDialog(null, message, title,
            JOptionPane.QUESTION_MESSAGE, null,
            choices, choices[0]);
    }
}
