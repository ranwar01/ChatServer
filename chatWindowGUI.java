import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.border.EtchedBorder;

public class chatWindowGUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
    private JFrame addingNewHost;
    public static InetAddress myAddress = null;
    public static DateTimeFormatter dtf;
    public static LocalDateTime now;

	private final int WIDTH = 650;
	private final int HEIGHT = 650;
	
	private JButton add;
	private JButton connectNewHost;
	private JLabel hostName;
	private JLabel hostIpAddress;
	private JLabel portNo;
	private JRadioButton local;
	private JTextField hostNameTextField;
	private JTextField hostIpAddressTextField;
	private JTextField portNoTextField;
	
	private JPanel infoPanel;
	private JPanel chatPanel;
	private JPanel centerView;

	private boolean filledIn;

	private Server server;
	private newClientAdded newClient;
	
	public chatWindowGUI(){
		
		this.setSize(WIDTH,HEIGHT);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		server = new Server();
		
		try {
			
			myAddress = InetAddress.getLocalHost();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		this.setTitle("Rehan " + "My IP address is: " + (myAddress) +" " + "Port is: " + Server.PORTLISTENING);
		
		dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"); 
		now = LocalDateTime.now();
		JPanelLayout();

		this.add(infoPanel, BorderLayout.PAGE_START);
		this.add(chatPanel, BorderLayout.EAST);		
		this.add(centerView,BorderLayout.CENTER);
		
		enterNewConnection();
		this.setVisible(true);	
		
	}
	private void JPanelLayout(){
		
	    infoPanel = new JPanel();
		infoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		JLabel time = new JLabel();
		JLabel myIp = new JLabel();
		
		myIp.setText(myAddress.toString());
		time.setText(dtf.format(now).toString());
		
		infoPanel.add(time);
		infoPanel.add(myIp);
	
		chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));
		chatPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		
		add = new JButton("Add New Connection");
		add.addActionListener(this);
		chatPanel.add(add);
		
		centerView = new JPanel();
		centerView.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	    Object columnNames[] = { "Name", "IP Adress", "PortNo" };
		Object[][] rowData  =  new Object[6][6];
	    JTable table = new JTable(rowData, columnNames);
	    centerView.add(new JScrollPane(table));	
		table.setBackground(Color.lightGray);
			
	}
	
	public void enterNewConnection(){
	
		addingNewHost = new JFrame("IP Adress: " + (myAddress));
		addingNewHost.setSize(350, 350);
		connectNewHost = new JButton("Connect");
		local = new JRadioButton("Local Host");
		
		hostName = new JLabel("Enter Name: ");
		hostIpAddress = new JLabel("Enter IPAdress: ");
		portNo = new JLabel("Enter Host PortNo: ");
		
		JPanel z = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		hostNameTextField = new JTextField(15);
		hostIpAddressTextField = new JTextField(15);
		portNoTextField = new JTextField(15);

		c.gridx = 0;
		c.gridy = 0;
		z.add(hostName);
		
		c.gridx = 1;
		c.gridy = 0;
		z.add(hostNameTextField, c);
		
		c.gridx =0;
		c.gridy =1;
		z.add(hostIpAddress,c);
		c.gridx = 1;
		c.gridy = 1;
		z.add(hostIpAddressTextField,c);
		
		c.gridx = 0;
		c.gridy = 2;
		z.add(portNo,c);
		c.gridx = 1;
		c.gridy = 2;
		z.add(portNoTextField,c);
		
		c.gridx = 0;
		c.gridy = 4;
		local.addActionListener(this);
		z.add(local,c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = 2;
		z.add(connectNewHost,c);
		connectNewHost.addActionListener(this);

		addingNewHost.add(z);
		addingNewHost.setVisible(false);
					
	}
	private void checkTextFieldForVerification() {

		InetAddress inetAddress1 = null;
		
		try {
			
			inetAddress1 = InetAddress.getByName(hostIpAddressTextField.getText().toString());
		
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		String ipAddress = inetAddress1.toString();
	
		if ((inetAddress1 != null)&&(!Server.clients.containsKey(ipAddress))) {
			
			newClient = new newClientAdded(inetAddress1, Integer.parseInt(portNoTextField.getText()));
			
		// add the new client to HashMap
			Server.clients.put(ipAddress, newClient);
			setClear(); // clear all text fields;
			filledIn = false;
			local.setSelected(false);
			addingNewHost.dispose();
			addingNewHost.setVisible(false);
		
		}
		
		else {
			
			JOptionPane.showMessageDialog(null, "This Client is connected already: ");
			
		}
	}
	
	private void setClear(){
		
		hostNameTextField.setText("");
		hostIpAddressTextField.setText("");
		portNoTextField.setText("");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(add)){

			addingNewHost.setVisible(true);
	
		}
		
		else if (e.getSource().equals(connectNewHost)){
			
			checkTextFieldForVerification();
			
		}
		
		else if (e.getSource().equals(local)){
			
			if (!filledIn){
				
			hostNameTextField.setText("Local");
			hostIpAddressTextField.setText(myAddress.toString());
			portNoTextField.setText("31000");
			filledIn = true;
			
			}
			
			else {
				
				hostNameTextField.setText("");
				hostIpAddressTextField.setText("");
				portNoTextField.setText("");
				filledIn = false;
				
			}
			
		}
		
	}


}
