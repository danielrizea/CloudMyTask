package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class ProcessMachineLoadRequestJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineDescription;
	private ConcurrentHashMap<String, Request> executingRequests;
	
	public ProcessMachineLoadRequestJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, MachineInfo machineDescription, ConcurrentHashMap<String, Request> executingRequests) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.machineDescription = machineDescription;
		this.executingRequests = executingRequests;
		
	}
	
	public void run() {
	
		Request answer = new Request("[CMTServiceObjectInstance "+machineDescription.id+"] incarcare masina", Request.REQUEST_GET_LOAD);
		System.out.println("[CMTServiceObjectInstance "+machineDescription.id+"] send local load " + 100*((float)(executingRequests.size()/machineDescription.getMaxJobsInExecution())));
		answer.loadFactor = 100*((float)(executingRequests.size()/machineDescription.getMaxJobsInExecution()));
		ci.sendResult(answer);
		
	}
}
