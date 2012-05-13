package com.cloudmytask.centralservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.cloudmytask.service.MulticastGroup;

public class MulticastServerHandler {

	private MulticastGroup group;
	private DatagramSocket socket;
	
	public MulticastServerHandler(MulticastGroup group) throws SocketException {
		super();
		this.group = group;
		this.socket = new DatagramSocket();
	}
	
	public void sendPacket(Object update) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(update);
		byte[] data = baos.toByteArray();
		
		System.out.println("[Multicast send handler] send data");
		
		DatagramPacket packet = new DatagramPacket(data, 0, data.length, group.getGroupAddress(), group.getPort());
		socket.send(packet);
	
		System.out.println("[Multicast send handler] data sent");
		
	}
}
