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
		// TODO Auto-generated method stub
		
		
		if (request.type == 1) {
			//StreamObject sobj = request.sobj;
			//upload
			//this.ssi.uploadStreamObject(sobj.name, sobj, scco);
			//this.ssi.testRequest(request, scco);
			this.ssi.createServerScriptFile(request, scco);
			
			System.out.println("Request submited to service ");
		} else if (request.type == 2) {
			//getList
			//this.ssi.getStreamObjectList(scco,request.clientId);
		} else if (request.type == 3) {
			//getFrame
			//this.ssi.getStreamObjectFrames(request.name, scco,request.clientId);
		}
		
	}

}
