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
	private ConcurrentHashMap<String, Integer> clientsRequests;
	
	public AvailableMachineJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, MachineInfo machineDescription, ConcurrentHashMap<String, Request> executingRequests, ConcurrentHashMap<String, Integer> clientsRequests) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.machineDescription = machineDescription;
		this.executingRequests = executingRequests;
		this.clientsRequests = clientsRequests;
	}
	
	public void run() {
	
	
		//add another request for user
		int requests = 1;
		if(clientsRequests.containsKey(request.clientID)){
			requests = clientsRequests.get(request.clientID);
			requests++;
			clientsRequests.replace(request.clientID, requests);
			System.out.println("[CMTServiceObject "+machineDescription.id+"] info in database ");
		}
		else
			clientsRequests.put(request.clientID, requests);
		
		System.out.println("[CMTServiceObject "+machineDescription.id+"] status of requests" + request.clientID + " " + requests);
		
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
