/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import knihovna.entity.Uzivatel;

/**
 *
 * @author wmatex
 */
public class SearchDialog extends JDialog {
    private Uzivatel user;
    private JPanel searchPanel;
    private JPanel buttonPanel;

    public SearchDialog(JFrame parent, Uzivatel user, String title) {
        super(parent);
        this.user = user;
        init(parent);
        this.setTitle(title);
    }

    private void init(JFrame parent) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(10,10));
        this.setModal(true);

        searchPanel = new JPanel(new BorderLayout(10, 10));
        Box hbox = Box.createHorizontalBox();
        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Zadejte název knihy");
        JTextField nameField = new JTextField(12);
        namePanel.add(nameLabel);
        
        namePanel.add(nameField);
        hbox.add(namePanel);
        searchPanel.add(hbox, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout());
        JButton searchButton = new JButton("Hledat");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            }
            
        });
        JButton closeButton = new JButton("Zavřít");
        final JDialog parentDialog = this;
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parentDialog.dispose();
            }
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }
}
