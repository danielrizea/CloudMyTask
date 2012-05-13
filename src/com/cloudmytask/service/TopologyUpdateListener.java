package com.cloudmytask.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class TopologyUpdateListener extends Thread{
	private MulticastClientHandler multicastHandler;
	private boolean running;
	private MulticastGroup group;
	
	public TopologyUpdateListener(MulticastGroup group) throws IOException {
		super();
		this.multicastHandler = new MulticastClientHandler(new MulticastSocket(group.getPort()), group.getGroupAddress());
		this.running = false;
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
				
				DatagramPacket packet = multicastHandler.receivePacketData();

				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
				MulticastGroup mg = (MulticastGroup) ois.readObject();
				System.out.println("[TopologyUpdatetListener] Am primit lista de stream-uri  de la " + packet.getAddress() + ":" + packet.getPort() + "(Adr multicast=" + mg.getGroupAddress().getHostName() + "; UDPPort=" + mg.getPort() + ")");
				//StreamAnnouncement sa = (StreamAnnouncement) ois.readObject();
				//System.out.println("[StreamAnnouncementListener-" + this.hashCode() + "] Am primit lista de stream-uri " + sa.streamIds + " de la " + packet.getAddress() + ":" + packet.getPort() + "(TCPPort=" + sa.TCPPort + "; UDPPort=" + sa.UDPPort + ")");
				ois.close();

				//this.counter++;
				//StreamingServiceTCPUDPClient ssci = new StreamingServiceTCPUDPClient((this.id + 1) * 100000 + this.counter, packet.getAddress().getHostAddress(), sa.TCPPort, sa.UDPPort, this.clientIP, this.UDPclientPort);
				//this.sspi.receivedStreamAnnouncement(sa.streamIds, ssci);
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
