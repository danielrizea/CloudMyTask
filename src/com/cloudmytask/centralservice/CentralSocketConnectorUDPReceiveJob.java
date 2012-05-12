package com.cloudmytask.centralservice;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

import javax.sql.PooledConnection;

import com.cloudmytask.client.Request;
import com.cloudmytask.service.CMTPublicServiceInterface;

// Acest tip de job asteapta primirea unui singur pachet, pe un socket UDP.
// La un moment dat, exista doar un singur job care asteapta pachete pe un anumit socket UDP.
public class CentralSocketConnectorUDPReceiveJob implements Runnable {
	private CentralPublicServiceInterface ssi;
	private CentralServiceSocketConnectorUDP sssc;
	private DatagramSocket serverSocket;
	
	// pool de threaduri care se ocupa de procesarea cererii
	private ExecutorService processPool;
	
	public CentralSocketConnectorUDPReceiveJob(DatagramSocket serverSocket, CentralPublicServiceInterface ssi, CentralServiceSocketConnectorUDP sssc, ExecutorService processPool) {
		this.serverSocket = serverSocket;
		this.ssi = ssi;
		this.sssc = sssc;
		this.processPool = processPool;
	}
	
	public void run() {
		byte[] buf = new byte[16384];

		while(true){
			
			try {
				DatagramPacket packet = new DatagramPacket(buf, 0, buf.length);
				this.serverSocket.receive(packet);
				InetAddress clientAddr = packet.getAddress();
				int clientPort = packet.getPort();
				CentralSocketConnectorUDPCallbackObject scco = new CentralSocketConnectorUDPCallbackObject(this.serverSocket, clientAddr, clientPort);
				
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
				Request request = (Request) ois.readObject();
				ois.close();
				
				// runnable care decide ce actiune sa ia 
				CentralSocketConnectorUDPProcessJob processJob = new CentralSocketConnectorUDPProcessJob(ssi, scco, request);
				
				if(CentralServiceSocketConnectorUDP.behaviour == 0){
					Thread newProcessThread = new Thread(processJob);
					newProcessThread.start();
					// to wait or not to wait with join
				}
				
				if(CentralServiceSocketConnectorUDP.behaviour == 1){
					
					if(!processPool.isTerminated())
						processPool.submit(processJob);
				}
				
				
			} catch (Exception e) {
				System.err.println("Exceptie la socket-ul UDP " + this.serverSocket + " : " + e);
				e.printStackTrace();
			}
		
		}
		
		// join threads

	}
}
