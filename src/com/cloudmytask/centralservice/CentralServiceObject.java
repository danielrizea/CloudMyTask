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
import com.cloudmytask.client.TopologyRequest;
import com.cloudmytask.connectors.CallbackInterface;
import com.cloudmytask.service.MulticastGroup;

public class CentralServiceObject implements CentralPublicServiceInterface, CentralPrivateServiceInterface {

	// un pool de thread-uri pentru a putea prelua cererile primite de la celelalte masini
	// alte pool-uri de thread-uri pentru a procesa cererile primite
	private ExecutorService evaluateRequestsPool, processIsBannedRequestsPool, processAddToBannedRequestsPool; 
	private ExecutorService processGetAvailableRequestsPool, processUpdateStatusRequestsPool;
	
	// hashmap cu clientii ce trimit scripturi care sunt banned 
	private ConcurrentHashMap<String, Boolean> bannedList;
	private ConcurrentHashMap<String, Integer> loadList;
	
	// MULTICAST handler + thread care se ocupa de informarea modificarilor in topologie 
	private MulticastServerHandler multicastHandler = null;
	private TopologyChangeThread topologyChange = null;
	
	public CentralServiceObject() {

		bannedList = new ConcurrentHashMap<String, Boolean>();
		loadList = new ConcurrentHashMap<String, Integer>();
		

		this.evaluateRequestsPool = Executors.newFixedThreadPool(GlobalConfig.NRTHREADS_CENTRALSERVICE);
		this.processIsBannedRequestsPool = Executors.newFixedThreadPool(GlobalConfig.NRTHREADS_CENTRALSERVICE);
		this.processAddToBannedRequestsPool = Executors.newFixedThreadPool(GlobalConfig.NRTHREADS_CENTRALSERVICE);
		this.processUpdateStatusRequestsPool = Executors.newFixedThreadPool(GlobalConfig.NRTHREADS_CENTRALSERVICE);
		this.processGetAvailableRequestsPool = Executors.newFixedThreadPool(GlobalConfig.NRTHREADS_CENTRALSERVICE);
		
		// MULTICAST - creare grup de multicast
		try {	
			MulticastGroup group = new MulticastGroup(GlobalConfig.MulticastAddress, GlobalConfig.MulticastPort);
			this.multicastHandler = new MulticastServerHandler(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//create topology change watch
		try{
			topologyChange = new TopologyChangeThread(this);
			topologyChange.startRunning();			
		}catch(Exception e){
			System.out.println("[CentralServiceInstance] error in starting topology changed thread lisener " + e.getMessage());
		}
	}
	
	// functie apelata de restul instantelor pentru a trimite cerere la CUnit
	public void sendRequestToCentralUnit(Request request, CallbackInterface ci) {
		
		// thread pt job de tip EvaluateRequest (pool 1)
		EvaluateRequestJob erj = new EvaluateRequestJob(this, bannedList, loadList, request, ci);
		
		this.evaluateRequestsPool.submit(erj);
	}

	// functie apelata pentru a adauga un client in banned list
	public void processAddToBannedRequest(Request request,  CallbackInterface ci) {
		
		// thread pentru job de tip AddToBanned (pool 2)
		AddToBannedRequestJob erj = new AddToBannedRequestJob(this, bannedList, request, ci);
		
		this.processAddToBannedRequestsPool.submit(erj);
	}

	// DEPRICATED ? functie apelata pentru a afla ce vecini sunt available
	public void processGetAvailableRequest(Request request,  CallbackInterface ci) {
		
		GetAvailableRequestJob erj = new GetAvailableRequestJob(this, loadList, request, ci);
		
		this.processGetAvailableRequestsPool.submit(erj);
	}

	// functie apelata pentru a verifica daca un client este in banned list
	public void processIsBannedRequest(Request request, CallbackInterface ci) {
	
		// thread pentru job de tip IsBanned (pool 2)
		IsBannedRequestJob erj = new IsBannedRequestJob(this, bannedList, request, ci);
		
		this.processIsBannedRequestsPool.submit(erj);	
	}

	// functie apelata pentru a anunta modificarile din statusul unei masini (cat e nivelul load)
	public void processUpdateStatusRequest(Request request, CallbackInterface ci) {
		
		// thread pentru job de tip UpdateStatus (pool 2)
		UpdateStatusRequestJob erj = new UpdateStatusRequestJob(this, loadList, request, ci);
		
		this.processUpdateStatusRequestsPool.submit(erj);	
	}	
	
	
	// Metode de start si stop.
	public void start() {
		// Nothing to do here.
	}
	
	public void stop() {
		// Nothing to do here.
	}

	
	// functie apelata pentru trimiterea topologiei catre celelalte instante de catre masina centrala
	// functie apelata din TopologyChangeThread
	public void sendTopology(TopologyRequest update) throws IOException {

		multicastHandler.sendPacket(update);
	}

}
