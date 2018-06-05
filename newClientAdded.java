import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class newClientAdded implements ActionListener {
	// refers to all variables for chat Window
	private JFrame chatWindowFrame;
	private final int chatWindowHEIGHT = 500;
	private final int chatWindowWIDTH =  610;
	private JButton send;
	private JPanel jpanel;
	private JPanel jPanelSouth;
	private JTextArea chatMessages;
	private JTextArea sendMessages;

	private DatagramPacket outPacket;
	// clientInformation
	private InetAddress clientAdress;
	private int clientPort;
	private boolean messageSent = false;

	
	public newClientAdded(InetAddress clientAddress, int portNo){

		this.clientAdress = clientAddress;
		this.clientPort = portNo;
		
		//initalize the chat window
		createJPanel();	
	}
	// intializes all fields and variables for new ChatWindow
	public void createJPanel(){
		
		chatWindowFrame = new JFrame();
		chatWindowFrame.setSize(chatWindowWIDTH, chatWindowHEIGHT);
		chatWindowFrame.setTitle("Connected with: " + clientAdress + " " + clientPort);
		
		jpanel = new JPanel(new BorderLayout());
		chatMessages = new JTextArea(100, 100);
		chatMessages.setEditable(false);
		
		jPanelSouth = new JPanel(new FlowLayout());
		sendMessages = new JTextArea(5,40);
	
		send = new JButton("Send");
		send.addActionListener(this);
		send.setPreferredSize(new Dimension(80, 80));
		
		jpanel.add(chatMessages,BorderLayout.CENTER);
		jpanel.add(jPanelSouth, BorderLayout.SOUTH);
		
		jPanelSouth.add(sendMessages);
		jPanelSouth.add(send);
		chatWindowFrame.add(jpanel);
		
		chatWindowFrame.setLocationRelativeTo(null);
		chatWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindowFrame.setVisible(true);

	}
	
	// Method appends all incoming messages to the chatText Area 
	public void setText(String str){
		
		chatMessages.append(str + "recieved from: " +clientAdress+ "\n");
		chatMessages.setWrapStyleWord(true);
		chatMessages.setForeground(Color.BLUE);	
		chatWindowFrame.setVisible(true);
		
	}

public void outSocket(String message) {

byte[] outBuffer = new byte[100];

	if (messageSent) {

		if (message != null){
			
			outBuffer = message.getBytes();
		
		}
			
	try {
		
		 outPacket = new DatagramPacket(outBuffer, message.length(),clientAdress, clientPort);
		 Server.inSocket.send(outPacket);
//		 System.out.println(outPacket.getData().toString());

	} catch (Exception e) {
		
		e.printStackTrace();
		System.exit(-1);
		
	}

	}
	
   messageSent = false;

}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(send)){
			
			String sendingMessage = sendMessages.getText();
			String appendMessage = "Rehan: " + sendingMessage;
			outSocket(appendMessage);
			chatMessages.append(appendMessage + "\n");
			chatMessages.setForeground(Color.BLACK);
			sendMessages.setText("");
			messageSent = true;
			
		}
		
	}
}