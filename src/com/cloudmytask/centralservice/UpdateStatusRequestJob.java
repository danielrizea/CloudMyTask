package com.cloudmytask.centralservice;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class UpdateStatusRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Integer> loadList;


	public UpdateStatusRequestJob(CentralPrivateServiceInterface service, ConcurrentHashMap<String, Integer> loadList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.loadList = loadList;
	}
	
	public void run() {
	
		try {
			// se modifica in lista de stari, situatia curenta in care se afla respectiva instanta
			Set<Map.Entry<String, Integer>> loadSet = loadList.entrySet();
			Map.Entry<String, Integer> mapEntry;
			try {
				// se face verificarea daca respectivul client se afla in lista de banned
				Iterator it = loadSet.iterator();  
				while(it.hasNext()) { 
					mapEntry = (Map.Entry<String, Integer>) it.next();
					if(mapEntry.getKey().equals(request.clientID))
					{
						// stergem informatia mai veche despre respectiva instanta
						loadList.remove(mapEntry);
						// adaugam informatia noua
						loadList.put(mapEntry.getKey(), request.state);
					}

				} 
			}
			
			catch (Exception e) {
				System.out.println("exception occured" + e.getMessage());
				e.printStackTrace();		
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