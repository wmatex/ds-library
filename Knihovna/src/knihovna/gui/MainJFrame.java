/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import knihovna.DatabaseManager;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwRezervace;
import knihovna.gui.TableDialog.ResultFetcher;

/**
 *
 * @author tomanlu2
 */
public class MainJFrame extends JFrame{
    private final Uzivatel user;
    private JPanel userPanel;
    private JPanel adminPanel = null;
    private JTabbedPane tabs;
    private JButton logoutJButton;
    private JLabel userJLabel;
    private JPanel infoJPanel;
    private JPanel settingsJPanel;
    private JButton settingsJButton;
    private JPanel logoutJPanel;
    
    public MainJFrame(Uzivatel user){
        super("Menu");
        this.user = user;
        init();
        
    }
    private void init(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        logoutJButton = new javax.swing.JButton();
        infoJPanel = new javax.swing.JPanel();
        userJLabel = new javax.swing.JLabel();
        settingsJPanel = new javax.swing.JPanel();
        settingsJButton = new javax.swing.JButton();
        logoutJPanel = new javax.swing.JPanel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        infoJPanel.setPreferredSize(new java.awt.Dimension(400, 40));
        
        logoutJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        infoJPanel.setLayout(new java.awt.BorderLayout());
        
        userJLabel.setText(user.getKrestniJmeno() + " " + user.getPrijmeni());
        logoutJButton.setText("Odhlásit se");
        logoutJPanel.add(userJLabel);
        logoutJPanel.add(logoutJButton);
        
        settingsJButton.setText("Nastavení");
        settingsJPanel.add(settingsJButton);
        
        infoJPanel.add(settingsJPanel,java.awt.BorderLayout.WEST);
        infoJPanel.add(logoutJPanel, java.awt.BorderLayout.EAST);
        
        getContentPane().add(infoJPanel, java.awt.BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(400,400));
        tabs = new JTabbedPane();
        userPanel = new NormalUserJPanel(user, this);
        tabs.addTab("Čtenář", userPanel);
        if (user.getRole() == 2) {
            adminPanel = new AdminJPanel(user, this);
            tabs.addTab("Zaměstnanec", adminPanel);
        }

        getContentPane().add(tabs, java.awt.BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        final MainJFrame parent = this;
        logoutJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                LoginJFrame login = new LoginJFrame();
                login.setVisible(true);
                parent.dispose();
            }
        });
        settingsJButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new javax.swing.JPanel();
                JButton passwordJButton = new javax.swing.JButton();
                passwordJButton.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JLabel oldPasswordJLabel = new javax.swing.JLabel();
                        JPasswordField oldPasswordJField = new javax.swing.JPasswordField(15);
                        JPanel oldPasswordJPanel = new javax.swing.JPanel();
                        
                        JLabel newPasswordJLabel = new javax.swing.JLabel();
                        JPasswordField newPasswordJField = new javax.swing.JPasswordField(15);
                        JPanel newPasswordJPanel = new javax.swing.JPanel();
                        
                        JLabel againPasswordJLabel = new javax.swing.JLabel();
                        JPasswordField againPasswordJField = new javax.swing.JPasswordField(15);
                        JPanel againPasswordJPanel = new javax.swing.JPanel();
                        
                        oldPasswordJLabel.setText("Staré heslo:");
                        newPasswordJLabel.setText("Nové heslo:");
                        againPasswordJLabel.setText("Nové heslo znovu:");
                        
                        oldPasswordJPanel.add(oldPasswordJLabel);
                        oldPasswordJPanel.add(oldPasswordJField);
                        
                        newPasswordJPanel.add(newPasswordJLabel);
                        newPasswordJPanel.add(newPasswordJField);
                        
                        againPasswordJPanel.add(againPasswordJLabel);
                        againPasswordJPanel.add(againPasswordJField);
                        
                        Box vBox = javax.swing.Box.createVerticalBox();
                        vBox.add(oldPasswordJPanel);
                        vBox.add(newPasswordJPanel);
                        vBox.add(againPasswordJPanel);
                        int option = JOptionPane.showConfirmDialog(null, vBox, "Změna hesla",JOptionPane.OK_CANCEL_OPTION);
                        if(option == JOptionPane.OK_OPTION){
                            String oldPassword = oldPasswordJField.getText();
                            String newPassword = newPasswordJField.getText();
                            String againPassword = againPasswordJField.getText();
                            if( "".equals(oldPassword) || "".equals(newPassword) || "".equals(againPassword)){
                                JOptionPane.showMessageDialog(null,"Vyplňte všechny údaje", "Chyba", JOptionPane.ERROR_MESSAGE);
                            }else{
                                try {
                                    if(user.getHeslo().equals(DatabaseManager.md5(oldPassword))){
                                        if(newPassword.equals(againPassword)){
                                            DatabaseManager dbManager = DatabaseManager.getInstance();
                                            user.setHeslo(dbManager.md5(newPassword));
                                            JOptionPane.showMessageDialog(null,"Heslo změněno", "Změna hesla", JOptionPane.PLAIN_MESSAGE);
                                        }else{
                                            JOptionPane.showMessageDialog(null,"Nová hesla se neshodují", "Chyba", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }else{
                                        JOptionPane.showMessageDialog(null,"Staré heslo je chybné", "Chyba", JOptionPane.ERROR_MESSAGE);
                                        
                                    }
                                } catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            }
                        }
                    }
                
                });
                passwordJButton.setText("Změna hesla");
                panel.add(passwordJButton);
                JOptionPane.showMessageDialog(null, panel, "Nastavení",JOptionPane.INFORMATION_MESSAGE);
            }
            
        });
    }

    public static void showReservationDialog(Uzivatel user) {
        ResultFetcher fetcher = new ResultFetcher() {
            @Override
            public Object[][] getResults(int pageno) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                List<VwRezervace> reservations = dbManager.getReservationsOfUser(user, pageno);
                if (reservations.isEmpty()) {
                    return null;
                }
                
                Object[][] data = new Object[reservations.size()][];
                int i = 0;
                for (VwRezervace row : reservations) {
                    data[i] = new Object[5];
                    data[i][0] = row.getNazev();
                    data[i][1] = row.getPoradi();
                    SimpleDateFormat ft1
                        = new SimpleDateFormat("d.M.y");
                    data[i][2] = ft1.format(row.getZajemDo());
                    data[i][3] = row.getSplnena();
                    Date expiration = row.getVyprsi();
                    if (expiration != null) {
                        SimpleDateFormat ft2
                            = new SimpleDateFormat("E d.M.y");
                        data[i][4] = ft2.format(expiration);
                    }
                    
                    i++;
                }
                return data;
            }
        };
        Object[][] initial = fetcher.getResults(0);
        if (initial == null) {
            JOptionPane.showMessageDialog(null,
                "Uživatel nemá žádné rezervace",
                "Žádné rezervace", JOptionPane.WARNING_MESSAGE);
        } else {
            String[] columnNames = {
                "Název",
                "Pořadí",
                "Zájem do",
                "Splněna",
                "Vyprší"
            };
            TableDialog tableDialog = new TableDialog(user, "Rezervace", fetcher);
            tableDialog.setInitial(columnNames, initial);
            tableDialog.setVisible(true);
        }
        
    }
    

}


