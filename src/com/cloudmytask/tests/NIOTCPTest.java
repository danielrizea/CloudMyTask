package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.centralservice.CentralServiceObject;
import com.cloudmytask.centralservice.CentralServiceSocketConnectorUDP;
import com.cloudmytask.client.NIOTCPClient;
import com.cloudmytask.client.Request;
import com.cloudmytask.client.TCPClient;
import com.cloudmytask.connectors.tcp.CMTServiceSocketConnectorTCP;
import com.cloudmytask.connectors.tcpnio.CMTServiceSocketConnectorNIOTCP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.MachineInfo;
import com.cloudmytask.service.client.CMTClientObject;

public class NIOTCPTest {

	
	public static void main(String args[]){
		
		int central_ports[] = new int[1];
		central_ports[0] = GlobalConfig.CENTRAL_UNIT_PORT;
		//start central service
		CentralServiceObject centralService = new CentralServiceObject();
		CentralServiceSocketConnectorUDP centralConnector = new CentralServiceSocketConnectorUDP(centralService, central_ports);
		centralConnector.start();
		
		
		int ports[] = new int[2];
		ports[0] = 5004;
		ports[1] = 5005;
		
		// Porneste serviciul de streaming.
		MachineInfo machineDescription = new MachineInfo(0, ports);
		
		CMTClientObject co = new CMTClientObject();
		// Porneste serviciul de streaming.
		CMTServiceObject ss = new CMTServiceObject(co,machineDescription,centralService);
		ss.start();


		CMTServiceSocketConnectorNIOTCP server = new CMTServiceSocketConnectorNIOTCP(ports, ss);
		server.startRunning();
				
		CMTClientObject clientObject = new CMTClientObject();
	
		ArrayList<NIOTCPClient> al = new ArrayList<NIOTCPClient>();
		
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
			
			Request r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
			r.scriptFileData = scriptData;
			r.scriptFileName = filename;	
			r.requestID = "id";
			System.out.println("Send request");
			
			Request response = (Request) clientObject.sendRequest(r, "localhost", 5004, 60001);
			System.out.println("Response from server : " + response.message);
		}
		catch(Exception e){
			
		}
		/*
		for (int i = 0; i < 1; i++) {
			NIOTCPClient client = new NIOTCPClient("localhost", 5004+i%2, 60002 + i + 1);
			client.start();
			al.add(client);
		}
		
		for (NIOTCPClient client: al) {
			try {
				client.join();
			} catch (Exception e) {}
		}
	*/
		
		try {
			Thread.sleep(30000);
		} catch (Exception e) {}
		
		// Opreste connector-ul de socketi.

		ss.stop();
		
		// Opreste serviciul de calcul scripturi.
		//ss.stop();
		
		
	}
}
