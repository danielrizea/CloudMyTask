package com.cloudmytask.service;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class HandOffJob implements Runnable {
	private CMTPrivateServiceInterface service;
	private Request request;
	private CallbackInterface ci;
	private MachineDescription md;


	public HandOffJob(CMTPrivateServiceInterface service, Request request, CallbackInterface ci, MachineDescription md) {
		this.service = service;
		this.request = request;
		this.ci = ci;
		this.md = md;
	}
	
	public void run() {
	
		
		//find machine to run code on// get id
		
		//send request lookup in neighbors and get connection port and IP
		
		
		
	}
}
