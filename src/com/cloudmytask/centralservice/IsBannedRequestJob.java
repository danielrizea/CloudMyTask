package com.cloudmytask.centralservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class IsBannedRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Boolean> bannedList;


	public IsBannedRequestJob(CentralPrivateServiceInterface service, ConcurrentHashMap<String, Boolean> bannedList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.bannedList = bannedList;
	}
	
	public void run() {

		try {

			System.out.println("[CentralServiceInstance] client " + request.clientID + " is " + bannedList.containsKey(request.clientID) + " " + request.message);
				if(bannedList.containsKey(request.clientID)){
						request.bannedInfo = true;
						ci.sendResult(request);
				}else{
					// nu este banned -> return false
					request.bannedInfo = false;
					ci.sendResult(request);					
				}
				System.out.println("[CentralServiceInstance] after client " + request.clientID + " is " + bannedList.containsKey(request.clientID));
		}
		
		catch (Exception e) {
			System.out.println("exception occured" + e.getMessage());
			e.printStackTrace();		
		}
		
		
		
		//trimite la urmatoarea etapa
		//this.service.searchCachedResultRequest(req, ci);
	}
}
