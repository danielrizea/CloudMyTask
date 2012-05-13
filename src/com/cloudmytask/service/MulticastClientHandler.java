package com.cloudmytask.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClientHandler implements MulticastClientPublicInterface{
	private MulticastSocket socket;
	private InetAddress groupAddr;
	DatagramPacket packet;

	public MulticastClientHandler(MulticastSocket socket, InetAddress groupAddr) {
		super();
		this.socket = socket;
		this.groupAddr = groupAddr;
	}

	public byte[] receivePacketData(byte[] data) throws IOException {
		this.packet = new DatagramPacket(data, data.length);
		socket.receive(packet);
		return this.packet.getData();
	}

	public void registerForGroup(MulticastGroup group) throws IOException {
		this.socket = new MulticastSocket(group.getPort());
		groupAddr = group.getGroupAddress();
		socket.joinGroup(groupAddr);		
	}

	public void unregisterFromGroup(MulticastGroup group) throws IOException {
		socket.leaveGroup(group.getGroupAddress());
		socket.close();  
	}

}