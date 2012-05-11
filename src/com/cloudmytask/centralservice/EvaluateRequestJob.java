package com.cloudmytask.centralservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class EvaluateRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private HashMap<String, Boolean> bannedList;
	private HashMap<String, Integer> loadList;


	public EvaluateRequestJob(CentralPrivateServiceInterface service, HashMap<String, Boolean> bannedList, HashMap<String, Integer> loadList,Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.bannedList = bannedList;
		this.loadList = loadList;
	}
	
	public void run() {
	
		
		//verificare tip de requesti si pasare mai departe
				
		try {
			
			// in functie de tipul de request se va face procesarea necesara
			switch(request.type)
			{
				case Request.R_IS_BANNED: service.processIsBannedRequest(request, bannedList, ci);
				case Request.R_ADD_BANNED: service.processAddToBannedRequest(request, bannedList, ci);
				case Request.R_UPDATE_STATUS: service.processUpdateStatusRequest(request, loadList, ci);
				case Request.R_GET_AVAILABLE: service.processGetAvailableRequest(request, loadList, ci);
			}
		}
		
		catch (Exception e) {
			System.out.println("exception occured" + e.getMessage());
			e.printStackTrace();		
		}
		
		
		
		//trimite la urmatoarea etapa
		//this.service.searchCachedResultRequest(req, ci);
	}
}
