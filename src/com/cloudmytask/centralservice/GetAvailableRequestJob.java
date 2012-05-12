package com.cloudmytask.centralservice;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class GetAvailableRequestJob implements Runnable {
	private CentralPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private ConcurrentHashMap<String, Integer> loadList;

	public GetAvailableRequestJob(CentralPrivateServiceInterface service, ConcurrentHashMap<String, Integer> loadList, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.loadList = loadList;
	}
	
	public void run() {
		Set<Map.Entry<String, int[]>> neighborsSet = request.neighbours.entrySet();
		Map.Entry<String, int[]> mapEntry;
		
		Set<Map.Entry<String, Integer>> loadSet = loadList.entrySet();
		Map.Entry<String, Integer> mapEntryAux;
		try {
			// verifica cine este vecinul cu gradul de availability cel mai crescut
			Integer minim = Integer.MAX_VALUE;
			
			Iterator it = neighborsSet.iterator();  
			while(it.hasNext()) { 
				mapEntry = (Map.Entry<String, int[]>) it.next();
				
				// daca printre cele cu informatii on load exista si cea de la vecinul respectiv				
				Iterator itaux = loadSet.iterator();
				while(itaux.hasNext())
				{
					mapEntryAux = (Map.Entry<String, Integer>) itaux.next();
					if(mapEntry.getKey().equals(mapEntryAux.getKey()))
					{
						if(minim > mapEntryAux.getValue())
						{
							// retinem noul minim
							minim = mapEntryAux.getValue();
							//retinem id-ul vecinului care e cel mai putin ocupat
							request.neighborID = mapEntryAux.getKey();
						}
					}
				}
			} 

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