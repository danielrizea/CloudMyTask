package com.cloudmytask.connectors.tcp;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import javax.sql.PooledConnection;

import com.cloudmytask.client.Request;
import com.cloudmytask.connectors.udp.CMTServiceSocketConnectorUDP;
import com.cloudmytask.service.CMTPublicServiceInterface;

// Acest tip de job asteapta primirea unui singur pachet, pe un socket UDP.
// La un moment dat, exista doar un singur job care asteapta pachete pe un anumit socket UDP.
public class SocketConnectorTCPReceiveJob implements Runnable {
	private CMTPublicServiceInterface ssi;
	private CMTServiceSocketConnectorTCP sssc;
	private ServerSocket serverSocket;
	
	// pool de threaduri care se ocupa de procesarea cererii
	private ExecutorService processPool;
	
	public SocketConnectorTCPReceiveJob(ServerSocket serverSocket, CMTPublicServiceInterface ssi, CMTServiceSocketConnectorTCP sssc, ExecutorService processPool) {
		this.serverSocket = serverSocket;
		this.ssi = ssi;
		this.sssc = sssc;
		this.processPool = processPool;
	}
	
	public void run() {

		while(true){
			Socket clientSocket = null;
			try {

				//accepti conexiunea 
				clientSocket = this.serverSocket.accept();

				InetAddress clientAddr = this.serverSocket.getInetAddress();
				int clientPort = this.serverSocket.getLocalPort();
				
				SocketConnectorTCPCallbackObject scco = new SocketConnectorTCPCallbackObject(serverSocket, clientAddr, clientPort, clientSocket);

				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				Request request = (Request) ois.readObject();
				
				//ois.close();
				
				// runnable care decide ce actiune sa ia 
				SocketConnectorTCPProcessJob processJob = new SocketConnectorTCPProcessJob(ssi, scco, request);
				
				if(CMTServiceSocketConnectorUDP.behaviour == 0){
					Thread newProcessThread = new Thread(processJob);
					newProcessThread.start();
					// to wait or not to wait with join
				}
				
				if(CMTServiceSocketConnectorUDP.behaviour == 1){
					
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
