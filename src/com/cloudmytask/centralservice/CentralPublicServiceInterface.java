package com.cloudmytask.centralservice;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public interface CentralPublicServiceInterface {
	
	//public void computeCentralRequest(byte[] request, CallbackInterface ci);

	// functie de trimiterea a requestului catre masina centrala
	public void sendRequestToCentralUnit(Request request, CallbackInterface ci);
	
//	public void testRequest(Request request, CallbackInterface ci);
	
	// prima metoda pentru intrare in servviciu in cazul UDP si TCP
	//public void createServerScriptFile(Request request, CallbackInterface ci);
}
