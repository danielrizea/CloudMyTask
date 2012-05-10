package com.cloudmytask.service;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public interface CMTPrivateServiceInterface {
	
	//public void decodeRequest(byte[] request, CallbackInterface ci);
	
	public void testRequest(Request request, CallbackInterface ci);
	
	public void runScriptOnServver(Request request, String filename, CallbackInterface ci);
	//am nevoie aici de createServerScriptFile pentru a avea acces la ea din DecodeJob
	public void createServerScriptFile(Request request, CallbackInterface ci);
	
	public void jobHandOff(Request request, CallbackInterface ci);
	
	/*
	public void searchCachedResultRequest(Request req, CallbackInterface ci);
	
	public void computeGCDRequest(Request req, CallbackInterface ci);
	
	public void sendResultRequest(Integer result, CallbackInterface ci);
	
	public void cacheResultRequest(Request req, Integer result, CallbackInterface ci);
	*/
}
