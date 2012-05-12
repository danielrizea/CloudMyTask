package com.cloudmytask.utils;

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

public class CommunicationUtils {

	public CommunicationUtils(){
		
	}
	
	public static void sendRequest(Request request, String serverIP,int serverPort, int clientPort){
		//decide on the communication method used
		
				if(GlobalConfig.CommunicationType == GlobalConfig.TCP)
					sendTCPRequest(request, serverIP, serverPort);
				
				if(GlobalConfig.CommunicationType == GlobalConfig.UDP)
					sendUDPRequest(request, serverIP, serverPort, clientPort);
				
				if(GlobalConfig.CommunicationType == GlobalConfig.NIOTCP)
					sendNIOTCPRequest(request, serverIP, serverPort);
	}
	
	
	public static Object sendRequestGetResponse(Request request, String serverIP,int serverPort, int clientPort){
	
		if(GlobalConfig.CommunicationType == GlobalConfig.TCP)
			return sendTCPRequestGetResponse(request, serverIP, serverPort);
		
		if(GlobalConfig.CommunicationType == GlobalConfig.UDP)
			return sendUDPRequestGetResponse(request, serverIP, serverPort, clientPort);
		
		if(GlobalConfig.CommunicationType == GlobalConfig.NIOTCP)
			return sendNIOTCPRequestGetResponse(request, serverIP, serverPort);
		
		return null;
	}
	
	private static void sendTCPRequest(Request request, String serverIP, int serverPort) {
		Socket clientSocket=null;
		try{
			//DatagramSocket socket = new DatagramSocket(this.clientPort);
			clientSocket = new Socket(serverIP, serverPort);
			
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(request);
			//oos.close();
			
		}catch(Exception e){
			System.err.println("[CMTClientObject] error in sending TCP Request : " +  e.getMessage());
		}		
	}


	private static void sendUDPRequest(Request request, String serverIP,
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
	}


	private static void sendNIOTCPRequest(Request request, String serverIP,
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
			System.err.println("[CMTClientObject NIOTCP-"  + "] Eroare la trimiterea cererii: " + e);
			e.printStackTrace();
		}
	}
		
	public static Object sendTCPRequestGetResponse(Request request, String serverIP, int serverPort) {

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
	            System.err.println("[CMTClientObject get response]- exception: " + e.getMessage());
	            e.printStackTrace();
	        }
		return response;
		
	}


	public static Object sendUDPRequestGetResponse(Request request, String serverIP,
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


	public static Object sendNIOTCPRequestGetResponse(Request request, String serverIP,
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
			System.err.println("[CMTClientObject NIOTCP-"+ "] Eroare la trimiterea cererii: " + e);
			e.printStackTrace();
		}
		
		Request ans = null;
		// Asteapta raspunsul.
		try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());	
			ans = (Request) in.readObject();
			System.out.println("[CMTClientObject NIOTCP-"  + "] Am primit raspunsul: " + ans);
			in.close();
			clientSocket.close();
		} catch (Exception e) {
			System.err.println("[CMTClientObject NIOTCP-"+ "] Eroare la primirea raspunsului: " + e);
			e.printStackTrace();
		}
		
		return ans;
	}

}
