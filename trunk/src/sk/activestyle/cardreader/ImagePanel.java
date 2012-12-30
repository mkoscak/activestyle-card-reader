package sk.activestyle.cardreader;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1850657336849891063L;

	private IconComponent canvas;
	
	public IconComponent getCanvas() {
		return canvas;
	}

	public void setCanvas(IconComponent canvas) {
		this.canvas = canvas;
	}
	
	public void SetImage(ImageIcon icon) {
		if (canvas == null)
			return;
		
		canvas.setIcon(icon);
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (canvas != null)
        	canvas.update(g);
    }
}
