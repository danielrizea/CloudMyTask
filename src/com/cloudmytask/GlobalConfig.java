package com.cloudmytask;

import java.util.concurrent.Executors;

public class GlobalConfig {

	public static final int TCP = 0;
	public static final int UDP = 1;
	public static final int NIOTCP = 2;
	public static int CommunicationType ;
	// 1 marcheaza conexiunea
	public static int connections[][];

	public static int MAX_REQUEST_PERIOD ;	
	public static int MAX_REQUESTS_ALLOWED_IN_PERIOD ;
	public static boolean BANN_ENABLE = false;
	

	//porturi masina
	public static int INSTANCE_COMM_PORT ;	
	// exemplu: 2 porturi comunicare clienti, job-uri + comenzi 7000 + 2*id., 7000 + 2*(id-1).
	public static int CLIENT_COMM_PORT ;
	//portul pe care asculta masina centrala
	public static int MACHINE_LOCAL_PORT;
	public static int CENTRAL_UNIT_PORT ;
	//multicast port
	public static int MulticastPort;
	public static long BroadcastPeriod ;
	
	// 0 -> 1 thread per request
	// 1 - > use thread pool
	public static int BEHAVIOUR;
	public static int processThreadsInPool;
	
	
	public static int NRTHREADS_CENTRALSERVICE; 
	public static int NRTHREADS_SERVICE;
	
	public static int NRTHREADS_CREATESCRIPTFILE; 
	public static int NRTHREADS_RUNSCRIPTONSERVER; 
	public static int NRTHREADS_JOBHANDOFF;
	

	
	//ip's	
	public static String CENTRAL_UNIT_IP ;
	//multicast ips
	public static String machineIPs[] ;
	public static String MulticastAddress;
	
	//parametri rulare teste
	public static int NROFCLIENTS;
	public static String SCRIPT;
	
	
}
