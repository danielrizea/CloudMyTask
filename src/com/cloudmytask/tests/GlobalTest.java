package com.cloudmytask.tests;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.ReadIni4jConfig;
import com.cloudmytask.centralservice.CentralServiceObject;
import com.cloudmytask.centralservice.CentralServiceSocketConnectorUDP;
import com.cloudmytask.client.AdvancedClient;

import com.cloudmytask.connectors.tcp.CMTServiceSocketConnectorTCP;
import com.cloudmytask.connectors.tcpnio.CMTServiceSocketConnectorNIOTCP;
import com.cloudmytask.connectors.udp.CMTServiceSocketConnectorUDP;
import com.cloudmytask.service.CMTServiceObject;
import com.cloudmytask.service.MachineInfo;
import com.cloudmytask.service.client.CMTClientObject;

public class GlobalTest {

	
	public static ArrayList<CMTServiceObject>  serviceObjectList = null;	
	public static ArrayList<CMTServiceSocketConnectorTCP> tcpConnectorList = null;
	public static ArrayList<CMTServiceSocketConnectorNIOTCP> nioTcpConnectorList = null;
	public static ArrayList<CMTServiceSocketConnectorUDP> udpConnectorList = null;
	public static CentralServiceObject centralService = null;
	public static CentralServiceSocketConnectorUDP centralConnector = null;
	
	public static void startService(){
		
		serviceObjectList = new ArrayList<CMTServiceObject>();
		tcpConnectorList = new ArrayList<CMTServiceSocketConnectorTCP>();
		nioTcpConnectorList = new ArrayList<CMTServiceSocketConnectorNIOTCP>();
		udpConnectorList = new ArrayList<CMTServiceSocketConnectorUDP>();
		
		
		int central_ports[] = new int[1];
		central_ports[0] = GlobalConfig.CENTRAL_UNIT_PORT;
		
		//start central service
		centralService = new CentralServiceObject();
		centralConnector = new CentralServiceSocketConnectorUDP(centralService, central_ports);
		centralConnector.start();
		
		System.out.println("Values " + GlobalConfig.connections.length);
		
		//start clients
		for(int i=0;i<GlobalConfig.connections.length;i++){
			CMTClientObject clientObject = new CMTClientObject();
			
			int ports[] = new int[3];
			
			ports[0] = GlobalConfig.INSTANCE_COMM_PORT + i;
			ports[1] = GlobalConfig.MACHINE_LOCAL_PORT + i;
			ports[2] = GlobalConfig.CLIENT_COMM_PORT + i;
					
			MachineInfo machineDescription = new MachineInfo(i, GlobalConfig.connections[i]);
			// Porneste serviciul de streaming.
			CMTServiceObject ss = new CMTServiceObject(clientObject,machineDescription,centralService);
			ss.start();
			serviceObjectList.add(ss);
			
			System.out.println("Start instance " + i  + " ");
			// Porneste connector-ul de socketi in functie de tipul de conexiune setat
			if(GlobalConfig.CommunicationType == GlobalConfig.TCP){
				// Porneste connector-ul de socketi.
				CMTServiceSocketConnectorTCP sssc = new CMTServiceSocketConnectorTCP(ss, ports);
				sssc.start();
				tcpConnectorList.add(sssc);
			}
			else
			if(GlobalConfig.CommunicationType == GlobalConfig.UDP){
				// Porneste connector-ul de socketi.
				CMTServiceSocketConnectorUDP sssc = new CMTServiceSocketConnectorUDP(ss, ports);
				sssc.start();
				udpConnectorList.add(sssc);
			}
			else
			if(GlobalConfig.CommunicationType == GlobalConfig.NIOTCP){
				// Porneste connector-ul de socketi.
				CMTServiceSocketConnectorNIOTCP sssc = new CMTServiceSocketConnectorNIOTCP(ss, ports);
				sssc.startRunning();
				nioTcpConnectorList.add(sssc);
			}
		}
	}
	
	public static void test_1(){
		
		ArrayList<AdvancedClient> advancedClients = new ArrayList<AdvancedClient>();
		
		//obtinere singleton testare pentru MonALISA
		ApMonLog apm = ApMonLog.getInstance();
		
		int nr_clienti = GlobalConfig.NROFCLIENTS;
		
		for(int i=0;i<nr_clienti;i++){
			AdvancedClient client = new AdvancedClient("client_"+i, "localhost", GlobalConfig.CLIENT_COMM_PORT, 10000+i);
			advancedClients.add(client);
		}
		
		String filename = null;
		String data = null;
		
			try{	
				//fisierul ce va fi trimis de un client pentru a 
				filename = GlobalConfig.SCRIPT;
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
				data = scriptData;

			}
			catch(Exception e){
				System.out.println("Exception in reading script file");
			}
			
			Random rand = new Random();
			int maxRequestsPerCicle = 30;
			for(int t=0;t<1;t++){
				for(int i=0;i<advancedClients.size();i++){
			
					int requests = 100; 
							//rand.nextInt(maxRequestsPerCicle);
											
					for(int j=0;j<requests;j++)
						advancedClients.get(i).submitScriptForExecutionBlockOnWaitingResult(data, filename);
				
					try {
						Thread.sleep(500);
					} catch (Exception e) {}
					
				}
			}
	}
	
	
	public static void main(String args[]){
		
		
		ReadIni4jConfig var = new ReadIni4jConfig();
		startService();
		test_1();

	}
}
