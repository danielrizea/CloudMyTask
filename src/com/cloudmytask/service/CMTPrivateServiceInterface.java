package com.cloudmytask.service;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public interface CMTPrivateServiceInterface {
	
	//public void decodeRequest(byte[] request, CallbackInterface ci);
	
	public void decideMachineAvailable(Request request,CallbackInterface ci);
	
	//am nevoie aici de createServerScriptFile pentru a avea acces la ea din DecodeJob
	public void createServerScriptFile(Request request, CallbackInterface ci);

	public void runScriptOnServer(Request request, String filename, CallbackInterface ci);
	
	public void getFreeNeighbor(Request request, CallbackInterface ci);
	
	public void jobHandOff(Request request, CallbackInterface ci, int machineID);
	
	public void sendAnswerToClient(Request request, CallbackInterface ci);

}
