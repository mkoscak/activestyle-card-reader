package sk.activestyle.cardreader;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CardReaderFrm implements ActionListener, ListSelectionListener {

	private JFrame frmActivestyleCardreader;
	private JTable tableOrders;
	private JTable tableItems;
	private JTextField txtFileName;
	private ImagePanel panelState;
	private JButton btnSetEquipped;
	private JCheckBox checkAutoEquip;
	
	private File inputFile;
	private ArrayList<OrderEntity> allOrders;
	
	String eol = System.getProperty("line.separator");
	private JTextField txtItemCode;
	private JTextField txtStoreNr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardReaderFrm window = new CardReaderFrm();
					window.frmActivestyleCardreader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CardReaderFrm() {
		initialize();
		
		panelState.setCanvas(new IconComponent());
		panelState.SetImage(ImageHelper.GetImage(ImageHelper.ImgStateUnknown));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmActivestyleCardreader = new JFrame();
		frmActivestyleCardreader.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						txtItemCode.requestFocus();
					}
				});
			}
		});
		frmActivestyleCardreader.setTitle("ActiveStyle (c) CardReader");
		frmActivestyleCardreader.setBounds(100, 100, 824, 466);
		frmActivestyleCardreader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmActivestyleCardreader.getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		frmActivestyleCardreader.setIconImage(ImageHelper.GetImage(ImageHelper.ImgAppIco).getImage());
		
		JSplitPane splitMain = new JSplitPane();
		splitMain.setDividerLocation((int)Math.round(frmActivestyleCardreader.getBounds().width*0.25));
		frmActivestyleCardreader.getContentPane().add(splitMain);
		
		JSplitPane splitOrdersItems = new JSplitPane();
		splitOrdersItems.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitOrdersItems.setDividerLocation((int)Math.round(frmActivestyleCardreader.getBounds().height*0.60));
		splitMain.setLeftComponent(splitOrdersItems);
		
		JScrollPane scrollPane = new JScrollPane();
		splitOrdersItems.setLeftComponent(scrollPane);
		
		tableOrders = new JTable();
		tableOrders.setToolTipText("Orders");
		tableOrders.setShowGrid(false);
		tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableOrders.setFillsViewportHeight(true);
		tableOrders.setModel(new StringIconTableModel<OrderEntity>());
		tableOrders.getColumnModel().getColumn(0).setPreferredWidth(60);
		tableOrders.getColumnModel().getColumn(0).setMinWidth(60);
		tableOrders.getColumnModel().getColumn(0).setMaxWidth(60);
		tableOrders.getColumnModel().getColumn(2).setPreferredWidth(40);
		tableOrders.getColumnModel().getColumn(2).setMinWidth(40);
		tableOrders.getColumnModel().getColumn(2).setMaxWidth(40);
		tableOrders.getColumnModel().getColumn(2).setCellRenderer(new IconCellRenderer());
		tableOrders.getSelectionModel().addListSelectionListener(this);
		scrollPane.setViewportView(tableOrders);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitOrdersItems.setRightComponent(scrollPane_1);
		
		tableItems = new JTable();
		tableItems.setToolTipText("Order items");
		tableItems.setShowGrid(false);
		tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableItems.setFillsViewportHeight(true);
		tableItems.setModel(new StringIconTableModel<OrderItemEntity>());
		tableItems.getColumnModel().getColumn(0).setPreferredWidth(60);
		tableItems.getColumnModel().getColumn(0).setMinWidth(60);
		tableItems.getColumnModel().getColumn(0).setMaxWidth(60);
		tableItems.getColumnModel().getColumn(2).setPreferredWidth(40);
		tableItems.getColumnModel().getColumn(2).setMinWidth(40);
		tableItems.getColumnModel().getColumn(2).setMaxWidth(40);
		tableOrders.getColumnModel().getColumn(2).setCellRenderer(new IconCellRenderer());
		scrollPane_1.setViewportView(tableItems);
		
		JPanel panelMain = new JPanel();
		panelMain.setToolTipText("");
		panelMain.setBackground(Color.WHITE);
		splitMain.setRightComponent(panelMain);
		panelMain.setLayout(null);
		
		JLabel lblInputFile = new JLabel("Input file");
		lblInputFile.setForeground(new Color(102, 0, 0));
		lblInputFile.setToolTipText("Select input CSV file");
		lblInputFile.setBounds(10, 11, 72, 14);
		panelMain.add(lblInputFile);
		
		txtFileName = new JTextField();
		txtFileName.setEditable(false);
		txtFileName.setBounds(90, 8, 380, 20);
		panelMain.add(txtFileName);
		txtFileName.setColumns(10);
		
		JButton btnChoose = new JButton("Choose");
		btnChoose.addActionListener(this);
		btnChoose.setBounds(483, 6, 89, 23);
		panelMain.add(btnChoose);
		
		txtItemCode = new JTextField();
		txtItemCode.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				txtItemCode.selectAll();
			}
		});
		txtItemCode.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (checkAutoEquip.isSelected())
					ItemCodePerformed();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (checkAutoEquip.isSelected())
					ItemCodePerformed();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (checkAutoEquip.isSelected())
					ItemCodePerformed();
			}
		});
		txtItemCode.setForeground(Color.BLUE);
		txtItemCode.setFont(new Font("Tahoma", Font.BOLD, 30));
		txtItemCode.setHorizontalAlignment(SwingConstants.CENTER);
		txtItemCode.setBounds(90, 68, 259, 52);
		panelMain.add(txtItemCode);
		txtItemCode.setColumns(10);
		
		JLabel lblItemCode = new JLabel("Item code");
		lblItemCode.setForeground(new Color(102, 0, 0));
		lblItemCode.setBounds(10, 86, 72, 14);
		panelMain.add(lblItemCode);
		
		panelState = new ImagePanel();
		panelState.setToolTipText("Order state");
		panelState.setBackground(Color.WHITE);
		panelState.setBounds(359, 68, 64, 52);
		panelMain.add(panelState);
		
		btnSetEquipped = new JButton("Set item equipped");
		btnSetEquipped.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemCodePerformed();
			}
		});
		btnSetEquipped.setBounds(433, 78, 139, 31);
		panelMain.add(btnSetEquipped);
		
		checkAutoEquip = new JCheckBox("Automatic order item equippment");
		checkAutoEquip.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				btnSetEquipped.setEnabled(!checkAutoEquip.isSelected());
			}
		});
		checkAutoEquip.setSelected(true);
		checkAutoEquip.setForeground(new Color(102, 0, 0));
		checkAutoEquip.setBackground(Color.WHITE);
		checkAutoEquip.setBounds(90, 38, 259, 23);
		panelMain.add(checkAutoEquip);
		
		txtStoreNr = new JTextField();
		txtStoreNr.setEditable(false);
		txtStoreNr.setForeground(new Color(0, 128, 0));
		txtStoreNr.setFont(new Font("Tahoma", Font.BOLD, 70));
		txtStoreNr.setHorizontalAlignment(SwingConstants.CENTER);
		txtStoreNr.setBounds(90, 131, 159, 91);
		panelMain.add(txtStoreNr);
		txtStoreNr.setColumns(10);
		
		JLabel lblStoreNumber = new JLabel("Store Nr.");
		lblStoreNumber.setForeground(new Color(102, 0, 0));
		lblStoreNumber.setBounds(10, 170, 72, 14);
		panelMain.add(lblStoreNumber);
	}
	
	public void ItemCodePerformed()
	{
		ArrayList<OrderEntity> data = GetOrders();
		if (data == null)
			return;
		
		String code = txtItemCode.getText().trim();
		
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).items.size(); j++) {
				String actual = data.get(i).items.get(j).Id.trim();
				if (code.equals(actual))
				{
					tableOrders.setRowSelectionInterval(i, i);
					Rectangle r = tableOrders.getCellRect(i, 0, true);
					tableOrders.scrollRectToVisible(r);
					tableItems.setRowSelectionInterval(j, j);
					r = tableItems.getCellRect(j, 0, true);
					tableItems.scrollRectToVisible(r);
					
					data.get(i).items.get(j).SetState(BaseState.Equipped);
					
					txtStoreNr.setText(data.get(i).StoreNr);
					
					tableOrders.updateUI();
					tableItems.updateUI();
					
					if (data.get(i).getState() == BaseState.Equipped)
						panelState.SetImage(ImageHelper.GetImage(ImageHelper.ImgStateEquipped));
					else
						panelState.SetImage(ImageHelper.GetImage(ImageHelper.ImgStateNonEquipped));
					panelState.repaint();
					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							txtItemCode.selectAll();
						}
					});
									
					return;
				}
			}
		}
		
		txtStoreNr.setText("");
		
		panelState.SetImage(ImageHelper.GetImage(ImageHelper.ImgStateUnknown));
		panelState.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		
		if (source == "Choose")
		{
			JFileChooser jfc = new JFileChooser(".");
			jfc.setDialogTitle("Select input CSV file");
			if (jfc.showOpenDialog(frmActivestyleCardreader) == JFileChooser.OPEN_DIALOG)
			{
				inputFile = jfc.getSelectedFile();
				txtFileName.setText(inputFile.getPath());
				
				allOrders = DoImport();
				ShowOrders(allOrders);
				
				txtItemCode.setText("");
			}
		}
	}

	@SuppressWarnings("unchecked")
	ArrayList<OrderEntity> GetOrders()
	{
		StringIconTableModel<OrderEntity> model = GetOrdersTableModel();
		if (model == null)
			return null;
		
		return (ArrayList<OrderEntity>)model.data;
	}
	
	StringIconTableModel<OrderEntity> GetOrdersTableModel() {
		@SuppressWarnings("unchecked")
		StringIconTableModel<OrderEntity> model = (StringIconTableModel<OrderEntity>)tableOrders.getModel();
		
		return model;
	}
	
	StringIconTableModel<OrderItemEntity> GetOrderItemsTableModel() {
		@SuppressWarnings("unchecked")
		StringIconTableModel<OrderItemEntity> model = (StringIconTableModel<OrderItemEntity>)tableItems.getModel();
		
		return model;
	}
	
	private void ShowOrders(ArrayList<OrderEntity> orders) {
		if (!(tableOrders.getModel() instanceof StringIconTableModel))
			return;
		
		StringIconTableModel<OrderEntity> model = GetOrdersTableModel();
		model.data = orders;
		
		if (orders.size() > 0)
			tableOrders.setRowSelectionInterval(0, 0);
		
		model.fireTableDataChanged();
	}
	
	private void ShowOrderItems(ArrayList<OrderItemEntity> items) {
		if (!(tableItems.getModel() instanceof StringIconTableModel))
			return;
		
		StringIconTableModel<OrderItemEntity> model = GetOrderItemsTableModel();
		model.data = items;
		
		model.fireTableDataChanged();
	}

	// import CSV - 3 stlpce - cislo skladu, cislo fakt. a cislo produktu
	private ArrayList<OrderEntity> DoImport() {
		ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
		
		if (!ValidateFile(inputFile))
			return null;
		
		try {
			String content = readFileAsString(inputFile);
			String splitStr = "\r\n";
			if (content.contains(splitStr) == false)
					splitStr = "\n";
			
			String[] lines = content.split(splitStr);
			String lastCode = "";
			OrderEntity order = null;
			ArrayList<OrderItemEntity> orderItems = null;
			
			for (int i = 1; i < lines.length; i++) {	// header vynechavame, preto od 1
				String[] items = lines[i].split(";");
				
				if (!items[1].equals(lastCode))
				{
					if (orderItems != null)
						order.items = orderItems;
					if (order != null)
						orders.add(order);
					
					order = new OrderEntity();
					order.Id = items[1];
					order.StoreNr = items[0];
					order.SetState(BaseState.NonEquipped);
					
					orderItems = new ArrayList<OrderItemEntity>();
					
					lastCode = order.Id;
				}
				
				OrderItemEntity newItem = new OrderItemEntity();
				newItem.Id = items[2];
				newItem.ItemName = "";
				newItem.parent = order;
				newItem.SetState(BaseState.NonEquipped);
				newItem.StoreNr = items[0];
				
				orderItems.add(newItem);
			}
			if (orderItems != null)
				order.items = orderItems;
			if (order != null)
				orders.add(order);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frmActivestyleCardreader, "Exception occured during reading: "+e);
		}
		
		return orders;
	}

	private boolean ValidateFile(File inputFile) {
		if (inputFile == null)
		{
			JOptionPane.showMessageDialog(frmActivestyleCardreader, "Select input file first!");
			return false;
		}
		
		if (inputFile.getName().toLowerCase().endsWith("csv") == false)
		{
			JOptionPane.showMessageDialog(frmActivestyleCardreader, "Input file must be CSV file!");
			return false;
		}
			
		return true;
	}
	
	private String readFileAsString(File file) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = tableOrders.getSelectedRow();
		if (index == -1)
			return;
		
		StringIconTableModel<OrderEntity> model = GetOrdersTableModel();
		@SuppressWarnings("unchecked")
		ArrayList<OrderEntity> data = (ArrayList<OrderEntity>)model.data;
		
		ArrayList<OrderItemEntity> items = data.get(index).items;
		ShowOrderItems(items);
	}
}
