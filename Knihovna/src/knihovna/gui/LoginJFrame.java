/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knihovna.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import knihovna.DatabaseManager;
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
        super("Pøihlášení");
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
        informationJLabel.setText("Vítejte v systému knihovny. Pøihlaste se prosím.");

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

        okJButton = new JButton("Pøihlásit se");
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
                JOptionPane.showMessageDialog(LoginJFrame.this, "Vyplòte prosím všechny údaje", "Upozornìní", JOptionPane.WARNING_MESSAGE);
            } else {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                Uzivatel user = null;
                try {
                    user = dbManager.login(name, password);
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                    JOptionPane.showMessageDialog(LoginJFrame.this, "Váš systém není podporován. Kontaktujte podporu.",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    System.exit(1);
                }
                if (user != null) {
                    MainJFrame jm = new MainJFrame(user);
                    jm.setVisible(true);
                    dispose();
                } else {
                    passwordJPasswordField.setText("");
                    JOptionPane.showMessageDialog(LoginJFrame.this, "Neexistující uživatel nebo neplatné heslo", "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
}
