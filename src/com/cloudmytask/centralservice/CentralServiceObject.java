package com.cloudmytask.centralservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.CallbackInterface;

public class CentralServiceObject implements CentralPublicServiceInterface, CentralPrivateServiceInterface {

	// un pool de thread-uri pentru a putea prelua cererile primite de la celelalte masini
	// un al 2lea pool de thread-uri pentru a procesa cererile primite
	private ExecutorService evaluateRequestsPool, processIsBannedRequestsPool, processAddToBannedRequestsPool; 
	private ExecutorService processGetAvailableRequestsPool, processUpdateStatusRequestsPool;
	
	// hashmap cu clientii ce trimit scripturi care sunt banned 
	private ConcurrentHashMap<String, Boolean> bannedList;
	private ConcurrentHashMap<String, Integer> loadList;
	
	// **** Multicast stuff
	private MulticastServerHandler multicastHandler = null;
	
	
	public CentralServiceObject() {

		bannedList = new ConcurrentHashMap<String, Boolean>();
		loadList = new ConcurrentHashMap<String, Integer>();
		
		//TODO parametrizare
		this.evaluateRequestsPool = Executors.newFixedThreadPool(4);
		this.processIsBannedRequestsPool = Executors.newFixedThreadPool(4);
		this.processAddToBannedRequestsPool = Executors.newFixedThreadPool(4);
		this.processUpdateStatusRequestsPool = Executors.newFixedThreadPool(4);
		this.processGetAvailableRequestsPool = Executors.newFixedThreadPool(4);
		// ***** Multicast
		try {
			this.multicastHandler = new MulticastServerHandler(InetAddress.getByName("230.0.0.1"), 4000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendRequestToCentralUnit(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		
		// creaee thread pt job
		EvaluateRequestJob erj = new EvaluateRequestJob(this, bannedList, loadList, request, ci);
		
		this.evaluateRequestsPool.submit(erj);
	}

	public void processAddToBannedRequest(Request request,  CallbackInterface ci) {
		// TODO Auto-generated method stub
		AddToBannedRequestJob erj = new AddToBannedRequestJob(this, bannedList, request, ci);
		
		this.processAddToBannedRequestsPool.submit(erj);
	}

	public void processGetAvailableRequest(Request request,  CallbackInterface ci) {
		// TODO Auto-generated method stub
		
		GetAvailableRequestJob erj = new GetAvailableRequestJob(this, loadList, request, ci);
		
		this.processGetAvailableRequestsPool.submit(erj);
	}

	public void processIsBannedRequest(Request request, CallbackInterface ci) {
		// TODO Auto-generated method stub
		IsBannedRequestJob erj = new IsBannedRequestJob(this, bannedList, request, ci);
		
		this.processIsBannedRequestsPool.submit(erj);	
	}

	public void processUpdateStatusRequest(Request request, CallbackInterface ci) {
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

	
	// convert to byte array
	public static byte[] getBytes(Object obj) throws java.io.IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		ObjectOutputStream oos = new ObjectOutputStream(bos); 
		oos.writeObject(obj);
		oos.flush(); 
		oos.close(); 
		bos.close();
		byte [] data = bos.toByteArray();
		return data;
	  }
	
	public void sendTopology() throws IOException {
		
		// trimitere topologie catre instantele clienti
		multicastHandler.sendPacket(getBytes(GlobalConfig.connections));
	}

}
