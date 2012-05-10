package com.cloudmytask.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends Thread {
	public static final int NumberOfClients = 4;
	public static final int NumberOfMessages = 3;
	
	private String serverIP;
	private int serverPort;
	public int clientPort;
	
	public UDPClient(String serverIP, int serverPort, int clientPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientPort = clientPort;
	}
	
	public void run() {

			
			
			try {
				
				String filename = "testscript.py";
				
				//citire script python
				  FileInputStream fstream = new FileInputStream(filename);
				  // Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  //Read File Line By Line
				  
				  String scriptData = "";
				  while ((strLine = br.readLine()) != null)   {
				  // Print the content on the console
				  //System.out.println (strLine);
				  scriptData += strLine + "\n";
				  }
				  //Close the input stream
				  in.close();
				
				
				DatagramSocket socket = new DatagramSocket(this.clientPort);
			
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				
				Request r = new Request("salut de la" +this.clientPort, 1);
				
				r.scriptFileData = scriptData;
				r.scriptFileName = filename;
				
				oos.writeObject(r);
				byte[] outputBuf = baos.toByteArray();
				
				InetAddress serverAddr = InetAddress.getByName(this.serverIP);
				DatagramPacket packet = new DatagramPacket(outputBuf, 0, outputBuf.length, serverAddr, this.serverPort);
				socket.send(packet);
				oos.close();
				baos.close();
			} catch (Exception e) {
				System.err.println("[CacheServiceUDPClient] Eroare la trimiterea cererii: " + e);
				e.printStackTrace();
			}
	}

}
