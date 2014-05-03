/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author tomanlu2
 */
public class NormalUserJPanel extends JPanel {
    User user;
    MainJFrame mainFrame;
    private JButton logoutJButton;
    private JLabel userJLabel;
    private JPanel userJPanel;
    public NormalUserJPanel(User user, MainJFrame mainFrame) {
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
       
        add(userJPanel, BorderLayout.NORTH );
    }

}
