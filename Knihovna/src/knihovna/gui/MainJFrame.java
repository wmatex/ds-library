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
import knihovna.entity.VwVypujcka;
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
        logoutJPanel = new javax.swing.JPanel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        infoJPanel.setPreferredSize(new java.awt.Dimension(400, 40));
        
        logoutJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        infoJPanel.setLayout(new java.awt.BorderLayout());
        
        userJLabel.setText(user.getKrestniJmeno() + " " + user.getPrijmeni());
        logoutJButton.setText("Odhlásit se");
        logoutJPanel.add(userJLabel);
        logoutJPanel.add(logoutJButton);
        
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
    }

    public static void showReservationDialog(final Uzivatel user) {
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
    
    public static void showBorrowDialog(final Uzivatel user) {
        ResultFetcher fetcher = new ResultFetcher() {
            @Override
            public Object[][] getResults(int pageno) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                List<VwVypujcka> borrows = dbManager.getBorrowsOfUser(user, pageno);
                if (borrows.isEmpty()) {
                    return null;
                }
                
                Object[][] data = new Object[borrows.size()][];
                int i = 0;
                for (VwVypujcka row : borrows) {
                    Date now    = new Date();
                    Date ret    = row.getDatumVraceni();
                    int diffInDays = (int)Math.ceil((ret.getTime() - now.getTime()) 
                        / (1000 * 60 * 60 * 24.0));
                    data[i] = new Object[4];
                    data[i][0] = row.getNazev();
                    SimpleDateFormat ft1
                        = new SimpleDateFormat("d.M.y");
                    data[i][1] = ft1.format(row.getDatumPujceni());
                    SimpleDateFormat ft2
                        = new SimpleDateFormat("E d.M.y");
                    String retDate = ft2.format(ret);
                    String message = "";
                    if (diffInDays <= 0) {
                        message = "<html><b>" + retDate + " (";
                        String s = "";
                        if (diffInDays == 0) {
                            s = "dnes";
                        } else if (diffInDays == -1) {
                            s = "včera";
                        } else {
                            s = "před " + (diffInDays*(-1)) + " dny";
                        }
                        message += s + ")</b></html>";
                    } else {
                        message = retDate + " (";
                        String s = "";
                        if (diffInDays == 1) {
                            s = "zítra";
                        } else {
                            s = "za " + diffInDays + " dní";
                        }
                        message += s + ")";
                    }
                    data[i][2] = message;
                    
                    i++;
                }
                return data;
            }
        };
        Object[][] initial = fetcher.getResults(0);
        if (initial == null) {
            JOptionPane.showMessageDialog(null,
                "Uživatel nemá žádné výpujčky",
                "Žádné výpůjčky", JOptionPane.WARNING_MESSAGE);
        } else {
            String[] columnNames = {
                "Titul",
                "Půjčeno",
                "Vrátit do"
            };
            TableDialog tableDialog = new TableDialog(user, "Výpůjčky", fetcher);
            tableDialog.setInitial(columnNames, initial);
            tableDialog.setVisible(true);
        }
        
    }

}


