package com.cloudmytask.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cloudmytask.centralservice.CentralPublicServiceInterface;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.client.CMTClientPublicInterface;

public class CMTServiceObject implements CMTPublicServiceInterface, CMTPrivateServiceInterface {
	
	private ExecutorService createScriptFilePool, runScriptOnServerPool, discoverFreeNeighbotPool, jobHandOffPool, decideMachineAvailablePool, processMachineLoadRequestPool, sendResultPool, decodePool, clientFilterPool;
	
	private CMTClientPublicInterface clientObjectInterface;
	
	private CentralPublicServiceInterface centralInterface;
	
	private MachineInfo machineDescription;
	
	// execution on local machine
	private ConcurrentHashMap<String,Request> requestsInExecution;
	
	//execution on distributed machine in cloud
	private ConcurrentHashMap<String,Request> requestsWaitingAnswer;
	
	private ConcurrentHashMap<String,Request> requestsProcessed;
	
	private ConcurrentHashMap<String, Integer> clientRequestsInPeriod;
	
	private ClearRequestsThreadForPeriod clearRequestsThread;
	
	public String LOGTag;
	
	
	
	public CMTServiceObject(CMTClientPublicInterface clientObjectInterface, MachineInfo machineDescription, CentralPublicServiceInterface centralInterface) {


		this.decodePool = Executors.newFixedThreadPool(3);
		this.sendResultPool = Executors.newFixedThreadPool(2);

		
		//TODO parametrizare
		this.createScriptFilePool = Executors.newFixedThreadPool(4);
		//max jobs = number of threads available
		this.runScriptOnServerPool = Executors.newFixedThreadPool(machineDescription.getMaxJobsInExecution());
		this.jobHandOffPool = Executors.newFixedThreadPool(4);
		this.discoverFreeNeighbotPool = Executors.newFixedThreadPool(4);
		this.decideMachineAvailablePool = Executors.newFixedThreadPool(4);
		this.processMachineLoadRequestPool = Executors.newFixedThreadPool(4);
		this.clientFilterPool = Executors.newFixedThreadPool(4);
		
		//interfaces needed
		this.clientObjectInterface = clientObjectInterface;
		this.machineDescription = machineDescription;
		this.centralInterface = centralInterface;
		//queue's to store state
		
		//requests that are currently executing
		this.requestsInExecution = new ConcurrentHashMap<String, Request>();
		//requests that have been passed to other service instances
		this.requestsWaitingAnswer = new ConcurrentHashMap<String, Request>();
		//requests that have been processed and the answer is stored
		this.requestsProcessed = new ConcurrentHashMap<String, Request>();
		// clients that have submitted requests
		this.clientRequestsInPeriod = new ConcurrentHashMap<String, Integer>();
		
		//thread that every period empties clientRequest
		clearRequestsThread = new ClearRequestsThreadForPeriod(clientRequestsInPeriod);
		
		this.LOGTag = "[CMTServiceObjectInstance "+machineDescription.id+"]";
	}
	

	public void decodeRequest(byte[] request, CallbackInterface ci) {

		System.out.println(LOGTag + "S-a primit o cerere de decode (ci=" + ci + ") => trimit cerere de decode");
		
		this.decodePool.submit(new DecodeJob(this, request, ci));
		
	}
	
	public void filterClients(Request request,CallbackInterface ci){
		
		this.clientFilterPool.submit(new FilterClientsJob(this, request, ci, clientObjectInterface, machineDescription, clientRequestsInPeriod));
	}
	
	public void decideMachineAvailable(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		this.decideMachineAvailablePool.submit(new AvailableMachineJob(this, request, ci, machineDescription, requestsInExecution, clientRequestsInPeriod));
	}
	
	
	public void createServerScriptFile(Request request,
			CallbackInterface ci) {
		
		System.out.println(LOGTag + " S-a primit o cerere de creare script local (ci=" + ci + ") request" + request);
		
		this.createScriptFilePool.submit(new CreateServerScriptJob(this, request, ci));
		
	}
	

	public void runScriptOnServer(Request request, String filename,
			CallbackInterface ci) {

		System.out.println(LOGTag + " S-a primit o cerere de executie script pe server   (ci=" + ci + ") +request" + request);
	
		this.runScriptOnServerPool.submit(new RunScriptOnServerJob(this, request, filename, ci,machineDescription, requestsInExecution));
	}


	public void getFreeNeighbor(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		this.discoverFreeNeighbotPool.submit(new DiscoverFreeNeighborJob(this, request, ci, clientObjectInterface, machineDescription));
	}
	
	public void sendAnswerToClient(Request request, CallbackInterface ci){
		
		this.sendResultPool.submit(new SendAnswerToClientJob(this,request,ci,requestsProcessed));
	}
	
	public void jobHandOff(Request request, CallbackInterface ci, int machineID) {
		// TODO Auto-generated method stub
		System.out.println(LOGTag + " start job hand-off");
		
		this.jobHandOffPool.submit(new HandOffJob(this, request, ci, clientObjectInterface, machineDescription, machineID, requestsWaitingAnswer));
	}
	

	public void processMachineLoadRequest(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
		this.processMachineLoadRequestPool.submit(new ProcessMachineLoadRequestJob(this, request, ci, machineDescription, requestsInExecution));
	}
	
	// Metode de start si stop.
	public void start() {
		// Nothing to do here.
		clearRequestsThread.start();

	}
	

	public void stop() {
		
		clearRequestsThread.stopThread();
		
		this.decodePool.shutdown();
		this.sendResultPool.shutdown(); 
		this.createScriptFilePool.shutdown();
		this.runScriptOnServerPool.shutdown();  
		this.jobHandOffPool.shutdown(); 
		this.discoverFreeNeighbotPool.shutdown(); 
		this.decideMachineAvailablePool.shutdown(); 
		this.processMachineLoadRequestPool.shutdown();
		
		try {
			this.decodePool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.sendResultPool.awaitTermination(100000, TimeUnit.MILLISECONDS); 
			this.createScriptFilePool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.runScriptOnServerPool.awaitTermination(100000, TimeUnit.MILLISECONDS);  
			this.jobHandOffPool.awaitTermination(100000, TimeUnit.MILLISECONDS); 
			this.discoverFreeNeighbotPool.awaitTermination(100000, TimeUnit.MILLISECONDS); 
			this.decideMachineAvailablePool.awaitTermination(100000, TimeUnit.MILLISECONDS); 
			this.processMachineLoadRequestPool.awaitTermination(100000, TimeUnit.MILLISECONDS);

		} catch (Exception e) {
			System.err.println("[GCDServiceObject] Eroare la awaitTermination: " + e);
			e.printStackTrace();
		}
	}

}
