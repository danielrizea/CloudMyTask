package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.client.TCPClient;
import com.cloudmytask.connectors.tcp.CMTServiceSocketConnectorTCP;
import com.cloudmytask.connectors.tcpnio.CMTServiceSocketConnectorNIOTCP;
import com.cloudmytask.connectors.udp.CMTServiceSocketConnectorUDP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.MachineInfo;
import com.cloudmytask.service.client.CMTClientObject;
import com.cloudmytask.utils.CommunicationUtils;

public class GlobalTest {

	
	
	
	public static void main(String args[]){
		
		
		
		ArrayList<CMTServiceObject> serviceObjectList = new ArrayList<CMTServiceObject>();
		ArrayList<CMTServiceSocketConnectorTCP> tcpConnectorList = new ArrayList<CMTServiceSocketConnectorTCP>();
		ArrayList<CMTServiceSocketConnectorNIOTCP> nioTcpConnectorList = new ArrayList<CMTServiceSocketConnectorNIOTCP>();
		ArrayList<CMTServiceSocketConnectorUDP> udpConnectorList = new ArrayList<CMTServiceSocketConnectorUDP>();
		
		for(int i=0;i<GlobalConfig.connections.length;i++){
			
			
			CMTClientObject clientObject = new CMTClientObject();
			
			int ports[] = new int[3];
			
			ports[0] = GlobalConfig.INSTANCE_COMM_PORT + i;
			ports[1] = GlobalConfig.MACHINE_LOCAL_PORT + i;
			ports[2] = GlobalConfig.CLIENT_COMM_PORT + i;
					
			MachineInfo machineDescription = new MachineInfo(i, GlobalConfig.connections[i]);
			// Porneste serviciul de streaming.
			CMTServiceObject ss = new CMTServiceObject(clientObject,machineDescription);
			
			serviceObjectList.add(ss);
			
			// Porneste connector-ul de socketi.
			CMTServiceSocketConnectorTCP sssc = new CMTServiceSocketConnectorTCP(ss, ports);
			sssc.start();
			
			tcpConnectorList.add(sssc);
			
		}


		try{
			final String filename = "test_sleep.py";
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
			final String data = scriptData;

			Request r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
			r.scriptFileData = scriptData;
			r.scriptFileName = filename;	
			System.out.println("Send request");
			r.requestID = r.hashCode() + "_" + r;
			//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
			ArrayList<Thread> clientRequests = new ArrayList<Thread>();
			for(int i=0;i<4;i++){
				
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						Request r;
						// TODO Auto-generated method stub
						r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
						r.scriptFileData = data;
						r.scriptFileName = filename;	
						r.requestID = r.hashCode() + "_" + r + "_";
						//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
						Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, 60001);
						System.out.println("Response from server : " + response.message);
					}
				});
				th.start();
				
				clientRequests.add(th);

				
			}

			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			for(int i=0;i<4;i++){
				
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						Request r;
						// TODO Auto-generated method stub
						r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
						r.scriptFileData = data;
						r.scriptFileName = filename;	
						r.requestID = r.hashCode() + "_" + r + "_";
						//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
						Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, 60001);
						System.out.println("Response from server : " + response.message);
					}
				});
				th.start();
				
				clientRequests.add(th);
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			for(int i=0;i<10;i++){
				
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						Request r;
						// TODO Auto-generated method stub
						r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
						r.scriptFileData = data;
						r.scriptFileName = filename;	
						r.requestID = r.hashCode() + "_" + r + "_";
						//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
						Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, 60001);
						System.out.println("Response from server : " + response.message);
					}
				});
				th.start();
				
				clientRequests.add(th);
			}
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
			for(int i=0;i<10;i++){
				
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						Request r;
						// TODO Auto-generated method stub
						r = new Request("salut de la", Request.REQUEST_PROCESS_SCRIPT);			
						r.scriptFileData = data;
						r.scriptFileName = filename;	
						r.requestID = r.hashCode() + "_" + r + "_";
						//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
						Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, 60001);
						System.out.println("Response from server : " + response.message);
					}
				});
				th.start();
				
				clientRequests.add(th);
			}
			
			for(Thread clientRequest : clientRequests){
				clientRequest.join();
			}
		
			//CommunicationUtils.sendRequest(r, "localhost", 5001, 60001);
			
			Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", 5001, 60001);
			System.out.println("Response from server : " + response.message);
		}
		catch(Exception e){
			
		}
		
		
		try {
			Thread.sleep(30000);
		} catch (Exception e) {}
		
		// Opreste connector-ul de socketi.
		
		
		for (CMTServiceSocketConnectorTCP service: tcpConnectorList) {
			try {
				service.stop();
			} catch (Exception e) {}
		}
		
		
	}
}
