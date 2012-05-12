package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;
import com.cloudmytask.utils.CommunicationUtils;

public class FilterClientsJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineInfo;
	private CMTClientPublicInterface clientInterface;
	private ConcurrentHashMap<String, Integer> clientsRequests;
	
	private static int port_id = 0;
	
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
		
		Request isBann = new Request("is client banned", Request.R_IS_BANNED);
		isBann.clientID = request.clientID;

		int clientPort = 6500 + machineInfo.id + port_id;
		port_id++;
		
		Request response = (Request)CommunicationUtils.sendUDPRequestGetResponse(isBann, GlobalConfig.CENTRAL_UNIT_IP, GlobalConfig.CENTRAL_UNIT_PORT, clientPort);
		
		if(response.bannedInfo == false){
		
			
			if(clientsRequests.containsKey(request.clientID)){
				
				int jobsInTheLastPeriod = clientsRequests.get(request.clientID);
				
				if(jobsInTheLastPeriod >= GlobalConfig.MAX_REQUESTS_ALLOWED_IN_PERIOD){
					Request answer = new Request("user maximum scripts allowed in " + GlobalConfig.MAX_REQUEST_PERIOD/1000 + " seconds", Request.REQUEST_PROCESS_SCRIPT);
					answer.requestID = answer.hashCode() + "_answer";
					System.out.println("[CMTServiceObject "+machineInfo.id+"] filter client " + request.clientID);
					this.service.sendAnswerToClient(answer, ci);
					
					Request addBann = new Request("add client to bann list", Request.R_ADD_BANNED);
					
					addBann.clientID = request.clientID;
					clientPort = 5500 + machineInfo.id + port_id;
					port_id++;
					response = (Request)CommunicationUtils.sendUDPRequestGetResponse(addBann, GlobalConfig.CENTRAL_UNIT_IP, GlobalConfig.CENTRAL_UNIT_PORT, clientPort);
					
				}
				else{
					this.service.decideMachineAvailable(request, ci);
				}
			}
			else
				this.service.decideMachineAvailable(request, ci);
			
		}
		else
		{
			Request respone = new Request("you are banned", Request.REQUEST_PROCESS_SCRIPT);
			ci.sendResult(response);
		}
	}
}
