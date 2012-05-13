package com.cloudmytask.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClientHandler implements MulticastClientPublicInterface{
	private MulticastSocket socket;
	private InetAddress groupAddr;
	private DatagramPacket packet;

	public MulticastClientHandler(MulticastSocket socket, InetAddress groupAddr) {
		super();
		this.socket = socket;
		this.groupAddr = groupAddr;
	}

	public DatagramPacket receivePacketData() throws IOException {
		byte[] receiveBuffer = new byte[16384];
		this.packet = new DatagramPacket(receiveBuffer,receiveBuffer.length);
		socket.receive(packet);
		//return this.packet.getData();
		return this.packet;
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