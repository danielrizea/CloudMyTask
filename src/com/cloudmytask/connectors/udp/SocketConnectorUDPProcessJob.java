package com.cloudmytask.connectors.udp;

import java.util.concurrent.ExecutorService;

import com.cloudmytask.client.Request;
import com.cloudmytask.service.CMTPublicServiceInterface;

public class SocketConnectorUDPProcessJob implements Runnable{

	private CMTPublicServiceInterface ssi;
	private SocketConnectorUDPCallbackObject scco;
	
	// pool de threaduri care se ocupa de procesarea cererii
	private ExecutorService processPool;
	private Request request;
	public SocketConnectorUDPProcessJob(CMTPublicServiceInterface ssi, SocketConnectorUDPCallbackObject scco, Request request) {

		this.ssi = ssi;
		this.scco = scco;
		this.request = request;
		
	}
	@Override
	public void run() {
		
		if (request.type == Request.REQUEST_PROCESS_SCRIPT) {
			//decide if job to be executed on this machine or on others based on load
			this.ssi.filterClients(request, scco);
			
			System.out.println("Request submited to service ");
		} else if (request.type == Request.REQUEST_GET_LOAD) {
			//get the machine load
			this.ssi.processMachineLoadRequest(request, scco);
		} else if (request.type == Request.REQUEST_PASS_SCRIPT) {
			
			this.ssi.decideMachineAvailable(request, scco);
		}
		
	}

}
