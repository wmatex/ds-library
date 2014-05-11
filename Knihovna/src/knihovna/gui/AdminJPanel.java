/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import knihovna.DatabaseManager;
import knihovna.entity.Titul;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwRezervace;
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
        
        newBorrowingJButton = new JButton("Nová výpůjčka");
        reservationsJButton = new JButton("Správa rezervací");
        newUserJButton = new JButton("Nový uživatel");
        borrowsJButton = new JButton("Výpujčky");
        
        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        borrowsJButton.setPreferredSize(buttonsDim);
        
        newBorrowingJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                if(email != null){
                    Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        showBorrowDialog(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        borrowsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout(0,10));

                JLabel label = new JLabel("Zadejte čárové kódy knih k vrácení");
        panel.add(label, BorderLayout.NORTH);
        final JTextField textField = new JTextField(13);
        panel.add(textField, BorderLayout.CENTER);
        JButton loadButton = new JButton();
        loadButton.setText("Načíst kód");
        panel.add(loadButton, BorderLayout.EAST);
        final JLabel infoLabel = new JLabel(" ");
        panel.add(infoLabel, BorderLayout.SOUTH);

        JButton create = new JButton();
        create.setText("Vrátit knihu");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String barcode = textField.getText();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    JOptionPane.showMessageDialog(null, "Výtisk s tímto čárovým kódem neexistuje",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        
                    } catch (EntityExistsException ex) {
                        JOptionPane.showMessageDialog(null, "Tento výtisk je již půjčený",
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Vytvořena výpůjčka pro titul " + t.getNazev());
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
            JOptionPane.showOptionDialog(null,
            panel, 
            "Výpůjčka", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] {create},
            create);}
            
        });
        
        newUserJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                JPanel namePanel = new JPanel();
                JPanel surnamePanel = new JPanel();
                JPanel emailPanel = new JPanel();
                Box vBox = Box.createVerticalBox();
                
                JLabel nameJLabel = new JLabel("Jméno: ");
                                
                JTextField nameJTextField = new JTextField(10);
                
                namePanel.add(nameJLabel);
                namePanel.add(nameJTextField);
                
                JLabel surnameJLabel = new JLabel("Příjmení: ");
                
                JTextField surnameJTextField = new JTextField(10);
                
                surnamePanel.add(surnameJLabel);
                surnamePanel.add(surnameJTextField);
                
                JLabel emailJLabel = new JLabel("Email: ");
                
                JTextField emailJTextField = new JTextField(10);
                
                emailPanel.add(emailJLabel);
                emailPanel.add(emailJTextField);
                
                vBox.add(namePanel);
                vBox.add(surnamePanel);
                vBox.add(emailPanel);
                while(true){
                int result = JOptionPane.showConfirmDialog(null,vBox,"Nový uživatel", JOptionPane.OK_CANCEL_OPTION );
                if(result == JOptionPane.CLOSED_OPTION || result == JOptionPane.CANCEL_OPTION)
                    break;
                if("".equals(nameJTextField.getText()) || "".equals(surnameJTextField.getText()) || "".equals(emailJTextField.getText())){
                    JOptionPane.showMessageDialog(null,"Vyplňte všechny údaje","Upozornění", 
                        JOptionPane.WARNING_MESSAGE);
                }
                else{
                    String password = newUser(nameJTextField.getText(), surnameJTextField.getText(), emailJTextField.getText());
                    if(password != null){
                    JOptionPane.showMessageDialog(null,"Vytvořen uživatel '" + emailJTextField.getText() + "' s heslem: "+ password ,"Vytvořeno", 
                        JOptionPane.INFORMATION_MESSAGE);break;
                }
                }
            }}
        });
        
        reservationsJButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                if(email != null){
                    Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        List<VwRezervace> reservations = dbManager.getReservationsOfUser(user);
                        if (reservations.isEmpty()) {
                            JOptionPane.showMessageDialog(null, 
                            "Uživatel nemá žádné rezervace",
                            "Žádné rezervace", JOptionPane.WARNING_MESSAGE);
                        } else {
                            String[] columnNames = {
                            "Název",
                            "Autor",
                            "Pořadí"                           
                         };
                            Object[][] data = new Object[reservations.size()][];
                            int i = 0;
                            for (VwRezervace row: reservations) {
                                data[i] = new Object[3];
                                data[i][0] = row.getNazev();
                                data[i][1] = row.getSplnena();
                                data[i][2] = row.getPoradi();
                                
                                i++;
                    }TableDialog tableDialog = new TableDialog(user, "Rezervace");
                    tableDialog.setTableFromResultSet(columnNames, data);
                    tableDialog.setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        });
        menuJPanel.add(newBorrowingJButton);
        menuJPanel.add(reservationsJButton);
        menuJPanel.add(newUserJButton);
        menuJPanel.add(borrowsJButton);
        
        add(menuJPanel, BorderLayout.CENTER);
    }
    
    private String newUser(String name, String surname, String email){
        DatabaseManager dbManager = DatabaseManager.getInstance();
        try {
            dbManager.createUser(name, surname, email);
        } catch (EntityExistsException ex) {
            JOptionPane.showMessageDialog(null, "Email je již používán","Chyba", JOptionPane.ERROR_MESSAGE);
                        return null;
        }
        return dbManager.getUserByEmail(email).getHeslo();
        
    }
    
    private void showBorrowDialog(final Uzivatel user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0,10));

        JLabel label = new JLabel("<html>Zadáváte výpůjčky pro uživatele <i>" + 
            user.getKrestniJmeno() + " " + user.getPrijmeni() +
            "</i><br>Zadejte čárový kód výpůjčky</html>");
        panel.add(label, BorderLayout.NORTH);
        final JTextField textField = new JTextField(13);
        panel.add(textField, BorderLayout.CENTER);
        JButton loadButton = new JButton();
        loadButton.setText("Načíst kód");
        panel.add(loadButton, BorderLayout.EAST);
        final JLabel infoLabel = new JLabel(" ");
        panel.add(infoLabel, BorderLayout.SOUTH);

        JButton create = new JButton();
        create.setText("Vytvořit výpůjčku");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String barcode = textField.getText();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    JOptionPane.showMessageDialog(null, "Výtisk s tímto čárovým kódem neexistuje",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        dbManager.createBorrowing(print, user);
                    } catch (EntityExistsException ex) {
                        JOptionPane.showMessageDialog(null, "Tento výtisk je již půjčený",
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Vytvořena výpůjčka pro titul " + t.getNazev());
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String barcode = textField.getText();
                DatabaseManager dbManager = DatabaseManager.getInstance();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    infoLabel.setText("Výtisk s tímto čárovým kódem neexistuje");
                    textField.selectAll();
                } else {
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Titul: " + t.getNazev());
                }
            }
            
        });
        final JButton cancel = new JButton("Zrušit");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane pane = getOptionPane((JComponent)ae.getSource());
                pane.setValue(cancel);
            }
            
        });
        JOptionPane.showOptionDialog(null,
            panel, 
            "Výpůjčka", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[] {create, cancel},
            create);
    }

    private JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane = null;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }

    private static Object multipleChoices(String message, String title, Object[] choices) {
        return JOptionPane.showInputDialog(null, message, title,
            JOptionPane.QUESTION_MESSAGE, null,
            choices, choices[0]);
    }
}
