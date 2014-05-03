/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author tomanlu2
 */
public class MainJFrame extends JFrame{
    private final User user;
    private JPanel mainJPanel;
    
    public MainJFrame(User user){
        super("Menu");
        this.user = user;
        init();
        
    }
    private void init(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(400,400));
        switch(user.getType()){
            case 0:
                mainJPanel = new NormalUserJPanel(user, this);
                break;
            case 1:
                mainJPanel = new AdminJPanel(user, this);
                break;
                
        }
        
        add(mainJPanel);
        pack();
    }
    

}


