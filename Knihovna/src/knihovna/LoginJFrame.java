/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knihovna;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import static java.lang.System.exit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import knihovna.entity.Uzivatel;

/**
 *
 * @author tomanlu2
 */
public class LoginJFrame extends JFrame {

    private JLabel informationJLabel;
    private JLabel nameJLabel;
    private JTextField nameJTextField;
    private JLabel passwordJLabel;
    private JPasswordField passwordJPasswordField;
    private JButton okJButton;
    private JPanel namePanel;
    private JPanel passwordPanel;
    private Box vBox;
    private JPanel buttonPanel;

    public LoginJFrame() {
        super("Přihlášení");
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(720, 200));
        setLayout(new FlowLayout());

        Dimension labelsDim = new Dimension(60, 25);
        Dimension fieldsDim = new Dimension(150, 25);

        vBox = Box.createVerticalBox();
        namePanel = new JPanel();
        passwordPanel = new JPanel();
        buttonPanel = new JPanel();

        informationJLabel = new JLabel();
        informationJLabel.setText("Vítejte v systému knihovny. Přihlaste se prosím.");

        nameJLabel = new JLabel("Jméno: ");
        nameJLabel.setPreferredSize(labelsDim);

        nameJTextField = new JTextField();
        nameJTextField.setPreferredSize(fieldsDim);
        nameJTextField.addActionListener(new LoginActionListener());

        passwordJLabel = new JLabel("Heslo: ");
        passwordJLabel.setPreferredSize(labelsDim);

        passwordJPasswordField = new JPasswordField();
        passwordJPasswordField.setPreferredSize(fieldsDim);
        passwordJPasswordField.addActionListener(new LoginActionListener());

        okJButton = new JButton("Přihlásit se");
        okJButton.addActionListener(new LoginActionListener());

        namePanel.add(nameJLabel);
        namePanel.add(nameJTextField);

        passwordPanel.add(passwordJLabel);
        passwordPanel.add(passwordJPasswordField);

        buttonPanel.add(okJButton);

        vBox.add(informationJLabel);
        vBox.add(namePanel);
        vBox.add(passwordPanel);
        vBox.add(buttonPanel);
        add(vBox);

        pack();
        setLocationRelativeTo(null);
    }

    private class LoginActionListener implements ActionListener {

        String name;
        String password;

        @Override
        public void actionPerformed(ActionEvent e) {
            name = nameJTextField.getText();
            password = passwordJPasswordField.getText();
            if (name.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(LoginJFrame.this, "Vyplňte prosím všechny údaje", "Upozornění", JOptionPane.WARNING_MESSAGE);
            } else {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                Uzivatel user = null;
                try {
                    user = dbManager.login(name, password);
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                    JOptionPane.showMessageDialog(LoginJFrame.this, "Váš systém není podporován. Kontaktujte podporu.",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    exit(1);
                }
                if (user != null) {
                    MainJFrame jm = new MainJFrame(user);
                    jm.setVisible(true);
                    dispose();
                } else {
                    passwordJPasswordField.setText("");
                    JOptionPane.showMessageDialog(LoginJFrame.this, "Neexistující uživatel nebo špatné heslo", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
}
