package com.cloudmytask.centralservice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MulticastServerHandler {

	private InetAddress multicastAddr;
	private int portNo;
	private DatagramSocket socket;
	
	public MulticastServerHandler(InetAddress multicastAddr, int portNo) throws SocketException {
		super();
		this.multicastAddr = multicastAddr;
		this.portNo = portNo;
		this.socket = new DatagramSocket();
	}
	
	public void sendPacket(byte[] data) throws IOException
	{
		DatagramPacket packet = new DatagramPacket(data, 0, data.length, multicastAddr, portNo);
		socket.send(packet);
	}
}
