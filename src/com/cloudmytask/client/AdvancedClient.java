package com.cloudmytask.client;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.tests.ApMonLog;
import com.cloudmytask.utils.CommunicationUtils;


public class AdvancedClient extends Thread {

	public String clientID;
	
	public ExecutorService requestPool;
	
	public String serviceInstanceIP = "localhost";
	
	public int serviceInstancePort ;
	
	public int localPort;
	
	public AdvancedClient(String clientID, String serviceIP, int servicePort, int localPort) {
		this.clientID = clientID;
		this.serviceInstanceIP = serviceIP;
		this.serviceInstancePort = servicePort;
		this.localPort = localPort;
		this.requestPool = Executors.newCachedThreadPool();
	}

	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	
	public void submitScriptForExecutionBlockOnWaitingResult(String scriptData, String scriptFileName){
		
		final String data = scriptData;
		final String filename = scriptFileName;
		final String instanceIP = this.serviceInstanceIP;
		final int instancePort = this.serviceInstancePort;
		Runnable taskRequest = new Runnable() {
			
			@Override
			public void run() {
				Request r;
				
				ApMonLog apm = ApMonLog.getInstance();
				
				// TODO Auto-generated method stub
				r = new Request("vreau sa-mi procesezi scriptul asta", Request.REQUEST_PROCESS_SCRIPT);			
				r.scriptFileData = data;
				r.scriptFileName = filename;	
				r.requestID = r.hashCode() + "_" + r + "_";
				r.clientID = clientID;
				//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
				
				long startTime = new Date().getTime();
				Request response = (Request) CommunicationUtils.sendRequestGetResponse(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, localPort);
				
				long stopTime = new Date().getTime();
				apm.logMessage("time_between_requests_5", 0,  (int)(stopTime-startTime));
				
				System.out.println("[Client + " + clientID+"] Response from serviceInstance http://"+ instanceIP +":" + instancePort + " message :" + response.message);
			
			}
		};
		
		requestPool.submit(taskRequest);
	}
	
	public void submitScriptForExecution(String scriptData, String scriptFileName){
		
		final String data = scriptData;
		final String filename = scriptFileName;
		final String instanceIP = this.serviceInstanceIP;
		final int instancePort = this.serviceInstancePort;

		Request r;
		// TODO Auto-generated method stub
		r = new Request("vreau sa-mi procesezi scriptul asta", Request.REQUEST_PROCESS_SCRIPT);			
		r.scriptFileData = data;
		r.scriptFileName = filename;	
		r.requestID = r.hashCode() + "_" + r + "_";
		r.clientID = clientID;
		//CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT + 0, 60001);
		CommunicationUtils.sendRequest(r, "localhost", GlobalConfig.CLIENT_COMM_PORT, localPort);
		//System.out.println("Response from serviceInstance at "+ instanceIP +": " + instancePort + " message :" + response.message);

	}
	
	
	
	public void stopThread(){
		try{
			requestPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
		}
		catch(Exception e){
			System.err.println("Error in stoping AdvancedClient " + e.getMessage());
		}
	}
	
	
}
