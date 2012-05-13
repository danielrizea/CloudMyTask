package com.cloudmytask.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.cloudmytask.client.TopologyRequest;


public class TopologyUpdateListener extends Thread{
	private MulticastClientHandler multicastHandler;
	private boolean running;
	private MulticastGroup group;
	private MachineInfo machineInfo;
	
	private String log = "TopologyUpdateListener";
	public TopologyUpdateListener(MulticastGroup group, MachineInfo machineInfo) throws IOException {
		super();
		this.multicastHandler = new MulticastClientHandler(new MulticastSocket(group.getPort()), group.getGroupAddress());
		this.running = false;
		this.machineInfo = machineInfo;
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
		System.out.println("[StreamAnnouncementListener-" + this.hashCode() + "] Am pornit");

		byte[] receiveBuffer = new byte[16384];

		while (this.isRunning()) {
			try {
				//DatagramPacket packet = new DatagramPacket(receiveBuffer, 0, receiveBuffer.length);
				//this.socket.receive(packet);
				//System.out.println("[TopologyUpdatetListener "+machineInfo.id+"] HERE in topology lsintener");
				
				DatagramPacket packet = multicastHandler.receivePacketData();

				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
				TopologyRequest mg = (TopologyRequest) ois.readObject();
				
				System.out.println("[TopologyUpdatetListener "+machineInfo.id+"] Am primit update topologie " + packet.getAddress() + ":" + packet.getPort());
				machineInfo.writeToLogFile(log, "Am primit update topologie " + packet.getAddress() + ":" + packet.getPort());
				//StreamAnnouncement sa = (StreamAnnouncement) ois.readObject();
				//System.out.println("[StreamAnnouncementListener-" + this.hashCode() + "] Am primit lista de stream-uri " + sa.streamIds + " de la " + packet.getAddress() + ":" + packet.getPort() + "(TCPPort=" + sa.TCPPort + "; UDPPort=" + sa.UDPPort + ")");
				ois.close();

				
				//process topology changes
				
				synchronized (machineInfo) {
				
					machineInfo.neighbours = mg.connections[machineInfo.id];
				}
			} catch (Exception e) {
				System.err.println("[TopologyUpdatetListener-" + this.hashCode() + "] Exceptie la receive: " + e);
				e.printStackTrace();
			}
		}

		try {
			this.multicastHandler.unregisterFromGroup(group);
		} catch (Exception e) {
			System.err.println("[TopologyUpdatetListener-" + this.hashCode() + "] Eroare la leaveGroup: " + e);
			e.printStackTrace();
		}

		System.out.println("[TopologyUpdatetListener-" + this.hashCode() + "] M-am oprit");
	}
}
