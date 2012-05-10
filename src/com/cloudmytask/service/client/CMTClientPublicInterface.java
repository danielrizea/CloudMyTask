package com.cloudmytask.service.client;

import com.cloudmytask.client.Request;

public interface CMTClientPublicInterface {

	//return object response
	public Object sendRequest(Request request, String serverIP, int serverPort, int clientPort);
	
}
