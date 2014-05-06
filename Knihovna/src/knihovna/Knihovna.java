/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna;

import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import knihovna.gui.LoginJFrame;

/**
 *
 * @author Lukáš
 */
public class Knihovna {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
        DatabaseManager.init();
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(null, "Nemohu se připojit k databázi, zkontrolujte připojení", 
                "Chyba", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
        //DatabaseManager.destroy();
    }
    
}
