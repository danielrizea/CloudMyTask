package com.cloudmytask.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.cloudmytask.utils.DataUtils;

public class NIOTCPClient extends Thread {
	public static final int NumberOfClients = 4;
	public static final int NumberOfMessages = 3;
	
	private String serverIP;
	private int serverPort;
	public int clientPort;
	
	public NIOTCPClient(String serverIP, int serverPort, int clientPort) {
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
			Request r = new Request("salut de la" +this.clientPort, 1);			
			r.scriptFileData = scriptData;
			r.scriptFileName = filename;				

			
			
			//DatagramSocket socket = new DatagramSocket(this.clientPort);
			Socket sockets = new Socket(this.serverIP, this.serverPort);
			
			
			//facem conversia la byte !!!
			byte[] req = DataUtils.encode(r);
			sockets.getOutputStream().write(req);

			
		} catch (Exception e) {
			System.err.println("[CacheServiceUDPClient] Eroare la trimiterea cererii: " + e);
			e.printStackTrace();
		}
	}

}
