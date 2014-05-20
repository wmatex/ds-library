/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package knihovna.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
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
    private int pageno = 0;
    private String[] columnNames;
    private ResultFetcher fetcher;
    private Dimension windowDimension;
    private JPanel panel = null;

    public TableDialog(Uzivatel user, String title, ResultFetcher fetcher) {
        super((JDialog)null);
        this.user = user;
        this.fetcher = fetcher;
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

        final JButton prevButton = new JButton("Předchozí");
        final JButton nextButton = new JButton("Další");
        prevButton.setEnabled(false);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pageno--;
                createPanel(fetcher.getResults(pageno));
                if (pageno < 1) {
                    prevButton.setEnabled(false);
                }
                nextButton.setEnabled(true);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object[][] resultList = fetcher.getResults(pageno+1);
                if (resultList != null) {
                    pageno++;
                    prevButton.setEnabled(true);
                    if (fetcher.getResults(pageno+1) == null) {
                        nextButton.setEnabled(false);
                    }
                    createPanel(resultList);
                }
            }
        });
        if (fetcher.getResults(1) == null) {
            nextButton.setEnabled(false);
        }
        JPanel panel = new JPanel();
        panel.add(prevButton);
        panel.add(nextButton);
        panel.add(closeButton);
        add(panel, BorderLayout.SOUTH);
    }

    private void createPanel(Object[][] data) {
        if (data.length == 0) return;
        GridBagLayout layout = new GridBagLayout();
        if (panel != null) {
            this.remove(panel);
        }
        panel = new JPanel(layout);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);
        c.gridy = 0;
        int x = 0;
        for (String name: columnNames) {
            c.gridx = x++;
            panel.add(new JLabel("<html><i>"+name+"</i></html>"), c);
        }
        
        Font font = new Font("Serif", Font.PLAIN, 12);
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
                } else if (o != null) {
                    JLabel l = new JLabel(o.toString());
                    l.setFont(font);
                    panel.add(l, c);
                }
                x++;
            }
            y++;
        }

        
        layout.layoutContainer(panel);
        double[][] weights = layout.getLayoutWeights();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = 1;
            }
        }
        layout.columnWeights = weights[0];
        layout.rowWeights = weights[1];
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel, BorderLayout.CENTER);
        revalidate();
    }
    
    public void setInitial(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        createPanel(data);
        pack();
        setLocationRelativeTo(null);
    }

    public ResultFetcher getFetcher() {
        return fetcher;
    }

    public interface ResultFetcher {
        Object[][] getResults(int pageno);
    }
}
