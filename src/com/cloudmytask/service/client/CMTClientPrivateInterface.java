package com.cloudmytask.service.client;

import com.cloudmytask.client.Request;

public interface CMTClientPrivateInterface {

	//return object response
	public Object sendTCPRequest(Request request, String serverIP, int serverPort);
	
	public Object sendUDPRequest(Request request, String serverIP, int serverPort, int clientPort);
	
	public Object sendNIOTCPRequest(Request request, String serverIP, int serverPort);
	
}
