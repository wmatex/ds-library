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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import knihovna.DatabaseManager;
import knihovna.entity.Titul;
import knihovna.entity.Uzivatel;
import knihovna.entity.VwRezervace;
import knihovna.entity.VwVypujcka;
import knihovna.entity.Vytisk;
import knihovna.gui.TableDialog.ResultFetcher;

/**
 *
 * @author tomanlu2
 */
public class AdminJPanel extends JPanel {

    Uzivatel user;
    MainJFrame mainFrame;
    private JPanel menuJPanel;
    private JButton newBorrowingJButton;
    private JButton reservationsJButton;
    private JButton newUserJButton;
    private JButton returnButton;
    private JButton waitingReservations;
    private JButton borrowsButton;

    public AdminJPanel(Uzivatel user, MainJFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.user = user;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        Dimension buttonsDim = new Dimension(200, 25);

        menuJPanel = new JPanel();

        newBorrowingJButton = new JButton("Nová výpůjčka");
        reservationsJButton = new JButton("Správa rezervací");
        borrowsButton       = new JButton("Správa výpůjček");
        newUserJButton      = new JButton("Nový uživatel");
        returnButton        = new JButton("Vrátit knihu");
        waitingReservations = new JButton("Čekající rezervace");

        newBorrowingJButton.setPreferredSize(buttonsDim);
        reservationsJButton.setPreferredSize(buttonsDim);
        newUserJButton.setPreferredSize(buttonsDim);
        returnButton.setPreferredSize(buttonsDim);
        borrowsButton.setPreferredSize(buttonsDim);
        waitingReservations.setPreferredSize(buttonsDim);

        newBorrowingJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                if (email != null) {
                    Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        showBorrowDialog(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout(0, 10));

                JLabel label = new JLabel("Zadejte čárové kódy knih k vrácení");
                panel.add(label, BorderLayout.NORTH);
                final JTextField textField = new JTextField(13);
                panel.add(textField, BorderLayout.CENTER);

                final JLabel infoLabel = new JLabel(" ");
                panel.add(infoLabel, BorderLayout.SOUTH);

                JButton returnBookJButton = new JButton();
                returnBookJButton.setText("Vrátit knihu");
                returnBookJButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        DatabaseManager dbManager = DatabaseManager.getInstance();
                        String barcode = textField.getText();
                        Vytisk print = dbManager.getPrintByBarcode(barcode);
                        if (print == null) {
                            JOptionPane.showMessageDialog(null, "Výtisk s tímto čárovým kódem neexistuje",
                                "Chyba", JOptionPane.ERROR_MESSAGE);
                        } else {

                            VwVypujcka borrow = dbManager.getBorrowByVytisk(print);
                            if (borrow == null) {
                                JOptionPane.showMessageDialog(null, "Výtisk není pujčený",
                                    "Chyba", JOptionPane.ERROR_MESSAGE);

                            } else {
                                System.out.println(borrow.toString());
                                dbManager.returnBorrowing(borrow);
                                int delay = (int) Math.ceil((new Date().getTime() - borrow.getDatumVraceni().getTime()) / (1000 * 60 * 60 * 24));
                                if (delay > 0) {
                                    JOptionPane.showMessageDialog(null, 
                                        "Výtisk " + borrow.getNazev() + " byl vrácen pozdě\nSpozdné činí: " + delay * 5 + "Kč",
                                        "Spozdné", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Výtisk " + borrow.getNazev() + " byl vrácen v pořádku",
                                        "Vráceno", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    }
                });
                final JButton cancel = new JButton("Zrušit");
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        JOptionPane pane = getOptionPane((JComponent) ae.getSource());
                        pane.setValue(cancel);
                    }

                });
                JOptionPane.showOptionDialog(null,
                    panel,
                    "Vrácení",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{returnBookJButton, cancel},
                    returnBookJButton);
            }

        });

        newUserJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                JPanel namePanel = new JPanel();
                JPanel surnamePanel = new JPanel();
                JPanel emailPanel = new JPanel();
                Box vBox = Box.createVerticalBox();

                JLabel nameJLabel = new JLabel("Jméno: ");

                JTextField nameJTextField = new JTextField(10);

                namePanel.add(nameJLabel);
                namePanel.add(nameJTextField);

                JLabel surnameJLabel = new JLabel("Příjmení: ");

                JTextField surnameJTextField = new JTextField(10);

                surnamePanel.add(surnameJLabel);
                surnamePanel.add(surnameJTextField);

                JLabel emailJLabel = new JLabel("Email: ");

                JTextField emailJTextField = new JTextField(10);

                emailPanel.add(emailJLabel);
                emailPanel.add(emailJTextField);

                vBox.add(namePanel);
                vBox.add(surnamePanel);
                vBox.add(emailPanel);
                while (true) {
                    int result = JOptionPane.showConfirmDialog(null, vBox, "Nový uživatel", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.CANCEL_OPTION) {
                        break;
                    }
                    if ("".equals(nameJTextField.getText()) || "".equals(surnameJTextField.getText()) || "".equals(emailJTextField.getText())) {
                        JOptionPane.showMessageDialog(null, "Vyplňte všechny údaje", "Upozornění",
                            JOptionPane.WARNING_MESSAGE);
                    } else {
                        String password = newUser(nameJTextField.getText(), surnameJTextField.getText(), emailJTextField.getText());
                        if (password != null) {
                            JOptionPane.showMessageDialog(null, "Vytvořen uživatel '" + emailJTextField.getText() + "' s heslem: " + password, "Vytvořeno",
                                JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
            }
        });

        reservationsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                if (email != null) {
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    final Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        MainJFrame.showReservationDialog(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        borrowsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JOptionPane.showInputDialog(null,
                    "Zadejte email čtenáře", "Čtenář", JOptionPane.QUESTION_MESSAGE);
                if (email != null) {
                    DatabaseManager dbManager = DatabaseManager.getInstance();
                    final Uzivatel user = dbManager.getUserByEmail(email);
                    if (user != null) {
                        MainJFrame.showBorrowDialog(user);
                    } else {
                        JOptionPane.showMessageDialog(null, "Čtenář: '" + email + "' neexistuje", "Chyba",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        waitingReservations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ResultFetcher fetcher = new ResultFetcher() {
                    @Override
                    public Object[][] getResults(int pageno) {
                        DatabaseManager dbManager = DatabaseManager.getInstance();
                        Collection<VwRezervace> reservations = dbManager.getWaitingRezervations(pageno);
                        if (reservations.isEmpty()) return null;

                        int i = 0;
                        Object[][] data = new Object[reservations.size()][];
                        for (final VwRezervace row: reservations) {
                            data[i] = new Object[3];
                            String name = row.getKrestniJmeno() + " " + row.getPrijmeni();
                            data[i][0] = name;
                            data[i][1] = row.getNazev();
                            final JButton button = new JButton("Splnit rezervaci");
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    DatabaseManager dbManager = DatabaseManager.getInstance();
                                    dbManager.fullfillReservation(row.getIdTitul());
                                    JOptionPane.showMessageDialog(null, 
                                        "Rezervace byla splněna", "", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                    button.setEnabled(false);
                                }
                            });
                            data[i][2] = button;
                            i++;
                        }
                        return data;
                    }
                };
                Object[][] initial = fetcher.getResults(0);
                if (initial == null) {
                    JOptionPane.showMessageDialog(null, "Žádné čekající rezervace", 
                        "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String[] columnNames = {
                        "Čtenář",
                        "Titul",
                        ""
                    };
                    TableDialog dialog = new TableDialog(null, "Rezervace čekající na vyřízení", fetcher);
                    dialog.setInitial(columnNames, initial);
                    dialog.setVisible(true);
                }
            }
        });
        menuJPanel.add(newBorrowingJButton);
        menuJPanel.add(reservationsJButton);
        menuJPanel.add(borrowsButton);
        menuJPanel.add(newUserJButton);
        menuJPanel.add(returnButton);
        menuJPanel.add(waitingReservations);

        add(menuJPanel, BorderLayout.CENTER);
    }

    private String newUser(String name, String surname, String email) {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        String password = null;
        try {
            password = dbManager.createUser(name, surname, email);
        } catch (EntityExistsException ex) {
            JOptionPane.showMessageDialog(null, "Email je již používán", "Chyba", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return password;

    }

    private void showBorrowDialog(final Uzivatel user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));

        JLabel label = new JLabel("<html>Zadáváte výpůjčky pro uživatele <i>"
            + user.getKrestniJmeno() + " " + user.getPrijmeni()
            + "</i><br>Zadejte čárový kód výpůjčky</html>");
        panel.add(label, BorderLayout.NORTH);
        final JTextField textField = new JTextField(13);
        panel.add(textField, BorderLayout.CENTER);
        JButton loadButton = new JButton();
        loadButton.setText("Načíst kód");
        panel.add(loadButton, BorderLayout.EAST);
        final JLabel infoLabel = new JLabel(" ");
        panel.add(infoLabel, BorderLayout.SOUTH);

        JButton create = new JButton();
        create.setText("Vytvořit výpůjčku");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                String barcode = textField.getText();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    JOptionPane.showMessageDialog(null, "Výtisk s tímto čárovým kódem neexistuje",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        dbManager.createBorrowing(print, user);
                    } catch (EntityExistsException ex) {
                        JOptionPane.showMessageDialog(null, "Tento výtisk je již půjčený",
                            "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Vytvořena výpůjčka pro titul " + t.getNazev());
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String barcode = textField.getText();
                DatabaseManager dbManager = DatabaseManager.getInstance();
                Vytisk print = dbManager.getPrintByBarcode(barcode);
                if (print == null) {
                    infoLabel.setText("Výtisk s tímto čárovým kódem neexistuje");
                    textField.selectAll();
                } else {
                    Titul t = print.getIdTitul();
                    infoLabel.setText("Titul: " + t.getNazev());
                }
            }

        });
        final JButton cancel = new JButton("Zrušit");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane pane = getOptionPane((JComponent) ae.getSource());
                pane.setValue(cancel);
            }

        });
        JOptionPane.showOptionDialog(null,
            panel,
            "Výpůjčka",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{create, cancel},
            create);
    }

    private JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane = null;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent) parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
}
