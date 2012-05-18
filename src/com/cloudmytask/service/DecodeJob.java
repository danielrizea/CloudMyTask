package com.cloudmytask.service;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.utils.DataUtils;

public class DecodeJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private byte[] request;
	private CallbackInterface ci;
	
	public DecodeJob(CMTPrivateServiceInterface service, byte[] request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
	}
	
	public void run() {
		Request req = (Request) DataUtils.decode(this.request);
		
		if (req.type == Request.REQUEST_PROCESS_SCRIPT) {
			//decide if job to be executed on this machine or on others based on load
			this.service.filterClients(req, ci);

			System.out.println("Request submited to service ");
		} else if (req.type == Request.REQUEST_GET_LOAD) {
			//get the machine load
			this.service.processMachineLoadRequest(req, ci);
		} else if (req.type == Request.REQUEST_PASS_SCRIPT) {
			
			this.service.decideMachineAvailable(req, ci);
		}
	}
}
