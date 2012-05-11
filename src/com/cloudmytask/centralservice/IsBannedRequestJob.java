package com.cloudmytask.centralservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class IsBannedRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private HashMap<String, Boolean> bannedList;


	public IsBannedRequestJob(CentralPrivateServiceInterface service, HashMap<String, Boolean> bannedList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.bannedList = bannedList;
	}
	
	public void run() {
		Set<Map.Entry<String, Boolean>> bannedSet = bannedList.entrySet();
		Map.Entry<String, Boolean> mapEntry;
		try {
			// se face verificarea daca respectivul client se afla in lista de banned
			Iterator it = bannedSet.iterator();  
			while(it.hasNext()) { 
				mapEntry = (Map.Entry<String, Boolean>) it.next();
				if(mapEntry.getKey().equals(request.clientID))
				{
					if(mapEntry.getValue() == true)
					{
						// este banned -> return true
						request.bannedInfo = true;
						ci.sendResult(request);
					}
					// ?????????????????????????
					else	// cam aiurea pentru ca nu e nevoie sa fie in lista decat daca e banned
					{
						// nu este banned -> return false
						request.bannedInfo = false;
						ci.sendResult(request);
					}
				}
				else
				{
					// nu este banned -> return false
					request.bannedInfo = false;
					ci.sendResult(request);					
				}
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
