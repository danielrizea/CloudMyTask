package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class HandOffJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo md;
	private int machineID;
	private CMTClientPublicInterface clientInterface;
	private ConcurrentHashMap<String, Request> redirectedRequests;
	
	public HandOffJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, CMTClientPublicInterface clientInterface, MachineInfo md, int machineID, ConcurrentHashMap<String, Request> redirectedRequests) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.md = md;
		this.machineID = machineID;
		this.clientInterface = clientInterface;
		this.redirectedRequests = redirectedRequests;
	}
	
	public void run() {
	
		//send to client// wait for response;
		
		String serverIP = GlobalConfig.machineIPs[machineID];
		int serverPort = GlobalConfig.INSTANCE_COMM_PORT + machineID;
		int clientPort = GlobalConfig.MACHINE_LOCAL_PORT + machineID;
	
		
		redirectedRequests.put(request.requestID, request);
		//request fro another service instance
		request.type = Request.REQUEST_PASS_SCRIPT;
		Request answer = (Request)this.clientInterface.sendRequest(request, serverIP, serverPort, clientPort);
		
		//send result
		
		redirectedRequests.remove(request.requestID);
		
		this.service.sendAnswerToClient(answer, ci);
	}
}
