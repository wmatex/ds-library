/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package knihovna.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import knihovna.DatabaseManager;
import knihovna.entity.Autor;
import knihovna.entity.Titul;
import knihovna.entity.VwTitul;
import knihovna.entity.Zanr;

/**
 *
 * @author wmatex
 */
public class TitleDialog extends JDialog {
    private JTextField nameField;
    private JTextField yearField;
    private JTextField borrowTimeField;
    private JTextField priceField;
    private JComboBox  genreCombo;
    private final ArrayList<JCheckBox> authorCheckBox = new ArrayList<>();
    private final ArrayList<JComboBox> authorNameField = new ArrayList<>();
    private JButton addLineButton;
    private JPanel authorPanel;
    private int lineIndex = 0;
    private VwTitul book;
    private boolean insert;

    public TitleDialog(String title, VwTitul book) {
        // If book is null then this is INSERT operation
        this.insert = book == null;
        this.book = book;
        init(title);
    }

    private void init(String title) {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(title);
        this.setModal(true);

        setLayout(new BorderLayout(10,10));
        Box vbox = Box.createVerticalBox();
        vbox.setBorder(new EmptyBorder(10,10,10,10));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        GridBagConstraints cLabel = new GridBagConstraints();
        GridBagConstraints cText  = new GridBagConstraints();
        cLabel.anchor = GridBagConstraints.EAST;
        cLabel.fill = GridBagConstraints.NONE;
        cLabel.insets = new Insets(5,5,5,5);
        cLabel.gridx = 0;
        cText.anchor = GridBagConstraints.WEST;
        cText.fill = GridBagConstraints.NONE;
        cText.insets = new Insets(5,5,5,5);
        cText.gridx = 1;

        cLabel.gridy = cText.gridy = 0;
        titlePanel.add(new JLabel("Název :"), cLabel);
        nameField = new JTextField(30);
        titlePanel.add(nameField, cText);

        cLabel.gridy = cText.gridy = 1;
        titlePanel.add(new JLabel("Rok vydání :"), cLabel);
        yearField = new JTextField(4);
        titlePanel.add(yearField, cText);

        cLabel.gridy = cText.gridy = 2;
        titlePanel.add(new JLabel("Výpůjční doba :"), cLabel);
        borrowTimeField = new JTextField(2);
        titlePanel.add(borrowTimeField, cText);

        cLabel.gridy = cText.gridy = 3;
        titlePanel.add(new JLabel("Cena :"), cLabel);
        priceField = new JTextField(7);
        titlePanel.add(priceField, cText);

        cLabel.gridy = cText.gridy = 4;
        titlePanel.add(new JLabel("Žánr: "), cLabel);
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String[] genres = dbManager.getGenres().toArray(new String[0]);
        genreCombo = new JComboBox(genres);
        genreCombo.setSelectedIndex(-1);
        titlePanel.add(genreCombo, cText);

        if (!insert) {
            prefillValues();
        }

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);

