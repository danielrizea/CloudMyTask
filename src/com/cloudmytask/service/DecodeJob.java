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
		
		this.service.decideMachineAvailable(req, ci);
	}
}
