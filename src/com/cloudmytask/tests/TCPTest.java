package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cloudmytask.client.Request;
import com.cloudmytask.client.TCPClient;
import com.cloudmytask.client.UDPClient;
import com.cloudmytask.connectors.tcp.CMTServiceSocketConnectorTCP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.client.CMTClientObject;

public class TCPTest {

	
	public static void main(String args[]){
		
		int ports[] = new int[2];
		ports[0] = 5000;
		ports[1] = 5001;
		
		// Porneste serviciul de streaming.
		CMTServiceObject ss = new CMTServiceObject();
		
		//ss.start();
		
		// Porneste connector-ul de socketi.
		CMTServiceSocketConnectorTCP sssc = new CMTServiceSocketConnectorTCP(ss, ports);
		sssc.start();
		
		CMTClientObject clientObject = new CMTClientObject();
		
	
		ArrayList<TCPClient> al = new ArrayList<TCPClient>();
		
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
			
			Request response = (Request) clientObject.sendRequest(r, "localhost", 5001, 60001);
			System.out.println("Response from server : " + response.message);
		}
		catch(Exception e){
			
		}

		
		
		try {
			Thread.sleep(30000);
		} catch (Exception e) {}
		
		// Opreste connector-ul de socketi.
		sssc.stop();
		
		// Opreste serviciul de streaming.
		//ss.stop();
		
		
	}
}
