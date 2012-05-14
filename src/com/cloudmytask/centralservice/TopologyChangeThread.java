package com.cloudmytask.centralservice;

import java.io.IOException;

import com.cloudmytask.GlobalConfig;
import com.cloudmytask.client.TopologyRequest;


public class TopologyChangeThread extends Thread{
	private CentralPrivateServiceInterface csi;
	private Boolean running = true;

	public TopologyChangeThread(CentralPrivateServiceInterface csi) throws IOException {
		super();

		this.running = false;
		this.csi = csi;
	}
	
	public synchronized void startRunning() {
		this.running = true;
		this.start();
	}
	
	public synchronized boolean isRunning() {
		return this.running;
	}
	
	public synchronized void stopRunning() {
		this.running = false;
	}
	
	public void run() {
		System.out.println("[TopologyUpdatetListener-" + this.hashCode() + "] Am pornit");

	//	while (this.isRunning()) {
			try {
			
				Thread.sleep(2000);
				
				// trimitere topologie catre instantele clienti
				TopologyRequest update = new TopologyRequest("topology update", TopologyRequest.UPDATE_TOPOLOGY);
				
				update.connections = GlobalConfig.connections;
				this.csi.sendTopology(update);
				System.out.println("[TopologyChangedThread] + topology changed send to service instances");
				
				}
			catch (Exception e) {
				System.err.println("[TopologyUpdatetListener-" + this.hashCode() + "] Exceptie la send: " + e);
				e.printStackTrace();
			}
	//	}

	}
}
