package com.cloudmytask.connectors.tcp;

import java.util.concurrent.ExecutorService;

import com.cloudmytask.client.Request;
import com.cloudmytask.service.CMTPublicServiceInterface;

public class SocketConnectorTCPProcessJob implements Runnable{

	private CMTPublicServiceInterface ssi;
	private SocketConnectorTCPCallbackObject scco;
	

	private Request request;
	public SocketConnectorTCPProcessJob(CMTPublicServiceInterface ssi, SocketConnectorTCPCallbackObject scco, Request request) {

		this.ssi = ssi;
		this.scco = scco;
		this.request = request;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		if (request.type == Request.REQUEST_PROCESS_SCRIPT) {
			//decide if job to be executed on this machine or on others based on load
			this.ssi.decideMachineAvailable(request, scco);
			
			System.out.println("Request submited to service ");
		} else if (request.type == Request.REQUEST_GET_LOAD) {
			//get the machine load
			this.ssi.processMachineLoadRequest(request, scco);
		} else if (request.type == 3) {
			//getFrame
			//this.ssi.getStreamObjectFrames(request.name, scco,request.clientId);
		}
		
	}

}
