package sk.activestyle.cardreader;

import javax.swing.ImageIcon;

public abstract class BaseEntity {

	public String Id;
	public String StoreNr;
	private BaseState State;
		
	public BaseState getState() {
		return State;
	}
	
	public void SetState(BaseState state) {
		State = state;
	}
	
	public abstract ImageIcon GetImage();
}
