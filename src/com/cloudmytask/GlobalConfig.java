package com.cloudmytask;

public class GlobalConfig {

	public static int TCP ;
	public static int UDP ;
	public static int NIOTCP ;
	public static int CommunicationType ;
	// 1 marcheaza conexiunea
	public static int connections[][];

	public static int MAX_REQUEST_PERIOD ;	
	public static int MAX_REQUESTS_ALLOWED_IN_PERIOD ;

	//ip's
	
	
	public static int INSTANCE_COMM_PORT ;	
	public static int CLIENT_COMM_PORT ;
	public static int MACHINE_LOCAL_PORT;
	public static int CENTRAL_UNIT_PORT ;
	public static int MulticastPort;
	public static long BroadcastPeriod ;
	
	//porturi masina
	//local fiecare masina/serviciu booteaza pe 
	//	2 porturi de comunicare  ID masina +5000 
	// daca masina 1 doreste comunicare cu masina 2 aceasta se conecteaza pe 5002 a masinii 2
	
	//  2 porturi comunicare clienti, job-uri + comenzi 7000 + 2*id., 7000 + 2*(id-1).
	
	//2 seconds
	
	//portul pe care asculta masina centrala
	
	public static String CENTRAL_UNIT_IP ;
	public static String machineIPs[] ;
	public static String MulticastAddress;
	
	
	//multicast IP

	
	
}
