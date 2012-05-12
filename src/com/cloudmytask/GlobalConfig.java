package com.cloudmytask;

public class GlobalConfig {

	public static int TCP = 0;
	public static int UDP = 1;
	public static int NIOTCP = 2;

	public static int CommunicationType = TCP;
	
	// 1 marcheaza conexiunea
	public static int connections[][]= {
		
		{0,1,0,0,1},
		{1,0,1,0,0},
		{0,1,0,1,1},
		{0,0,1,0,0},
		{1,0,1,0,0},
	};
	
	public static int INSTANCE_COMM_PORT = 5000;
	
	public static int CLIENT_COMM_PORT = 7000;
	
	public static int MACHINE_LOCAL_PORT = 6000;
	
	//ip's
	
	public static String machineIPs[] ={"localhost","localhost","localhost","localhost","localhost"};
	
	
	//porturi masina
	//local fiecare masina/serviciu booteaza pe 
	//	2 porturi de comunicare  ID masina +5000 
	// daca masina 1 doreste comunicare cu masina 2 aceasta se conecteaza pe 5002 a masinii 2
	
	//  2 porturi comunicare clienti, job-uri + comenzi 7000 + 2*id., 7000 + 2*(id-1).
	
	//2 seconds
	public static int MAX_REQUEST_PERIOD = 1000;
	
	public static int MAX_REQUESTS_ALLOWED_IN_PERIOD = 4;
	
	//portul pe care asculta masina centrala
	public static int CENTRAL_UNIT_PORT = 30000;
	
	public static String CENTRAL_UNIT_IP = "localhost";
	
}
