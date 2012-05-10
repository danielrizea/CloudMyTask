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
import com.cloudmytask.service.MachineDescription;
import com.cloudmytask.service.client.CMTClientObject;

public class GlobalTest {

	
	
	
	public static void main(String args[]){
		
		
		
		ArrayList<CMTServiceObject> serviceObjectList = new ArrayList<CMTServiceObject>();
		ArrayList<CMTServiceSocketConnectorTCP> tcpConnectorList = new ArrayList<CMTServiceSocketConnectorTCP>();
		ArrayList<CMTServiceSocketConnectorNIOTCP> nioTcpConnectorList = new ArrayList<CMTServiceSocketConnectorNIOTCP>();
		ArrayList<CMTServiceSocketConnectorUDP> udpConnectorList = new ArrayList<CMTServiceSocketConnectorUDP>();
		
		for(int i=0;i<GlobalConfig.connections.length;i++){
			
			
			CMTClientObject clientObject = new CMTClientObject();
			
			int ports[] = new int[1];
			
			ports[0] = 7000 + 2*i;		
					
			MachineDescription machineDescription = new MachineDescription(i, GlobalConfig.connections[i]);
			// Porneste serviciul de streaming.
			CMTServiceObject ss = new CMTServiceObject(clientObject,machineDescription);
			
			serviceObjectList.add(ss);
			
			// Porneste connector-ul de socketi.
			CMTServiceSocketConnectorTCP sssc = new CMTServiceSocketConnectorTCP(ss, ports);
			sssc.start();
			
			tcpConnectorList.add(sssc);
			
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
