package com.cloudmytask.centralservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class AddToBannedRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	HashMap<String, Boolean> bannedList;


	public AddToBannedRequestJob(CentralPrivateServiceInterface service, HashMap<String, Boolean> bannedList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.bannedList = bannedList;
	}
	
	public void run() {
	
		try {
			// se adauga la bannedList clientul respectiv
			//!!!! consider ca in message vor fi date informatii extra pentru cereri 
			bannedList.put(request.message, true);
			
		}
		
		catch (Exception e) {
			System.out.println("exception occured" + e.getMessage());
			e.printStackTrace();		
		}
		
		
		
		//trimite la urmatoarea etapa
		//this.service.searchCachedResultRequest(req, ci);
	}
}