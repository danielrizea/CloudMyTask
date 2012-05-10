package com.cloudmytask.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class RunScriptOnServerJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private String filename;


	public RunScriptOnServerJob(CMTPrivateServiceInterface service, Request request,String filename, CallbackInterface ci) {
		this.service = service;
		this.request = request;
		this.filename = filename;
		this.ci = ci;
	}
	
	public void run() {
	
		
		//rulare script pe server
		
		   try {



			   //String[]callAndArgs= {"\"python\",\"my_python.py\",\"arg1\",\"arg2\"};
//			   String command = "cmd /c C:\\Python32\\python.exe " + filename ;
			   String command = "python " + filename ;

			 //  cmd /c c:\\path\\to\\python python\\test.py
			  // String comm = "dir ";
			   System.out.println(command);

			  Process p = Runtime.getRuntime().exec(command);

	            

	            BufferedReader stdInput = new BufferedReader(new 

	                 InputStreamReader(p.getInputStream()));



	            BufferedReader stdError = new BufferedReader(new 

	                 InputStreamReader(p.getErrorStream()));

	            // read the output
	            String s;
	            
	            while ((s = stdInput.readLine()) != null) {

	                System.out.println("Output" + s);

	            }   

	            // read any errors

	            while ((s = stdError.readLine()) != null) {

	                System.out.println("Error" + s);

	            }

	        }

	        catch (Exception e) {

	            System.out.println("exception occured" + e.getMessage());

	            e.printStackTrace();

	        }
		System.out.println("Send result to write level");
		ci.sendResult(request);

		
		
		//delete file at the end
		try{
			File file = new File(filename);
			file.delete();
		}catch(Exception e){
			System.out.println("[RunScriptOnServer] delete file exception" +  e.getMessage());
		}
		//trimite la urmatoarea etapa
		//this.service.searchCachedResultRequest(req, ci);
	}
}
