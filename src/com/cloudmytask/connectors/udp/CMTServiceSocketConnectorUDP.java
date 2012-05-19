package com.cloudmytask.connectors.udp;

import java.net.*;
import java.util.concurrent.*;

import com.cloudmytask.service.CMTPublicServiceInterface;

public class CMTServiceSocketConnectorUDP {
	private CMTPublicServiceInterface ssi;
	private int ports[];
	
	private ExecutorService receivePool;
	private ExecutorService processPool;
	
	//TODO param
	public static int processThreadsInPool = 10;
	
	// 0 -> 1 thread per request
	// 1 - > use thread pool
	//TODO param
	public static int behaviour = 0;
	
	
	public CMTServiceSocketConnectorUDP(CMTPublicServiceInterface ssi, int ports[]) {
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
				// pe fiecare port se creaza un DatagramSocket: fol pentru primit/trimis pachete
				DatagramSocket serverSocket = new DatagramSocket(port);
				serverSocket.setSoTimeout(200000);
				SocketConnectorUDPReceiveJob rj = new SocketConnectorUDPReceiveJob(serverSocket, this.ssi, this, processPool);
				this.submitReceiveJob(rj);
				System.out.println("[StreamingService] UDP Server socket pornit pe portul " + port);
			} catch (Exception e) {
				System.err.println("Exceptie la crearea DatagramSocket-ului: " + e);
				e.printStackTrace();
				return;
			}
		}
	}
	
	protected void submitReceiveJob(SocketConnectorUDPReceiveJob rj) {
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
