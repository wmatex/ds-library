/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import knihovna.entity.Uzivatel;

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        infoJPanel.setPreferredSize(new java.awt.Dimension(400, 40));
        infoJPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        userJLabel.setText(user.getKrestniJmeno() + " " + user.getPrijmeni());
        logoutJButton.setText("Odhlásit se");
        infoJPanel.add(userJLabel);
        infoJPanel.add(logoutJButton);

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
    

}


