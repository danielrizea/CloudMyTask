package com.cloudmytask.service;

import java.util.Arrays;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class DiscoverFreeNeighborJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineDescription;
	private CMTClientPublicInterface clientInterface;
	public DiscoverFreeNeighborJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, CMTClientPublicInterface clientInterface, MachineInfo machineDescription) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.machineDescription = machineDescription;
		this.clientInterface = clientInterface;
	}
	
	public void run() {
	
	
		//send load request to all machines
		float[] load = new float[machineDescription.neighbours.length];
		
		for(int i=0;i<load.length;i++)
			load[i] = 100;
		
		Request requestLoad = new Request("get load", Request.REQUEST_GET_LOAD);
		
		for(int i=0;i<machineDescription.neighbours.length;i++){
			
			if(machineDescription.neighbours[i] == 1){
				String serverIP = GlobalConfig.machineIPs[i];
				int serverPort = GlobalConfig.INSTANCE_COMM_PORT + i;
				int clientPort = GlobalConfig.MACHINE_LOCAL_PORT + i;
			
				Request answer = (Request)clientInterface.sendRequest(requestLoad, serverIP, serverPort, clientPort);
				load[i] = answer.loadFactor;
				System.out.println("[CMTServiceObjectInstance "+machineDescription.id+"] load received from " +i + " " + answer.loadFactor);
			}
		}
		
		// 0% - 100% load possible values 
		float min=100;
		int poz=-1;
		
		for(int i=0;i<load.length;i++){
			if(load[i]<min){
				min = load[i];
				poz = i;
			}
		}
		
		System.out.println("[CMTServiceObjectInstance "+machineDescription.id+"] DiscoverFreeNeighbor send to " + poz + " neighborsLoad " + Arrays.toString(load) );
		
		this.service.jobHandOff(request, ci, poz);
		
	}
}
