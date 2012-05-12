package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.centralservice.CentralServiceObject;
import com.cloudmytask.centralservice.CentralServiceSocketConnectorUDP;
import com.cloudmytask.client.Request;
import com.cloudmytask.client.TCPClient;
import com.cloudmytask.client.UDPClient;
import com.cloudmytask.connectors.tcp.CMTServiceSocketConnectorTCP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.MachineInfo;
import com.cloudmytask.service.client.CMTClientObject;
import com.cloudmytask.utils.CommunicationUtils;

public class TCPTest {

	
	public static void main(String args[]){
		int central_ports[] = new int[1];
		central_ports[0] = GlobalConfig.CENTRAL_UNIT_PORT;
		//start central service
		CentralServiceObject centralService = new CentralServiceObject();
		CentralServiceSocketConnectorUDP centralConnector = new CentralServiceSocketConnectorUDP(centralService, central_ports);
		centralConnector.start();
		
		int ports[] = new int[2];
		ports[0] = 5000;
		ports[1] = 5001;
		
		MachineInfo machineDescription = new MachineInfo(0, ports);
		
		CMTClientObject co = new CMTClientObject();
		// Porneste serviciul de streaming.
		CMTServiceObject ss = new CMTServiceObject(co,machineDescription,centralService);
		
		//ss.start();
		
		// Porneste connector-ul de socketi.
		CMTServiceSocketConnectorTCP sssc = new CMTServiceSocketConnectorTCP(ss, ports);
		sssc.start();
		
		CMTClientObject clientObject = new CMTClientObject();
		
	
		ArrayList<TCPClient> al = new ArrayList<TCPClient>();
		
		try{
			String filename = "test_sleep.py";
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
			System.out.println("Send request");
			r.requestID = r.hashCode() + "_" + r;
			//CommunicationUtils.sendRequest(r, "localhost", 5001, 60001);
			/*
			for(int i=0;i<3;i++){
				r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
				r.scriptFileData = scriptData;
				r.scriptFileName = filename;	
				System.out.println("Send request");
				r.requestID = r.hashCode() + "_" + r + "_" + i;
				CommunicationUtils.sendRequest(r, "localhost", 5001, 60001);

			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			*/
			//CommunicationUtils.sendRequest(r, "localhost", 5001, 60001);
			
			Request response = (Request) clientObject.sendRequest(r, "localhost", 5001, 60001);
			System.out.println("Response from server : " + response.message);
		}
		catch(Exception e){
			System.out.println("Exception " + e.getMessage());
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
