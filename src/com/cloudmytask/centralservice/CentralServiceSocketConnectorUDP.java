package com.cloudmytask.centralservice;

import java.net.*;
import java.util.concurrent.*;

import com.cloudmytask.service.CMTPublicServiceInterface;

public class CentralServiceSocketConnectorUDP {
	private CentralPublicServiceInterface ssi;
	private int ports[];
	
	private ExecutorService receivePool;
	private ExecutorService processPool;
	
	//*** PARAMETRIZAT
	public static int processThreadsInPool = 10;
	
	// 0 -> 1 thread per request
	// 1 - > use thread pool
	//*** PARAMETRIZAT
	public static int behaviour = 0;
	
	
	public CentralServiceSocketConnectorUDP(CentralPublicServiceInterface  ssi, int ports[]) {
		this.ssi = ssi;
		//pe ce porturi se creaza sockets
		this.ports = ports;
		
		// Creeaza un pool de thread-uri pe care se vor primi cererile (folosind socketi UDP).
		// pool initial pentru primirea cererilor - threaduri ce se ocupa de cereri
		this.receivePool = Executors.newFixedThreadPool(ports.length);
		// pool pentru procesarea cererilor
		this.processPool = Executors.newFixedThreadPool(processThreadsInPool);
	}
	
	public void start() {
		for (int i = 0; i < ports.length; i++) {
			int port = ports[i];

			try {
				// pe fiecare port se creaza un DatagramSocket: fol pentru primit/trimis pachete
				DatagramSocket serverSocket = new DatagramSocket(port);				
				// TODO de ce avem nevoie de TO????
				serverSocket.setSoTimeout(200000);
				CentralSocketConnectorUDPReceiveJob rj = new CentralSocketConnectorUDPReceiveJob(serverSocket, this.ssi, this, processPool);
				this.submitReceiveJob(rj);
				System.out.println("[StreamingService] UDP Server socket pornit pe portul " + port);
			} catch (Exception e) {
				System.err.println("Exceptie la crearea DatagramSocket-ului: " + e);
				e.printStackTrace();
				return;
			}
		}
	}
	
	protected void submitReceiveJob(CentralSocketConnectorUDPReceiveJob rj) {
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
