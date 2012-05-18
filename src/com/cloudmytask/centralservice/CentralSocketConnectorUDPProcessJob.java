package com.cloudmytask.centralservice;

import java.util.concurrent.ExecutorService;

import com.cloudmytask.client.Request;
import com.cloudmytask.service.CMTPublicServiceInterface;

public class CentralSocketConnectorUDPProcessJob implements Runnable{

	private CentralPublicServiceInterface ssi;
	private CentralSocketConnectorUDPCallbackObject scco;
	
	// pool de threaduri care se ocupa de procesarea cererii
	private ExecutorService processPool;
	private Request request;
	public CentralSocketConnectorUDPProcessJob(CentralPublicServiceInterface ssi, CentralSocketConnectorUDPCallbackObject scco, Request request) {

		this.ssi = ssi;
		this.scco = scco;
		this.request = request;
		
	}
	@Override
	public void run() {
		
		// trimite cererea catre CUnit
		this.ssi.sendRequestToCentralUnit(request, scco);
	}

}
