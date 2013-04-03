package sk.activestyle.cardreader;

import javax.swing.ImageIcon;

public class OrderItemEntity extends BaseEntity {

	public String ItemName;
	public String ProdName;
	public String Size;
	public OrderEntity parent;
	
	@Override
	public ImageIcon GetImage() {
		if (parent == null)
			return null;
		
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
		
		// ak ma objednavka vsetky vybavene produkty, tak je tiez vybavena
		if (getState() == BaseState.Equipped && parent != null && parent.getState() != BaseState.Equipped)
		{
			for (int i = 0; i < parent.items.size(); i++) {
				if (parent.items.get(i).getState() != BaseState.Equipped) {
					return;					
				}
			}
			
			parent.SetState(BaseState.Equipped);
		}
	}
}