        authorPanel = new JPanel();
        authorPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5,5,5,5);

        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        authorPanel.add(new JLabel("Autoři :"), c);

        final List<Autor> authors = dbManager.getAllAuthors();
        lineIndex = 1;
        if (insert) {
            createAuthorLine(authors, null, 1);
            lineIndex++;
        } else {
            Collection<Autor> bookAuthors = book.getAutors();
            for (Autor a: bookAuthors) {
                createAuthorLine(authors, a, lineIndex);
                lineIndex++;
            }
        }

        c.gridy = lineIndex;
        c.gridwidth = 2;
        addLineButton = new JButton("Další autor");
        final JDialog dialog = this;
        addLineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                authorPanel.remove(addLineButton);
                lineIndex++;
                createAuthorLine(authors, null, lineIndex);
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;
                c.gridx = 0;
                c.gridy = lineIndex+1;
                c.gridwidth = 2;
                c.insets = new Insets(5,5,5,5);
                authorPanel.add(addLineButton, c);
                authorPanel.revalidate();
            }
        });
        authorPanel.add(addLineButton, c);

        authorPanel.add(sep);
        vbox.add(titlePanel);
        vbox.add(sep);
        vbox.add(authorPanel);
        JScrollPane scrollPane = new JScrollPane(vbox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Uložit");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (apply()) {
                    if (insert == true) {
                        JOptionPane.showMessageDialog(dialog, "Nový titul byl úspěšně vložen do databáze",
                            "Úspěch", JOptionPane.INFORMATION_MESSAGE);
                        insert = false;
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Titul byl úspěšně změněm v databázi", 
                            "Úspěch", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                }
            }
        });
        JButton cancelButton = new JButton("Zrušit");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void createAuthorLine(List<Autor> authors, Autor preselected, int gridy) {
        String[] names = new String[authors.size()];
        int i = 0;
        int pIndex = -1;
        for (Autor a: authors) {
            names[i] = a.getJmeno() + " " + a.getPrijmeni();
            if (a.equals(preselected)) {
                pIndex = i;
            }
            i++;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridy = gridy;
        c.insets = new Insets(5,5,5,5);

        c.gridx = 0;
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(true);
        authorCheckBox.add(checkBox);
        authorPanel.add(checkBox, c);

        c.gridx = 2;
        JComboBox box = new JComboBox(names);
        box.setSelectedIndex(pIndex);
        box.setEditable(true);
        authorNameField.add(box);
        authorPanel.add(box, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        authorPanel.add(new JLabel("Jméno: "), c);

    }

    private void prefillValues() {
        if (book != null) {
            nameField.setText(book.getNazev());
            yearField.setText(book.getRokVydani().toString());
            borrowTimeField.setText(book.getVypujcniDobaDny().toString());
            priceField.setText(book.getCena().toPlainString());
            genreCombo.setSelectedItem(book.getZanr());
        }
    }

    public boolean apply() {
        if (!checkInput()) {
            return false;
        }
        EntityTransaction t = DatabaseManager.getInstance().getTransaction();
        t.begin();
        if (!commitToDatabase()) {
            t.rollback();
            return false;
        }
        t.commit();
        return true;
    }

    private boolean commitToDatabase() {
        String titleName = nameField.getText().trim();
        short year = Short.parseShort(yearField.getText().trim());
        short borrowTime = Short.parseShort(borrowTimeField.getText().trim());
        String price = priceField.getText().trim();
        String genre = ((String)genreCombo.getSelectedItem()).trim();

        DatabaseManager dbManager = DatabaseManager.getInstance();
        if (insert) {
            book = new VwTitul();
        }
        book.setNazev(titleName);
        book.setRokVydani(year);
        book.setVypujcniDobaDny(borrowTime);
        book.setCena(new BigDecimal(price));
        book.setZanr(genre);

        try {
            if (insert) {
                dbManager.persist(book);
            } else {
                dbManager.merge(book);
            }
        } catch (PersistenceException e) {
            if (insert) {
                book = null;
            }
            return showError("Titul s tímto názvem již v databázi existuje");
        }

        try {
            if (!insert) {
                dbManager.deleteAllBookAuthors(book);
            }
            // Insert authors
            for (int i = 0; i < authorCheckBox.size(); i++) {
                if (authorCheckBox.get(i).isSelected() &&
                    authorNameField.get(i).getSelectedItem() != null) {
                    String authorName = ((String)authorNameField.get(i).getSelectedItem()).trim();
                    String[] parts = authorName.split("\\s");
                    String firstName = "";
                    for (int j = 0; j < parts.length-2; j++) {
                        firstName += parts[j] + " ";
                    }
                    firstName += parts[parts.length-2];
                    String lastName = parts[parts.length-1];
                    System.out.println(firstName + "|" + lastName);
                    
                    dbManager.insertAuthor(book.getIdTitul(), firstName, lastName);
                }
            }
            dbManager.clear();
        } catch (PersistenceException e) {
            if (insert) {
                book = null;
            }
            return showError("Titul může mít pouze jednoho stejného autora");
        }
        
        return true;
    }

    private boolean checkInput() {
        if (nameField.getText().isEmpty()) {
            return showError("Název nesmí být prázdný");
        } else if (!yearField.getText().matches("^\\s*[0-9]{4}\\s*$")) {
            return showError("Zadávejte rok vydání ve formátu: RRRR");
        } else if (!borrowTimeField.getText().matches("^\\s*[0-9]{1,2}\\s*$")) {
            return showError("Zadávejte výpůjční dobu ve tvaru: xx");
        } else if (!priceField.getText().matches("^\\s*[0-9]{1,5}(\\.[0-9]{1,2})?\\s*")) {
            return showError("Zadávejte cenu ve formátu: xxxxx.xx");
        } else if (genreCombo.getSelectedIndex() < 0) {
            return showError("Žánr musí být vybrán");
        } else {
            boolean valid = false;
            for (int i = 0; i < authorCheckBox.size(); i++) {
                if (authorCheckBox.get(i).isSelected() && 
                    authorNameField.get(i).getSelectedItem() != null) {
                    String authorName = (String) authorNameField.get(i).getSelectedItem();
                    if (!authorName.matches("^\\s*(\\p{L}+\\s)+\\p{L}+\\s*$")) {
                        return showError("Autor: "+authorName+" musí obsahovat "
                            + "alespoň jedno jméno a příjmení oddělené jednou mezerou");
                    } else {
                        valid = true;
                    }
                }
            }
            if (!valid) {
                return showError("Titul musí mít alespoň jednoho autora");
            } else {
                return true;
            }
        }
    }

    private boolean showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Chyba", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
