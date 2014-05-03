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
    private JButton borrowsJButton;
    
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
        borrowsJButton = new JButton("Vypujcky");
        
        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        borrowsJButton.setPreferredSize(buttonsDim);
        
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
                                Reservation[] reservations = getReservations(reader);
                                JList list = new JList(reservations);
                                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                                list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                                list.setVisibleRowCount(-1);
                                JScrollPane listScroller = new JScrollPane(list);
                                Box vBox = Box.createVerticalBox();
                                JLabel label = new JLabel("Vyberte rezervace");
                                vBox.add(label);
                                vBox.add(listScroller);
                                JOptionPane.showMessageDialog(null,vBox,"Rezervace",JOptionPane.INFORMATION_MESSAGE);
                                Object[] selectedObjects = list.getSelectedValues();
                                Reservation[] selectedReservations = new Reservation[selectedObjects.length];
                                for(int i = 0; i < selectedObjects.length; i++){
                                    selectedReservations[i] = (Reservation)selectedObjects[i];
                                }
                                //newBorrowing(selectedReservations, reader);
                               if(selectedReservations[0] != null)
                                JOptionPane.showMessageDialog(null, "Vypujcka vytvorena", "Nova vypucjka", JOptionPane.PLAIN_MESSAGE);
                             
                             }else{
                                JOptionPane.showMessageDialog(null, "Ctenar: '" + reader + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                            }
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
        
        borrowsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String reader = JOptionPane.showInputDialog(null, "Zadejte email ctenare", "Ctenar", JOptionPane.QUESTION_MESSAGE);
                        if(reader != null){
                            if(checkReader(reader)){
                                Borrowing[] borrows = getBorrows(reader);
                                Reservation selectedBorrow = (Reservation)JOptionPane.showInputDialog(null, "Vyberte rezervaci k vypujceni", "Rezervace",
JOptionPane.INFORMATION_MESSAGE, null,
borrows, borrows[0]);
                                //newBorrowing(selectedValue, reader);
                                if(selectedBorrow != null)
                                JOptionPane.showMessageDialog(null, "Vypujcka " + selectedBorrow +" vracena", "Vracena vypujcka ", JOptionPane.PLAIN_MESSAGE);
                            
                             }else{
                                JOptionPane.showMessageDialog(null, "Ctenar: '" + reader + "' neexistuje", "Chyba", JOptionPane.ERROR_MESSAGE);
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
    
    private boolean checkBarcode(String barcode){
        //najde knihu
        return barcode.equals("Ahoj");
    }

    private boolean checkReader(String reader){
        //najde ctenare
        return reader.equals("Neco");
    }
    
    private Reservation[] getReservations(String reader){
        Reservation[] reservations = new Reservation[10];
        reservations[0] = new Reservation(new User(reader, "asd", 0), new Book("Lukas", "neco", 1895, 30));
        reservations[1] = new Reservation(new User(reader, "gghjasd", 0), new Book("LNj", "nesado", 1895, 30));
        reservations[2] = new Reservation(new User(reader, "aghjsd", 0), new Book("Luasdsasdasdsadsadsadasd", "nasdasdo", 1895, 30));
        return reservations;
        
    }
    
    private boolean newUser(String name, String email, String password){
        return true;
        
    }
    
    private Borrowing[] getBorrows(String reader) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
}
