package com.cloudmytask.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class RunScriptOnServerJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineInfo machineInfo;
	private String filename;
	private ConcurrentHashMap<String, Request> executingRequests;

	public RunScriptOnServerJob(CMTPrivateServiceInterface service,
			Request request, String filename, CallbackInterface ci,MachineInfo machineInfo,
			ConcurrentHashMap<String, Request> executingRequests) {
		this.service = service;
		this.request = request;
		this.filename = filename;
		this.ci = ci;
		this.machineInfo = machineInfo;
		this.executingRequests = executingRequests;
	}
	
	public void run() {
		
		//rulare script pe server
		executingRequests.put(request.requestID, request);

		   try {

			   //String[]callAndArgs= {"\"python\",\"my_python.py\",\"arg1\",\"arg2\"};
			   String command = "python " + filename ;

			 //  cmd /c c:\\path\\to\\python python\\test.py
			  // String comm = "dir ";
			 // System.out.println(command);

			 // System.out.println("Request " + request);
  
			  Process p = Runtime.getRuntime().exec(command);
	          BufferedReader stdInput = new BufferedReader(new 
	        		  InputStreamReader(p.getInputStream()));



	          BufferedReader stdError = new BufferedReader(new 

	                 InputStreamReader(p.getErrorStream()));

	          String executionResult = "";
	            // read the output
	          String s;

	          
	          while ((s = stdInput.readLine()) != null) {

	        	  	executionResult += s;
	        	  	//System.out.println("Output" + s);

	          }   

	            // read any errors

	            while ((s = stdError.readLine()) != null) {
	            	executionResult += s;
	                //System.out.println("Error" + s);

	            }

	            request.answer = executionResult;
	        }

	        catch (Exception e) {

	            System.out.println("exception occured" + e.getMessage());

	            e.printStackTrace();
	            
	        }
		
		request.message = "executed on service instance " + machineInfo.id;
		executingRequests.remove(request.requestID);
		
		
		//delete file at the end
		try{
			File file = new File(filename);
			file.delete();
		}catch(Exception e){
			System.out.println("[RunScriptOnServer] delete file exception" +  e.getMessage());
		}
		//trimite la urmatoarea etapa
		this.service.sendAnswerToClient(request, ci);
	}
}
