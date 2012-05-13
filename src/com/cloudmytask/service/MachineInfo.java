package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MachineInfo {

	
	public int id;
	public int[] neighbours;
	public String machineIP;
	
	private int nrProcessors;
	
	public BufferedWriter outLog = null;
	
	private static final int WORKING_THREADS_PER_PROCESS = 2;
	
	private static int MAX_WORKING_THREADS_ON_MACHINE;
	
	public MachineInfo(int id, int[] neighbours){
		
		this.id = id;
		this.neighbours = neighbours;
		
		Runtime runtime = Runtime.getRuntime();
        
        this.nrProcessors = runtime.availableProcessors();
        
        MAX_WORKING_THREADS_ON_MACHINE = getMaxJobsInExecution(); 
        
        try{
        	outLog = new BufferedWriter(new FileWriter("InstanceService_"+this.id+".log")); 	
        }
        catch(Exception e){
        	System.out.println("[MachineInfo] Error in creating log file " + e.getMessage());
        }
        
        System.out.println("[CMTServiceObject] machine " + id + " has " + nrProcessors + " can handle " + MAX_WORKING_THREADS_ON_MACHINE + " jobs in parallel");
	}
	
	public void writeToLogFile(String tag, String message){
		try{
			if(outLog != null){
				outLog.append("["+tag+" "+ this.id +"] " + message );
				outLog.newLine();
			}
		}catch(Exception e){
			System.out.println("Exception writing " + tag + " " + message + " with " + e.getMessage());
		}
	}
	
	public int getMaxJobsInExecution(){
		
		return WORKING_THREADS_PER_PROCESS * nrProcessors;
	}
}
