package com.cloudmytask.centralservice;

import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class AddToBannedRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Boolean> bannedList;


	public AddToBannedRequestJob(CentralPrivateServiceInterface service, ConcurrentHashMap<String, Boolean> bannedList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.bannedList = bannedList;
	}
	
	public void run() {
	
		
		System.out.println("[CentralServiceInstance] add client to banned list " + request.clientID + " " + request.message);
		try {
			// se adauga la bannedList clientul respectiv
			//!!!! consider ca in message vor fi date informatii extra pentru cereri 
			bannedList.put(request.clientID, true);
			request.message = "client added to banned list";
			ci.sendResult(request);
		}
		
		catch (Exception e) {
			System.out.println("exception occured" + e.getMessage());
			e.printStackTrace();		
		}
		
		
		
		//trimite la urmatoarea etapa
		//this.service.searchCachedResultRequest(req, ci);
	}
}