package sk.activestyle.cardreader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class IconComponent extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 8519335936997621021L;
    ImageIcon icon;

    public IconComponent() {
        this.setBackground(new Color(255, 255, 240));
    }

    public IconComponent(ImageIcon value) {
        this();
        icon = value;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (icon == null)
            return;
        
        int posx = Math.abs((int)g.getClipBounds().getWidth() - icon.getIconWidth()) / 2;
        int posy = Math.abs((int)g.getClipBounds().getHeight() - icon.getIconHeight()) / 2;

        icon.paintIcon(this, g, posx, posy);
    }
}