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
import java.util.Collection;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import knihovna.DatabaseManager;
import knihovna.entity.Autor;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwTitul;

/**
 *
 * @author wmatex
 */
public class SearchDialog extends JDialog {
    private Uzivatel user;
    private JPanel searchPanel;
    private JPanel buttonPanel;
    private JTextField nameField;

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
        nameField = new JTextField(12);
        namePanel.add(nameLabel);
        
        namePanel.add(nameField);
        hbox.add(namePanel);
        searchPanel.add(hbox, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout());
        JButton searchButton = new JButton("Hledat");
        final SearchDialog dialog = this;
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String criteria = nameField.getText();
                Collection<VwTitul> titules = dbManager.searchTitles(criteria, 0);
                if (titules.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                        "Vašemu kritériu neodpovídá žádný titul",
                        "Žádný výsledek", JOptionPane.WARNING_MESSAGE);
                } else {
                    String[] columnNames = {
                        "Název",
                        "Autor",
                        "Rok vydání",
                        "Žánr",
                        "Volné výtisky"
                    };
                    Object[][] data = new Object[titules.size()][];
                    int i = 0;
                    for (VwTitul row: titules) {
                        data[i] = new Object[5];
                        data[i][0] = row.getNazev();
                        data[i][1] = generateAutorString(row);
                        data[i][2] = row.getRokVydani();
                        data[i][3] = row.getZanr();
                        data[i][4] = row.getVolneVytisky();
                        i++;
                    }

                    TableDialog tableDialog = new TableDialog(user, "Výsledky hledání");
                    tableDialog.setTableFromResultSet(columnNames, data);
                    tableDialog.setVisible(true);
                    
                }
            }
            
        };

        searchButton.addActionListener(listener);
        nameField.addActionListener(listener);
        JButton closeButton = new JButton("Zavøít");
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

    private String generateAutorString(VwTitul titul) {
        String autorString = "";
        Collection<Autor> authors = titul.getAutors();
        String delim = "";
        for (Autor author: authors) {
            autorString += delim + author.getJmeno() + " " + author.getPrijmeni();
            delim = ", ";
        }

        return autorString;
    }
}
