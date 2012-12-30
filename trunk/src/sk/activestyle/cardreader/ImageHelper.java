package sk.activestyle.cardreader;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class ImageHelper {
	
	public static int ImgEquipped = 0;
	public static int ImgNonEquipped = 1;
	public static int ImgStateEquipped = 2;
	public static int ImgStateNonEquipped = 3;
	public static int ImgStateUnknown = 4;
	public static int ImgAppIco = 5;
	
	static ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
	
	static
	{
		try
		{
			ClassLoader loader = ImageHelper.class.getClassLoader();
			
			images.add(new ImageIcon(loader.getResource("res/eq16.png")));
			images.add(new ImageIcon(loader.getResource("res/non16.png")));
			images.add(new ImageIcon(loader.getResource("res/ok-icon.png")));
			images.add(new ImageIcon(loader.getResource("res/err-icon.png")));
			images.add(new ImageIcon(loader.getResource("res/help-icon.png")));
			images.add(new ImageIcon(loader.getResource("res/ikona.png")));
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	/**
	 * 
	 * @param kind - typ obrazka podla konstant definovanych v tejto triede
	 * @return obrazok - ikona
	 */
	public static ImageIcon GetImage(int kind)
	{
		if (images == null || kind >= images.size() || kind < 0)
			return null;
		
		return images.get(kind);
	}
}
