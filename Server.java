import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Server extends Thread {
	
	public final static int PORTLISTENING = 31000;
	public static InetAddress myAddress;
	public static DatagramSocket inSocket;
	private DatagramPacket inPacket;
	
	private newClientAdded newClient;
	private InetAddress clientAddress;
	private int clientPort;
	public static HashMap < String, newClientAdded> clients; // stores list of all clients

	private byte [] inBuffer;
	
	public Server(){
		
		clients = new HashMap<String, newClientAdded>();
		
		try {
			myAddress = InetAddress.getLocalHost();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.exit(-1);
		}
		
		try {
			
			inSocket = new DatagramSocket(PORTLISTENING, myAddress);
			
		} catch (SocketException e) {
	
			e.printStackTrace();
		}
		this.start();  
	
	}
	public void run() {
		
		inBuffer = new byte[100];
		inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			
		while(true) {
	
			for ( int i = 0 ; i < inBuffer.length ; i++ ) {
				
				inBuffer[i] = ' ';
				
			}
			System.out.println("Waiting for input...");
			
			try {
				
				inSocket.receive(inPacket);
				clientAddress = inPacket.getAddress();
	            clientPort = inPacket.getPort();  
	            System.out.println("recieved from " + " " + clientAddress + "from clientPortNO: " + clientPort);
	            
	            String id = clientAddress.toString();
	      
	            if (!clients.containsKey(id)){

	            	 newClient = new newClientAdded(clientAddress, clientPort);
	            	 clients.put(id, newClient);
//	            	System.out.println("ADDING A NEW NEW CLIENT WITH IP ADDRESS OF: " + clientAddress);
	            }
	            
	            newClientAdded client = clients.get(id);
	            client.setText(new String(inPacket.getData()));

			} catch (IOException e) {
	
				e.printStackTrace();
			}
 	
		}
		
	}

}
