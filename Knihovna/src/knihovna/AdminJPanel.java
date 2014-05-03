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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author tomanlu2
 */
public class AdminJPanel extends JPanel {
    User user;
    MainJFrame mainFrame;
    private JButton logoutJButton;
    private JLabel userJLabel;
    private JPanel userJPanel;
    private JPanel menuJPanel;
    private JButton newBorrowingJButton;
    private JButton reservationsJButton;
    private JButton newUserJButton;
    
    public AdminJPanel(User user, MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.user = user;
        init();
    }
    
    private void init(){
        setLayout(new BorderLayout());
        
        userJLabel = new JLabel("Prihlasen jako " + user.getName());
        logoutJButton = new JButton("Odhlasit");
        userJPanel = new JPanel();
        
        logoutJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                LoginJFrame login = new LoginJFrame();
                login.setVisible(true);
                mainFrame.dispose();
            }
        });
        userJPanel.add(userJLabel);
        userJPanel.add(logoutJButton);
        
        add(userJPanel, BorderLayout.SOUTH );
        
        Dimension buttonsDim = new Dimension(200, 25);
        
        menuJPanel = new JPanel();
        
        newBorrowingJButton = new JButton("Nova vypujcka");
        reservationsJButton = new JButton("Sprava rezervaci");
        newUserJButton = new JButton("Novy uzivatel");
        
        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        
        newBorrowingJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                 String barcode = JOptionPane.showInputDialog(null, "Zadejte carovy kod knihy", "Carovy kod", JOptionPane.QUESTION_MESSAGE);
                 if(barcode != null){
                    if(checkBarcode(barcode)){
                        String reader = JOptionPane.showInputDialog(null, "Zadejte email ctenare", "Ctenar", JOptionPane.QUESTION_MESSAGE);
                        if(reader != null){
                            if(checkReader(reader)){
                                //newBorrowing(barcode, reader);
                                 JOptionPane.showMessageDialog(null, "Vypujcka vytvorena", "Nova vypucjka", JOptionPane.PLAIN_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null, "Ctenar: '" + reader + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Vytisk s carovym kodem: '" + barcode + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        reservationsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String reader = JOptionPane.showInputDialog(null, "Zadejte email ctenare", "Ctenar", JOptionPane.QUESTION_MESSAGE);
                        if(reader != null){
                            if(checkReader(reader)){
                                Reservation[] possibleValues = getReservations(reader);
                                Reservation selectedValue = (Reservation)JOptionPane.showInputDialog(null, "Vyberte rezervaci k vypujceni", "Rezervace",
JOptionPane.INFORMATION_MESSAGE, null,
possibleValues, possibleValues[0]);
                                //newBorrowing(selectedValue, reader);
                                JOptionPane.showMessageDialog(null, "Vypujcka vytvorena", "Nova vypucjka", JOptionPane.PLAIN_MESSAGE);
                            
                             }else{
                                JOptionPane.showMessageDialog(null, "Ctenar: '" + reader + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                            }
                        }
            }
        });
        menuJPanel.add(newBorrowingJButton);
        menuJPanel.add(reservationsJButton);
        menuJPanel.add(newUserJButton);
        
        add(menuJPanel, BorderLayout.CENTER);
        
        
    }
    
    private boolean checkBarcode(String barcode){
        //najde knihu
        return barcode.equals("Ahoj");
    }

    private boolean checkReader(String reader){
        //najde ctenare
        return reader.equals("Neco");
    }
    
    private Reservation[] getReservations(String reader){
        Reservation[] reservations = new Reservation[5];
        reservations[0] = new Reservation(new User(reader, "asd", 0), new Book("Lukas", "neco", 1895, 30));
        reservations[1] = new Reservation(new User(reader, "gghjasd", 0), new Book("LNj", "nesado", 1895, 30));
        reservations[2] = new Reservation(new User(reader, "aghjsd", 0), new Book("Luasdsad", "nasdasdo", 1895, 30));
        return reservations;
        
    }
}
