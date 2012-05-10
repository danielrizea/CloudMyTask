package com.cloudmytask.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class CMTServiceObject implements CMTPublicServiceInterface, CMTPrivateServiceInterface {
	
	private ExecutorService decryptPool, decodePool, searchCachedResultPool, computeGCDPool, sendResultPool, cacheResultPool;

	private ExecutorService createScriptFilePool, runScriptOnServerPool;
	
	
	
	public CMTServiceObject() {
//TODO -> mare grija cu poolurile de threaduri
		//cache service client
//TODO: getInterface details

		this.decryptPool = Executors.newFixedThreadPool(2);
		this.decodePool = Executors.newFixedThreadPool(3);
		this.searchCachedResultPool = Executors.newFixedThreadPool(1);
		this.computeGCDPool = Executors.newFixedThreadPool(3);
		this.sendResultPool = Executors.newFixedThreadPool(2);
		this.cacheResultPool = Executors.newFixedThreadPool(1);
		
		//TODO parametrizare
		this.createScriptFilePool = Executors.newFixedThreadPool(4);
		this.runScriptOnServerPool = Executors.newFixedThreadPool(4);
		
	}
	

	public void decodeRequest(byte[] request, CallbackInterface ci) {

		System.out.println("[CMTerviceObject] S-a primit o cerere de decode (ci=" + ci + ") => trimit cerere de decode");
		
		this.decodePool.submit(new DecodeJob(this, request, ci));
		
	}

	
//TODO: nu mai avem nevoie
	// Metoda apelata la primirea unei cereri de la un client.
	public void computeGCDRequest(byte[] request, CallbackInterface ci) {
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
	
	// Metode de start si stop.
	public void start() {
		// Nothing to do here.
	}
	
	public void stop() {
		this.decryptPool.shutdown();
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
		}
	}


}
