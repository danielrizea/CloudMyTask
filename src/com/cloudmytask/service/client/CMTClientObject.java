package com.cloudmytask.service.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.utils.DataUtils;

public class CMTClientObject implements CMTClientPrivateInterface, CMTClientPublicInterface {


	public Object sendRequest(Request request, String serverIP, int serverPort,
			int clientPort) {
		
		//decide on the communication method used
		
		if(GlobalConfig.CommunicationType == GlobalConfig.TCP)
			return sendTCPRequest(request, serverIP, serverPort);
		
		if(GlobalConfig.CommunicationType == GlobalConfig.UDP)
			return sendUDPRequest(request, serverIP, serverPort, clientPort);
		
		if(GlobalConfig.CommunicationType == GlobalConfig.NIOTCP)
			return sendNIOTCPRequest(request, serverIP, serverPort);
			
		return null;
	}

	public Object sendTCPRequest(Request request, String serverIP, int serverPort) {

		Socket clientSocket=null;
		Request response = null;
		
		try{
			//DatagramSocket socket = new DatagramSocket(this.clientPort);
			clientSocket = new Socket(serverIP, serverPort);
			
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(request);
			//oos.close();
			
		}catch(Exception e){
			System.err.println("[CMTClientObject] error in sending TCP Request : " +  e.getMessage());
		}
		
		if(clientSocket != null)
			System.out.println("Socket " + clientSocket.getLocalPort());
		
		 try {
			  ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
	          //receive response request object
	          response = (Request) in.readObject();
	          System.out.println(response);
	            
	        } catch (Exception e) {
	            System.err.println("[CMTClientObject get response]-" + this.hashCode() + " exception: " + e.getMessage());
	            e.printStackTrace();
	        }
		return response;
		
	}


	public Object sendUDPRequest(Request request, String serverIP,
			int serverPort, int clientPort) {
		
		
		DatagramSocket socket = null;
		
		Request response = null;
		
		try{
			
			socket = new DatagramSocket(clientPort);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
				
			oos.writeObject(request);
			byte[] outputBuf = baos.toByteArray();
			
			InetAddress serverAddr = InetAddress.getByName(serverIP);
			DatagramPacket packet = new DatagramPacket(outputBuf, 0, outputBuf.length, serverAddr, serverPort);
			socket.send(packet);
			oos.close();
			baos.close();
		}
		catch(Exception e){			
			System.err.println("[CMTClientObject] error in sending UDP Request : " + e.getMessage());
		}
		
		
		// Asteapta raspunsul.
		byte[] buf = new byte[16384];
				
				try {
					DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
					socket.receive(packet);
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
					response = (Request) ois.readObject();
					System.out.println("[CMTClientObject] Am primit rezultat de la CacheService: " + request.requestID);
					ois.close();

				} catch (Exception e) {
					System.err.println("[CMTClientObject] Eroare la primirea raspunsului: " + e);
					e.printStackTrace();
				}
				
		return response;
		
	}


	public Object sendNIOTCPRequest(Request request, String serverIP,
			int serverPort) {
		
		Socket clientSocket = null;
		
		try {
			// Ne conectam la server.
			clientSocket = new Socket(serverIP, serverPort);
			
			// Serializam obiectul de tip Request si il codificam corespunzator.
			byte[] encodedObject = DataUtils.encode(request);
			
			// Trimitem datele pe socket.
			clientSocket.getOutputStream().write(encodedObject);
		} catch (Exception e) {
			System.err.println("[CMTClientObject NIOTCP-" + this.hashCode() + "] Eroare la trimiterea cererii: " + e);
			e.printStackTrace();
		}
		
		Request ans = null;
		// Asteapta raspunsul.
		try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());	
			ans = (Request) in.readObject();
			System.out.println("[CMTClientObject NIOTCP-" + this.hashCode() + "] Am primit raspunsul: " + ans);
			in.close();
			clientSocket.close();
		} catch (Exception e) {
			System.err.println("[CMTClientObject NIOTCP-" + this.hashCode() + "] Eroare la primirea raspunsului: " + e);
			e.printStackTrace();
		}
		
		return ans;
	}

	
	
	
}
