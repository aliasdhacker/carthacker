package org.acarr.swing;

import org.acarr.CartHacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class TableMaker {

    private static JTable jTable;

    private static final Logger logger = LoggerFactory.getLogger(TableMaker.class);

    private static Object[][] data;

    public TableMaker() {
        jTable = new JTable();
    }

    public TableMaker(JTable jTable) {
        this.jTable = jTable;
    }

    public JTable getTable() {
        jTable = new JTable();
        jTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{
                        "Website", "Status", "rawouput"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

        });
        jTable.setDefaultRenderer(String.class, new CellRenderer());
        jTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = jTable.columnAtPoint(e.getPoint());
                if (col == 0) {
                    URL value = (URL) jTable.getValueAt(jTable.rowAtPoint(e.getPoint()), 0);
                    logger.info("Mouse clicked on URL: " + value.toString());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(jTable.getValueAt(jTable.rowAtPoint(e.getPoint()), jTable.columnAtPoint(e.getPoint())).toString()), null);
                }


                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return jTable;
    }

    public static void setStatus(int index, String status) {
        if (!CartHacker.headlessMode) {
            ((Frame) CartHacker.frame).getjTable1().setValueAt(status, index, 1);
            TableCellEditor e = ((Frame) CartHacker.frame).getjTable1().getCellEditor(index, 1);
        }
        data[index][1] = status;

    }

    public static void setRaw(int arrayIndex, String s) {
        if (!CartHacker.headlessMode) {
            ((Frame) CartHacker.frame).getjTable1().setValueAt(s, arrayIndex, 2);
        }
        data[arrayIndex][2] = s;
    }

    public static Object[][] getData() {
        return data;
    }

    public static void setData(Object[][] data) {
        TableMaker.data = data;
    }

    public void setWebsite(int arrayIndex, String s) {
        if (!CartHacker.headlessMode) {
            ((Frame) CartHacker.frame).getjTable1().setValueAt(s, arrayIndex, 0);
        }
        data[arrayIndex][2] = s;
    }
}
