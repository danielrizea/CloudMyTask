package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class FilterClientsJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineInfo;
	private CMTClientPublicInterface clientInterface;
	private ConcurrentHashMap<String, Integer> clientsRequests;
	
	public FilterClientsJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, CMTClientPublicInterface clientInterface, MachineInfo machineInfo, ConcurrentHashMap<String, Integer> clientsRequests) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.machineInfo = machineInfo;
		this.clientInterface = clientInterface;
		this.clientsRequests = clientsRequests;
	}
	
	public void run() {
	
		//send to client// wait for response;
		
		if(clientsRequests.containsKey(request.clientID)){
			
			int jobsInTheLastPeriod = clientsRequests.get(request.clientID);
			
			if(jobsInTheLastPeriod >= GlobalConfig.MAX_REQUESTS_ALLOWED_IN_PERIOD){
				Request answer = new Request("user maximum scripts allowed in " + GlobalConfig.MAX_REQUEST_PERIOD/1000 + " seconds", Request.REQUEST_PROCESS_SCRIPT);
				answer.requestID = answer.hashCode() + "_answer";
				System.out.println("[CMTServiceObject "+machineInfo.id+"] filter client " + request.clientID);
				this.service.sendAnswerToClient(answer, ci);
			}
			else{
				this.service.decideMachineAvailable(request, ci);
			}
		}
		else
			this.service.decideMachineAvailable(request, ci);
		
	}
}
