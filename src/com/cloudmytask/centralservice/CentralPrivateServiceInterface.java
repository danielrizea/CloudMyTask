package com.cloudmytask.centralservice;

import java.io.IOException;
import java.util.HashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.client.TopologyRequest;
import com.cloudmytask.connectors.CallbackInterface;

public interface CentralPrivateServiceInterface {
	
	// se face procesarea requestului de verificare daca un client e in banned
	public void processIsBannedRequest(Request request, CallbackInterface ci);
	
	// se face procesarea req de adaugare a unui client in banned
	public void processAddToBannedRequest(Request request, CallbackInterface ci);
	
	// se face procesarea req de update a starii masinii
	public void processUpdateStatusRequest(Request request,  CallbackInterface ci);
	
	// se face procesarea req de verificare carui vecin i se poate pasa cererea 
	public void processGetAvailableRequest(Request request, CallbackInterface ci);
	
	
	// ************ MULTICAST
	public void sendTopology(TopologyRequest update) throws IOException;
	
}
