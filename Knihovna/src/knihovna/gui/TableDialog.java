/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import knihovna.entity.Uzivatel;

/**
 *
 * @author wmatex
 */
public class TableDialog extends JDialog {
    private final Uzivatel user;
    public TableDialog(Uzivatel user, String title) {
        super((JDialog)null);
        this.user = user;
        init(title);
    }
    
    private void init(String title) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setLayout(new BorderLayout(10,10));
        JButton closeButton = new JButton("Zavřít");
        final JDialog dialog = this;
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });
        JPanel panel = new JPanel();
        panel.add(closeButton);
        add(panel, BorderLayout.SOUTH);
    }
    
    public void setTableFromResultSet(String[] columnNames, Object[][] data) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        c.gridy = 0;
        int x = 0;
        for (String name: columnNames) {
            c.gridx = x++;
            panel.add(new JLabel("<html><i>"+name+"</i></html>"), c);
        }
        
        Font font = new Font("Serif", Font.PLAIN, 11);
        int y = 1;
        for (Object[] row: data) {
            x = 0;
            for (Object o: row) {
                c.gridy = y;
                c.gridx = x;
                if (o instanceof Component) {
                    panel.add((Component) o, c);
                } else if (o instanceof Boolean) {
                    JCheckBox box = new JCheckBox();
                    box.setSelected((Boolean) o);
                    box.setEnabled(false);
                    panel.add(box, c);
                } else {
                    JLabel l = new JLabel(o.toString());
                    l.setFont(font);
                    panel.add(l, c);
                }
                x++;
            }
            y++;
        }
        
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }
}
