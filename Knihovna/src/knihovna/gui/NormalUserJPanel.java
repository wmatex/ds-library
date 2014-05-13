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
import javax.swing.JButton;
import javax.swing.JPanel;
import knihovna.entity.Uzivatel;

/**
 *
 * @author tomanlu2
 */
public class NormalUserJPanel extends JPanel {
    Uzivatel user;
    MainJFrame mainFrame;
    
    public NormalUserJPanel(Uzivatel user, MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.user = user;
        init();
    }
    
    private void init(){
        setLayout(new BorderLayout());
        Dimension buttonsDim = new Dimension(200, 25);
        
        JPanel menuJPanel = new JPanel();
        
        JButton searchBookJButton = new JButton("Hledat knihu");
        JButton reservationsJButton = new JButton("Správa rezervací");
        reservationsJButton.setPreferredSize(buttonsDim);
        searchBookJButton.setPreferredSize(buttonsDim);
        menuJPanel.add(searchBookJButton);
        menuJPanel.add(reservationsJButton);
        searchBookJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SearchDialog searchFrame = 
                    new SearchDialog(null,
                    user, "Hledat knihu");
                searchFrame.setVisible(true);
            }
            
        });
        reservationsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                MainJFrame.showReservationDialog(user);
            }
            
        });
        add(menuJPanel, BorderLayout.CENTER);
    }

}
