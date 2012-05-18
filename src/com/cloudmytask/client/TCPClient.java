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
import java.net.Socket;

public class TCPClient extends Thread {
	public static final int NumberOfClients = 4;
	public static final int NumberOfMessages = 3;
	
	private String serverIP;
	private int serverPort;
	public int clientPort;
	
	public TCPClient(String serverIP, int serverPort, int clientPort) {
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
				
				// formare string cu continutul fisierului
				scriptData += strLine + "\n";
			}
			//Close the input stream
			in.close();
			Request r = new Request("salut de la" +this.clientPort, 1);			
			r.scriptFileData = scriptData;
			r.scriptFileName = filename;				

			InetAddress serverAddr = InetAddress.getByName(this.serverIP);
				
			//DatagramSocket socket = new DatagramSocket(this.clientPort);
			Socket sockets = new Socket(this.serverIP, this.serverPort);
			
			ObjectOutputStream oos = new ObjectOutputStream(sockets.getOutputStream());
			oos.writeObject(r);
			oos.close();

			
		} catch (Exception e) {
			System.err.println("[CacheServiceUDPClient] Eroare la trimiterea cererii: " + e);
			e.printStackTrace();
		}
	}

}
