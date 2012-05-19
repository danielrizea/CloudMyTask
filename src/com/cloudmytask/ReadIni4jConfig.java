package com.cloudmytask;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.ini4j.*;

public class ReadIni4jConfig {
	public static final String generalConfigFileName = "configs/config.ini";
	public static final String addressConfigFileName = "configs/addressConfig.ini";
		
	public ReadIni4jConfig ()
	{
		

		int connections[][] = new int[100][100];
		Ini ini;
		try {
			ini = new Ini(new File(generalConfigFileName));
		} catch (Exception e) {
			System.err.println("Exception opening the testConfigFile:" + e);
			e.printStackTrace();
			return;
		}
		

		//prelucrare date din [connections]
		Ini.Section iniConnection = ini.get("connections");
		// prelucrare matrice de adiacenta
		String connectionMatrix = iniConnection.get("matrix");
		int connectionMatrixLength = connectionMatrix.length();
		
		int i = 0 ; 
		int nrCol = 0;
		int nrLine = 0; 
		while (i < connectionMatrixLength)
		{
			//e inceput de linie dintr-o matrice
			if ( connectionMatrix.charAt(i) == '{'){
				i++;
			}
			//e sfarsitul unei linii
			else if ( connectionMatrix.charAt(i) == '}'){
				nrCol ++;
				nrLine = 0;
				i++;
			}
			else if ( ( connectionMatrix.charAt(i) == ',') || ( connectionMatrix.charAt(i) == ' ') )
			{
				i++;
			}
			else {

				String aux = String.valueOf(connectionMatrix.charAt(i));
				connections[nrCol][nrLine] = Integer.parseInt(aux);
				i++;
				nrLine++;
			}
			
		}
		
		System.out.println("Dim matrix :" + nrLine);
		
		//dimensiunea fixa a matricei de vecini
		int array_dim = nrCol -1;
		int new_connections[][] = new int[array_dim][array_dim];
		
		for(i=0;i<array_dim;i++)
			for(int j=0;j<array_dim;j++)
				new_connections[i][j] = connections[i][j];
		
		GlobalConfig.connections = new_connections;
		
		GlobalConfig.MAX_REQUEST_PERIOD = Integer.parseInt(iniConnection.get("MAX_REQUEST_PERIOD"));
		GlobalConfig.MAX_REQUESTS_ALLOWED_IN_PERIOD = Integer.parseInt(iniConnection.get("MAX_REQUESTS_ALLOWED_IN_PERIOD"));
		
		//prelucrare date din  [protocol]
		Ini.Section iniProtocol = ini.get("protocol");
		GlobalConfig.CommunicationType = Integer.parseInt(iniProtocol.get("type"));
		
		//prelucrare date din  [runningTests]
		Ini.Section inirunningTests = ini.get("runningTests");
		GlobalConfig.NROFCLIENTS = Integer.parseInt(inirunningTests.get("NROFCLIENTS"));
		GlobalConfig.BEHAVIOUR = Integer.parseInt(inirunningTests.get("BEHAVIOUR"));
		GlobalConfig.processThreadsInPool = Integer.parseInt(inirunningTests.get("processThreadsInPool"));	
		GlobalConfig.NRTHREADS_CENTRALSERVICE = Integer.parseInt(inirunningTests.get("NRTHREADS_CENTRALSERVICE"));
		GlobalConfig.NRTHREADS_SERVICE = Integer.parseInt(inirunningTests.get("NRTHREADS_SERVICE"));
 		
		GlobalConfig.NRTHREADS_CREATESCRIPTFILE = Integer.parseInt(inirunningTests.get("NRTHREADS_CREATESCRIPTFILE"));
		GlobalConfig.NRTHREADS_JOBHANDOFF = Integer.parseInt(inirunningTests.get("NRTHREADS_JOBHANDOFF"));
		
		
		GlobalConfig.SCRIPT = inirunningTests.get("SCRIPT");
		
		//prelucrare fisier addressConfig.ini
		try {
			ini = new Ini(new File(addressConfigFileName));
		} catch (Exception e) {
			System.err.println("Exception opening the testConfigFile:" + e);
			e.printStackTrace();
			return;
		}
		//prelucrare date din [ports]
		Ini.Section iniPorts = ini.get("ports");
		GlobalConfig.CENTRAL_UNIT_PORT = Integer.parseInt(iniPorts.get("CENTRAL_UNIT_PORT"));
		GlobalConfig.INSTANCE_COMM_PORT = Integer.parseInt(iniPorts.get("INSTANCE_COMM_PORT"));
		GlobalConfig.MACHINE_LOCAL_PORT = Integer.parseInt(iniPorts.get("MACHINE_LOCAL_PORT"));
		GlobalConfig.CLIENT_COMM_PORT = Integer.parseInt(iniPorts.get("CLIENT_COMM_PORT"));
		GlobalConfig.MulticastPort = Integer.parseInt(iniPorts.get("MulticastPort"));
		GlobalConfig.BroadcastPeriod = Integer.parseInt(iniPorts.get("BroadcastPeriod"));
		
		//prelucrare date din [ips]	
		Ini.Section iniIPs = ini.get("ips");
		GlobalConfig.CENTRAL_UNIT_IP = iniIPs.get("CENTRAL_UNIT_IP");
		GlobalConfig.MulticastAddress = iniIPs.get("MulticastAddress");
		int nrMachineIPs = Integer.parseInt(iniIPs.get("nrMachineIPs"));
		
		//parsare valoare pentru machineIPs
		String machineIPs[] = new String[nrMachineIPs];
		for (i = 0 ; i < nrMachineIPs; i++){
			machineIPs[i] ="";
		}

		String machineString = iniIPs.get("machineIPs");
		int machineStringLength = machineString.length();
		//transform un string intr-un vector
		i = 0;
		int elementNr = 0;
		while (i < machineStringLength -1)
		{
			//e inceputul elementului 
			if ( machineString.charAt(i) == '{'){
				i++;
			}
			//urmeaza un alt element
			else if ( machineString.charAt(i) == ','){
				elementNr ++;
				i++;
			}
			else if  ( machineString.charAt(i) == ' ') {
				i++;
			}
			else {
				machineIPs[elementNr] += String.valueOf(machineString.charAt(i));
				i++;
			}	
		}
		GlobalConfig.machineIPs = machineIPs;

		
		
		
		

	}



}