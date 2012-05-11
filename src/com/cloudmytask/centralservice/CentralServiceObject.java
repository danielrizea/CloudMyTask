package com.cloudmytask.centralservice;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class CentralServiceObject implements CentralPublicServiceInterface, CentralPrivateServiceInterface {

	// un pool de thread-uri pentru a putea prelua cererile primite de la celelalte masini
	// un al 2lea pool de thread-uri pentru a procesa cererile primite
	private ExecutorService evaluateRequestsPool, processIsBannedRequestsPool, processAddToBannedRequestsPool; 
	private ExecutorService processGetAvailableRequestsPool, processUpdateStatusRequestsPool;
	
	// hashmap cu clientii ce trimit scripturi care sunt banned 
	private HashMap<String, Boolean> bannedList;
	private HashMap<String, Integer> loadList;
	
	
	public CentralServiceObject() {
//TODO -> mare grija cu poolurile de threaduri
		//cache service client
//TODO: getInterface details

		bannedList = new HashMap<String, Boolean>();
		loadList = new HashMap<String, Integer>();
		
		//TODO parametrizare
		this.evaluateRequestsPool = Executors.newFixedThreadPool(4);
		this.processIsBannedRequestsPool = Executors.newFixedThreadPool(4);
		this.processAddToBannedRequestsPool = Executors.newFixedThreadPool(4);
		this.processUpdateStatusRequestsPool = Executors.newFixedThreadPool(4);
		this.processGetAvailableRequestsPool = Executors.newFixedThreadPool(4);
		
	}
//TODO: nu mai avem nevoie
	// Metoda apelata la primirea unei cereri de la un client.
/*	public void computeGCDRequest(byte[] request, CallbackInterface ci) {
		System.out.println("[CMTerviceObject] S-a primit o cerere de la un client (ci=" + ci + ") => trimit cerere de decriptare");
		//this.decryptPool.submit(new DecryptJob(this, request, ci));
	}
	
	public void computeGCDRequest(Request request, CallbackInterface ci) {
		System.out.println("[CMTServiceObject] S-a primit o cerere de la un client (ci=" + ci + ") => trimit cerere de decriptare");
		//this.decryptPool.submit(new DecryptJob(this, request, ci));
	}
	

	public void testRequest(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		System.out.println("[CMTServiceObject] S-a primit o cerere de la un client (ci=" + ci + ") =>  cu mesajul " + request.message  + " req id" + request.type);
	}

	public void computeCMTRequest(byte[] request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
	}

	public void computeCMTRequest(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void createServerScriptFile(Request request,
			CallbackInterface ci) {
		
		System.out.println("[CMTServiceObject] S-a primit o cerere de creare script local (ci=" + ci + ")");
		
		this.createScriptFilePool.submit(new CreateServerScriptJob(this, request, ci));
		
	}
	

	public void runScriptOnServver(Request request, String filename,
			CallbackInterface ci) {

		System.out.println("[CMTServiceObject] S-a primit o cerere de executie script pe server   (ci=" + ci + ")");
	
		this.runScriptOnServerPool.submit(new RunScriptOnServerJob(this, request, filename, ci));
	}
	*/
	
//
	/*
	// Metode apelate de catre thread-urile din pool-uri.
	public void decodeRequest(byte[] request, CallbackInterface ci) {
		System.out.println("[GCDServiceObject] S-a primit o cerere de decodificare de la un thread de decriptare (ci=" + ci + ")");
		this.decodePool.submit(new DecodeJob(this, request, ci));
	}
//metode de interpretat: decide action

	
	
	public void searchCachedResultRequest(Request req, CallbackInterface ci) {
		System.out.println("[GCDServiceObject] S-a primit o cerere de cautare a unui rezultat cached de la un thread de decodificare (ci=" + ci + ")");		
		this.searchCachedResultPool.submit(new SearchCachedResultJob(this, req, this.csc, ci));
	}

	public void computeGCDRequest(Request req, CallbackInterface ci) {
		System.out.println("[GCDServiceObject] S-a primit o cerere de calcul al GCD-ului de la un thread de cautare a unui rezultat cached (req=" + req.numbers + "; ci=" + ci + ")");
		this.computeGCDPool.submit(new ComputeGCDJob(this, req, ci));
	}
	
	public void sendResultRequest(Integer result, CallbackInterface ci) {
		System.out.println("[GCDServiceObject] S-a primit o cerere de trimitere a rezultatului catre client (ci=" + ci + ")");				
		this.sendResultPool.submit(new SendResultJob(this, result, ci));
	}
	
	public void cacheResultRequest(Request req, Integer result, CallbackInterface ci) {
		System.out.println("[GCDServiceObject] S-a primit o cerere de cache-uire a rezultatului de la un thread de calcul al GCD-ului (result=" + result + "; ci=" + ci + ")");
		this.cacheResultPool.submit(new CacheResultJob(req, result, this.csc));		
	}

	*/
	
	
	public void sendRequestToCentralUnit(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
		// creaee thread pt job
		EvaluateRequestJob erj = new EvaluateRequestJob(this, bannedList, loadList, request, ci);
		
		this.evaluateRequestsPool.submit(erj);
	}

	public void processAddToBannedRequest(Request request, HashMap<String, Boolean> bannedList, CallbackInterface ci) {
		// TODO Auto-generated method stub
		AddToBannedRequestJob erj = new AddToBannedRequestJob(this, bannedList, request, ci);
		
		this.processAddToBannedRequestsPool.submit(erj);
	}

	public void processGetAvailableRequest(Request request, HashMap<String, Integer> loadList, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
		GetAvailableRequestJob erj = new GetAvailableRequestJob(this, loadList, request, ci);
		
		this.processGetAvailableRequestsPool.submit(erj);
	}

	public void processIsBannedRequest(Request request, HashMap<String, Boolean> bannedList, CallbackInterface ci) {
		// TODO Auto-generated method stub
		IsBannedRequestJob erj = new IsBannedRequestJob(this, bannedList, request, ci);
		
		this.processIsBannedRequestsPool.submit(erj);	
	}

	public void processUpdateStatusRequest(Request request, HashMap<String, Integer> loadList, CallbackInterface ci) {
		// TODO Auto-generated method stub
		UpdateStatusRequestJob erj = new UpdateStatusRequestJob(this, loadList, request, ci);
		
		this.processUpdateStatusRequestsPool.submit(erj);	
	}	
	
	
	// Metode de start si stop.
	public void start() {
		// Nothing to do here.
	}
	
	public void stop() {
	/*	this.decryptPool.shutdown();
		this.decodePool.shutdown();
		this.searchCachedResultPool.shutdown();
		this.computeGCDPool.shutdown();
		this.sendResultPool.shutdown();
		this.cacheResultPool.shutdown();
		
		try {
			this.decryptPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.decodePool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.searchCachedResultPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.computeGCDPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
			this.cacheResultPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			System.err.println("[GCDServiceObject] Eroare la awaitTermination: " + e);
			e.printStackTrace();
		}*/
	}

}
