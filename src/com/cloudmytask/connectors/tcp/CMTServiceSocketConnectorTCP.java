package com.cloudmytask.connectors.tcp;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cloudmytask.service.CMTPublicServiceInterface;

public class CMTServiceSocketConnectorTCP {
	private CMTPublicServiceInterface ssi;
	private int ports[];
	
	private ExecutorService receivePool;
	private ExecutorService processPool;
	
	public static int processThreadsInPool = 4;
	
	// 0 -> 1 thread per request
	// 1 - > use thread pool
	public static int behaviour = 0;
	
	
	public CMTServiceSocketConnectorTCP(CMTPublicServiceInterface ssi, int ports[]) {
		this.ssi = ssi;
		//pe ce porturi se creaza sockets
		this.ports = ports;
		
		// Creeaza un pool de thread-uri pe care se vor primi cererile (folosind socketi UDP).
		this.receivePool = Executors.newFixedThreadPool(ports.length);
		this.processPool = Executors.newFixedThreadPool(processThreadsInPool);
	}
	
	public void start() {
		for (int i = 0; i < ports.length; i++) {
			int port = ports[i];

			try {

				ServerSocket serverSocket = new ServerSocket(port);
				serverSocket.setSoTimeout(200000);
				
				SocketConnectorTCPReceiveJob rj = new SocketConnectorTCPReceiveJob(serverSocket, ssi, this, processPool);
				this.submitReceiveJob(rj);
				System.out.println("[CMTServiceSocketConnector] TCP Server socket pornit pe portul " + port);
			
			
			} catch (Exception e) {
				System.err.println("Exceptie la stabilire conexiune TCP: " + e);
				e.printStackTrace();
				return;
			}
		}
	}
	
	protected void submitReceiveJob(SocketConnectorTCPReceiveJob rj) {
		//tot timpul e un thread care asculta
		//cand se termina, se da submit 
		
		if (!this.receivePool.isShutdown()) {
			this.receivePool.submit(rj);
		}
	}
	
	public void stop() {
		this.receivePool.shutdown();		
	}


}
