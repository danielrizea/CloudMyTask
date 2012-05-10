package com.cloudmytask;

public class GlobalConfig {

	public static int TCP = 0;
	public static int UDP = 1;
	public static int NIOTCP = 2;
	
	
	
	// 1 marcheaza conexiunea
	public static int connections[][]= {
		
		{0,1,0,0,1},
		{1,0,1,0,0},
		{0,1,0,1,1},
		{0,0,1,0,0},
		{1,0,1,0,0},
	};
	
	//ip's
	
	public static String machineIPs[] ={"localhost","IP1","IP2","IP3","IP4"};
	
	
	//porturi masina
	//local fiecare masina/serviciu booteaza pe 
	//	2 porturi de comunicare  ID masina +5000 
	// daca masina 1 doreste comunicare cu masina 2 aceasta se conecteaza pe 5002 a masinii 2
	
	//  2 porturi comunicare clienti, job-uri + comenzi 7000 + 2*id., 7000 + 2*(id-1).
	
	
	
	
	public static int CommunicationType = UDP;
	
}
