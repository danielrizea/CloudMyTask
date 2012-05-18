package com.cloudmytask.centralservice;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public interface CentralPublicServiceInterface {
	
	// functie de trimiterea a requestului catre masina centrala
	public void sendRequestToCentralUnit(Request request, CallbackInterface ci);
	
}
