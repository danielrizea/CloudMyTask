package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.GlobalConfig;

public class ClearRequestsThreadForPeriod extends Thread{

	private ConcurrentHashMap<String, Integer> clientsRequests;
	
	
	private boolean run = true;
	
	public ClearRequestsThreadForPeriod(ConcurrentHashMap<String, Integer> clientsRequests) {
		this.clientsRequests = clientsRequests;
	}

	public void stopThread(){
		run = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(run){
		
			//System.out.println("[CMTServiceObject] clean list" );
			clientsRequests.clear();
			try{
				Thread.sleep(GlobalConfig.MAX_REQUEST_PERIOD);
			}catch(Exception e){
				System.out.println("Exception THREAD sleep " + e.getMessage());
			}
		}
		
	}
}
