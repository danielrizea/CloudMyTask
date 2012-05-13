package com.cloudmytask.service;

import java.io.IOException;

public interface MulticastClientPublicInterface {

	// inregistrare la grup de multicast
	public void registerForGroup(MulticastGroup group) throws IOException;
	
	// iesire din grup de multicast
	public void unregisterFromGroup(MulticastGroup group) throws IOException;

	// primire date
	public byte[] receivePacketData(byte[] data) throws IOException;
}