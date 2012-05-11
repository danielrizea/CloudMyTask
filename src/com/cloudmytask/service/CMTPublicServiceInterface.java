package com.cloudmytask.service;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public interface CMTPublicServiceInterface {
	
	public void filterClients(Request request, CallbackInterface ci);
	
	public void decodeRequest(byte[] request, CallbackInterface ci);
		
	public void decideMachineAvailable(Request request, CallbackInterface ci);
	
	// prima metoda pentru intrare in servviciu in cazul UDP si TCP
	public void createServerScriptFile(Request request, CallbackInterface ci);
	
	public void processMachineLoadRequest(Request request, CallbackInterface ci);
}
