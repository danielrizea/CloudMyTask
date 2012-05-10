package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cloudmytask.client.Request;
import com.cloudmytask.client.UDPClient;
import com.cloudmytask.connectors.udp.CMTServiceSocketConnectorUDP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.client.CMTClientObject;

public class UDPTest {

	
	public static void main(String args[]){
		
		int ports[] = new int[2];
		ports[0] = 5000;
		ports[1] = 5001;
		
		// Porneste serviciul de streaming.
		CMTServiceObject ss = new CMTServiceObject();
		
		//ss.start();
		
		// Porneste connector-ul de socketi.
		CMTServiceSocketConnectorUDP sssc = new CMTServiceSocketConnectorUDP(ss, ports);
		sssc.start();
	
		ArrayList<UDPClient> al = new ArrayList<UDPClient>();
		
		CMTClientObject clientObject = new CMTClientObject();
		
		try{
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
			
			Request r = new Request("salut de la", 1);			
			r.scriptFileData = scriptData;
			r.scriptFileName = filename;	
			System.out.println("Send request");
			
			Request response = (Request)clientObject.sendRequest(r, "localhost", 5000, 60001);
			System.out.println(response.message);
		}
		catch(Exception e){
			
		}
		
		/*
		for (int i = 0; i < 1; i++) {
			UDPClient client = new UDPClient("localhost", 5000+i%2, 60000 + i + 1);
			client.start();
			al.add(client);
		}
		
		for (UDPClient client: al) {
			try {
				client.join();
			} catch (Exception e) {}
		}
	*/
		
		try {
			Thread.sleep(30000);
		} catch (Exception e) {}
		
		// Opreste connector-ul de socketi.
		sssc.stop();
		
		// Opreste serviciul de streaming.
		//ss.stop();
		
		
	}
}
