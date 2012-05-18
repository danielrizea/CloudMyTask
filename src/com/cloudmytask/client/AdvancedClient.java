package com.cloudmytask.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AdvancedClient extends Thread {

	public String clientID;
	
	//content of the script, not script file name
	public String scriptDataFileContent;
	
	public ExecutorService requestPool;

	
	public AdvancedClient(String clientID) {
		this.clientID = clientID;
		
		requestPool = Executors.newCachedThreadPool();
	}

	public void run() {
		// TODO Auto-generated method stub
		super.run();
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
