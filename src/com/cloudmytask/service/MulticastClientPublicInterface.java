package com.cloudmytask.service;

import java.io.IOException;
import java.net.DatagramPacket;

public interface MulticastClientPublicInterface {

	// inregistrare la grup de multicast
	public void registerForGroup(MulticastGroup group) throws IOException;
	
	// iesire din grup de multicast
	public void unregisterFromGroup(MulticastGroup group) throws IOException;

	// primire date
	//public byte[] receivePacketData() throws IOException;
	public DatagramPacket receivePacketData() throws IOException;
	
}