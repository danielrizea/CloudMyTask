package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class CreateServerScriptJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;



	public CreateServerScriptJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.ci = ci;
	}
	
	public void run() {
	
		
		//creare fisier python
		
		String filename = "server_" + request.hashCode()+ "_"+request.scriptFileName;
		
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter(filename);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(request.scriptFileData);
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		//trimite la urmatoarea etapa
		this.service.runScriptOnServer(request, filename, ci);
	}
}
