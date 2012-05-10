package com.cloudmytask.utils;

import java.util.HashMap;


public class MachineInternalInfo {

	
	public String ID;
	public String name;
	public int numberOfProcessors;
	public String IP;
	// asociere IP-port pentru vecini
	public HashMap<String,int[]> neighbours;
	
	public MachineInternalInfo (String ID){
		this.ID = ID;
		getAvailableProcessors();
		//get neighbors -> TODO 
		getNeighboursInfo();
	}
	
	 public void getAvailableProcessors() {
	        
	       Runtime runtime = Runtime.getRuntime();    
	       this.numberOfProcessors= runtime.availableProcessors();
	       System.out.println("Number of processors available to the Java Virtual Machine: " + this.numberOfProcessors);
	        
	}
	 
	 //TODO 
	 public void getNeighboursInfo(){
		 
	 }
	 
	
}
