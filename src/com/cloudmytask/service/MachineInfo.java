package com.cloudmytask.service;

public class MachineInfo {

	
	public int id;
	public int[] neighbours;
	public String machineIP;
	
	private int nrProcessors;
	
	private static final int WORKING_THREADS_PER_PROCESS = 2;
	
	private static int MAX_WORKING_THREADS_ON_MACHINE;
	
	public MachineInfo(int id, int[] neighbours){
		
		this.id = id;
		this.neighbours = neighbours;
		
		Runtime runtime = Runtime.getRuntime();
        
        this.nrProcessors = runtime.availableProcessors();
        
        MAX_WORKING_THREADS_ON_MACHINE = getMaxJobsInExecution(); 
        
        System.out.println("[CMTServiceObject] machine " + id + " has " + nrProcessors + " can handle " + MAX_WORKING_THREADS_ON_MACHINE + " jobs in parallel");
	}
	
	
	public int getMaxJobsInExecution(){
		
		return WORKING_THREADS_PER_PROCESS * nrProcessors;
	}
}
