package sk.activestyle.cardreader;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class StringIconTableModel<T extends BaseEntity> extends AbstractTableModel {
	
	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
	}

	private static final long serialVersionUID = -1807578052065592574L;
	public ArrayList<? extends BaseEntity> data = new ArrayList<T>();
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column)
		{
		case 0:
			return "Store Nr.";
			
		case 1:
			return "Item Nr.";
			
		case 2:
			return "State";
		}
		
		return "unknown";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row >= getRowCount())
			return null;
		
		switch (col)
		{
		case 0:
			return data.get(row).StoreNr;
			
		case 1:
			return data.get(row).Id;
			
		case 2:
			return data.get(row).GetImage();
		}
		
		return null;
	}
}
