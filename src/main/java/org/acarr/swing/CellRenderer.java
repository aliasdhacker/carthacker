package org.acarr.swing;

import org.acarr.CartHacker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CellRenderer extends DefaultTableCellRenderer {

    Color green = Color.GREEN.darker();
    Color red = Color.RED.darker();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (column == 1) {
            if ((String) value == ("OK")) {
                c.setBackground(green);
                c.setForeground(Color.BLACK);
                c.setFont(new Font(null, Font.BOLD, CartHacker.DEFAULT_FONT_SIZE));
            } else {
                c.setBackground(red);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Default", Font.BOLD, CartHacker.DEFAULT_FONT_SIZE));
            }
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(Color.BLACK);
            c.setFont(new Font(null, Font.BOLD, CartHacker.DEFAULT_FONT_SIZE));
        }

        return c;
    }

}
