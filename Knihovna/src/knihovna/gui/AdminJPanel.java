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
        
        newBorrowingJButton = new JButton("Nov� v�puj�ka");
        reservationsJButton = new JButton("Spr�va rezervac�");
        newUserJButton = new JButton("Nov� u�ivatel");
        borrowsJButton = new JButton("V�p�j�ky");
        
        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        borrowsJButton.setPreferredSize(buttonsDim);
        
        newBorrowingJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email �ten��e", "�ten��", JOptionPane.QUESTION_MESSAGE);
                if(email != null){
                    Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        showBorrowDialog(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "�ten��: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        borrowsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        });
        
        newUserJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
                JPanel namePanel = new JPanel();
                JPanel passwordPanel = new JPanel();
                JPanel emailPanel = new JPanel();
                Box vBox = Box.createVerticalBox();
                
                JLabel nameJLabel = new JLabel("Jm�no: ");
                                
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
                    JOptionPane.showMessageDialog(null,"Novy uzivatel vytvoren","Novy uzivatel", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Nepodaril se vytvorit novy uzivatel ","Chyba", 
                        JOptionPane.ERROR_MESSAGE);
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

    private void showBorrowDialog(final Uzivatel user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0,10));

        JLabel label = new JLabel("<html>Zad�v�te v�puj�ky pro u�ivatele <i>" + 
            user.getKrestniJmeno() + " " + user.getPrijmeni() +
            "</i><br>Zadejte ��rov� k�d v�tisku</html>");
        panel.add(label, BorderLayout.NORTH);
        final JTextField textField = new JTextField(13);
        panel.add(textField, BorderLayout.CENTER);
        JButton loadButton = new JButton();
        loadButton.setText("Na��st k�d");
        panel.add(loadButton, BorderLayout.EAST);
        final JLabel infoLabel = new JLabel(" ");
        panel.add(infoLabel, BorderLayout.SOUTH);

        JButton create = new JButton();
        create.setText("Vytvo�it novou v�puj�ku");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String barcode = textField.getText();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    JOptionPane.showMessageDialog(null, "V�tisk s t�mto ��rov�m k�dem neexistuje",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        dbManager.createBorrowing(print, user);
                    } catch (EntityExistsException ex) {
                        JOptionPane.showMessageDialog(null, "Tento v�tisk je ji� p�j�en�",
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Vytvo�ena v�p�j�ka pro titul " + t.getNazev());
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
                    infoLabel.setText("V�tisk s t�mto ��rov�m k�dem neexistuje");
                    textField.selectAll();
                } else {
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Titul: " + t.getNazev());
                }
            }
            
        });
        final JButton cancel = new JButton("Zru�it");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane pane = getOptionPane((JComponent)ae.getSource());
                pane.setValue(cancel);
            }
            
        });
        JOptionPane.showOptionDialog(null,
            panel, 
            "V�puj�ka", 
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
