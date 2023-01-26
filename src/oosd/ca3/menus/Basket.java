package oosd.ca3.menus;

import oosd.ca3.util.TableHandler;

import javax.swing.*;
import java.awt.*;

public class Basket extends JPanel {

    /*
    *
    * TODO:
    *  - Decrement stock when an item is purchased.
    *  - Add a button to remove an item from the basket.
    *  - Add a button to purchase all items in the basket.
    *
    */

    TableHandler tableHandler = new TableHandler();

    public Basket(final JButton btnProducts, final JButton btnAdmin, final JButton btnLogout) {

        setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnLogout);

        add(buttonPanel, c);
        c.gridwidth = 1;

        c.gridy++;

        final JScrollPane basketScrollPane = new JScrollPane(tableHandler.getBasketTable());

        c.gridheight = 4;
        add(basketScrollPane, c);
        c.gridheight =1;

        c.anchor = GridBagConstraints.NORTH;
        c.gridy = 1;
        c.gridx = 1;

        final JLabel totalLabel = new JLabel("Total: ");
        final JTextField totalTextField = new JTextField(10);
        final JButton removeSelected = new JButton("Remove selected");
        final JButton purchase = new JButton("Purchase");

        add(totalLabel, c);
        c.gridx++;
        add(totalTextField, c);

        c.gridx = 1;
        c.gridy++;
        c.gridwidth = 2;

        add(removeSelected, c);

        c.gridy++;

        add(purchase, c);

    }

}
