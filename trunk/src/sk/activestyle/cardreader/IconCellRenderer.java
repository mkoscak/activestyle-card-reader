package sk.activestyle.cardreader;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class IconCellRenderer implements TableCellRenderer {

    private IconComponent component;

    public IconCellRenderer() {
        component = new IconComponent();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        component.setIcon((ImageIcon) value);

        return component;
    }
}