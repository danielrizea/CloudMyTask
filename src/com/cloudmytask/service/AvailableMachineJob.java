package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class AvailableMachineJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineDescription;
	private ConcurrentHashMap<String, Request> executingRequests;

	public AvailableMachineJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, MachineInfo machineDescription, ConcurrentHashMap<String, Request> executingRequests) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.machineDescription = machineDescription;
		this.executingRequests = executingRequests;
	}
	
	public void run() {
	
	
		int maxJobs = machineDescription.getMaxJobsInExecution();
		int currentJobs = executingRequests.size();
		
		
		if(currentJobs < maxJobs){
			System.out.println("[CMTServiceObject] Execute on local machine " + currentJobs + " " + maxJobs+ " load factor:" + (float) currentJobs/maxJobs);
			this.service.createServerScriptFile(request, ci);
			
			
		}else{
			System.out.println("[CMTServiceObject] Pass to another machine " + " load factor:" + (float)(currentJobs/maxJobs) );
			this.service.getFreeNeighbor(request, ci);
		}
	}
}
