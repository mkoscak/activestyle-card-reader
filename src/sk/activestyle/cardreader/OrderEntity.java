package sk.activestyle.cardreader;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class OrderEntity extends BaseEntity {

	public ArrayList<OrderItemEntity> items = new ArrayList<OrderItemEntity>();
	
	@Override
	public ImageIcon GetImage() {
		switch (getState())
		{
		case Equipped:
			return ImageHelper.GetImage(ImageHelper.ImgEquipped);
			
		case NonEquipped:
			return ImageHelper.GetImage(ImageHelper.ImgNonEquipped);
		}
		
		return null;
	}

	@Override
	public void SetState(BaseState state) {
		super.SetState(state);
	}
}
